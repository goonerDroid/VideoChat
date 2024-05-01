package com.sublime.videochat.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sublime.videochat.util.AppConfig
import com.sublime.videochat.util.LockScreenOrientation
import io.getstream.video.android.compose.theme.VideoTheme

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    autoLogIn: Boolean = true,
    navigateToCallJoin: () -> Unit,
) {

    VideoTheme {
        LockScreenOrientation(orientation = Configuration.ORIENTATION_PORTRAIT)

        val uiState by loginViewModel.uiState.collectAsState(initial = LoginUiState.Nothing)

        val isLoading by remember(uiState) {
            mutableStateOf(
                uiState !is LoginUiState.Nothing && uiState !is LoginUiState.SignInFailure,
            )
        }

        val selectedEnv by AppConfig.currentEnvironment.collectAsStateWithLifecycle()
        val availableEnv by remember {
            mutableStateOf(AppConfig.availableEnvironments)
        }

        val availableLogins = listOf("google", "email", "guest")
        var isShowingEmailLoginDialog by remember { mutableStateOf(false) }
    }

}