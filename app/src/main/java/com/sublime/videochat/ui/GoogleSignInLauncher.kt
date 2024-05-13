package com.sublime.videochat.ui

import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

@Composable
fun rememberLauncherForGoogleSignInActivityResult(
    onSignInSuccess: (email: String) -> Unit,
    onSignInFailed: () -> Unit,
): ManagedActivityResultLauncher<Intent, ActivityResult> {

    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.d("Google Sign In", "Checking activity result")

        if (result.resultCode != ComponentActivity.RESULT_OK) {
            Log.d("Google Sign In", "Failed with result not OK: ${result.resultCode}")
            onSignInFailed()
        } else {
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(result.data)

            try {
                val account = task.getResult(ApiException::class.java)
                val email = account.email

                if (email != null) {
                    Log.d("Google Sign In", "Successful: $email ")
                    onSignInSuccess(email)
                } else {
                    Log.d("Google Sign In", "Failed with null email")
                    onSignInFailed()
                }
            } catch (e: ApiException) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.d("Google Sign In", "Failed with ApiException: ${e.statusCode}")
                onSignInFailed()
            }
        }
    }

}