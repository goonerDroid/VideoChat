package com.sublime.videochat.ui

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.sublime.videochat.data.GoogleAccountRepository
import com.sublime.videochat.data.services.GetAuthDataResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.video.android.datastore.delegate.StreamUserDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.shareIn
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStore: StreamUserDataStore,
    private val googleSignInClient: GoogleSignInClient,
    private val googleAccountRepository: GoogleAccountRepository
) : ViewModel() {
    var autoLogin: Boolean = true
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

    private fun signInSuccess(userId: String): Flow<LoginUiState> {
        TODO("Not yet implemented")
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