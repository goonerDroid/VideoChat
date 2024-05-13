package com.sublime.videochat.ui

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.sublime.videochat.BuildConfig
import com.sublime.videochat.data.GoogleAccountRepository
import com.sublime.videochat.data.services.GetAuthDataResponse
import com.sublime.videochat.data.services.StreamService
import com.sublime.videochat.util.AppConfig
import com.sublime.videochat.util.UserHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.log.streamLog
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.datastore.delegate.StreamUserDataStore
import io.getstream.video.android.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStore: StreamUserDataStore,
    private val googleSignInClient: GoogleSignInClient,
    private val googleAccountRepository: GoogleAccountRepository
) : ViewModel() {
    var autoLogIn: Boolean = true
    private val event: MutableSharedFlow<LoginEvent> = MutableSharedFlow()

    internal val uiState: SharedFlow<LoginUiState> = event.flatMapLatest { event ->
        when (event) {
            is LoginEvent.Loading -> flowOf(LoginUiState.Loading)
            is LoginEvent.GoogleSignIn -> flowOf(
                LoginUiState.GoogleSignIn(
                    signInIntent = googleSignInClient.signInIntent,
                ),
            )

            is LoginEvent.SignInSuccess -> signInSuccess(event.userId)
            is LoginEvent.SignInFailure -> flowOf(
                LoginUiState.SignInFailure(event.errorMsg),
            )

            else -> flowOf(LoginUiState.Nothing)
        }
    }.shareIn(viewModelScope, SharingStarted.Lazily, 0)

    fun handleUiEvent(event: LoginEvent) {
        viewModelScope.launch { this@LoginViewModel.event.emit(event) }
    }

    @Suppress("KotlinConstantConditions")
    fun signInIfValidUserExist() {
        viewModelScope.launch {
            val user = dataStore.user.firstOrNull()
            if (user != null) {
                handleUiEvent(LoginEvent.Loading)
                if (BuildConfig.BUILD_TYPE != "benchmark") {
                    delay(10)
                    handleUiEvent(LoginEvent.SignInSuccess(userId = user.id))
                }
            } else {
                if (autoLogIn) {
                    handleUiEvent(LoginEvent.Loading)
                    handleUiEvent(
                        LoginEvent.SignInSuccess(
                            UserHelper.generateRandomString(upperCaseOnly = true),
                        ),
                    )
                }
            }
        }
    }

    private fun signInSuccess(userId: String): Flow<LoginUiState> =
        AppConfig.currentEnvironment.flatMapLatest {
            if (it != null) {
                if (StreamVideo.isInstalled) {
                    flowOf(LoginUiState.AlreadyLoggedIn)
                } else {
                    try {
                        val authData = StreamService.instance.getAuthData(
                            userId = userId,
                        )
                        val loggedInGoogleUser =
                            if (autoLogIn) null else googleAccountRepository.getCurrentUser()
                        val user = User(
                            id = authData.userId,
                            // if autoLogIn is true it means we have a random user
                            name = if (autoLogIn) userId else loggedInGoogleUser?.name ?: "",
                            image = if (autoLogIn) "" else loggedInGoogleUser?.photoUrl ?: "",
                            role = "admin",
                            custom = mapOf("email" to authData.userId),
                        )
                        // Store the data in the demo app
                        dataStore.updateUser(user)
                        // Init the Video SDK with the data
//                        StreamVideoInitHelper.loadSdk(dataStore)//TODO Plug Stream Video SDK
                        flowOf(LoginUiState.SignInComplete(authData))
                    } catch (exception: Throwable) {
                        val message = "Sign in failed: ${exception.message ?: "Generic error"}"
                        streamLog { "Failed to fetch token - cause: $exception" }
                        flowOf(LoginUiState.SignInFailure(message))
                    }
                }
            } else {
                flowOf(LoginUiState.Loading)
            }
        }


}



sealed interface LoginUiState {

    object Nothing : LoginUiState

    object Loading : LoginUiState

    object AlreadyLoggedIn : LoginUiState

    data class GoogleSignIn(val signInIntent: Intent) : LoginUiState

    data class SignInComplete(val authData: GetAuthDataResponse) : LoginUiState

    data class SignInFailure(val errorMsg: String) : LoginUiState
}

sealed interface LoginEvent {
    object Nothing : LoginEvent

    object Loading : LoginEvent

    data class GoogleSignIn(val id: String = UUID.randomUUID().toString()) : LoginEvent

    data class SignInSuccess(val userId: String) : LoginEvent

    data class SignInFailure(val errorMsg: String) : LoginEvent
}