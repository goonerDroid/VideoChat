package com.sublime.videochat.data

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.sublime.videochat.models.GoogleAccount
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GoogleAccountRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val googleSignInClient: GoogleSignInClient
) {

    private val baseUrl = "https://people.googleapis.com/v1/people:listDirectoryPeople"


    fun getCurrentUser(): GoogleAccount {
        val currentUser = GoogleSignIn.getLastSignedInAccount(context)
        return GoogleAccount(
            email = currentUser?.email ?: "",
            id = currentUser?.id ?: "",
            name = currentUser?.displayName ?: "",
            photoUrl = currentUser?.photoUrl?.toString(),
            isFavorite = false,
        )
    }
}