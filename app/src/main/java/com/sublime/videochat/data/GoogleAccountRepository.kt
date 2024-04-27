package com.sublime.videochat.data

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GoogleAccountRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val googleSignInClient: GoogleSignInClient
) {

    private val baseUrl = "https://people.googleapis.com/v1/people:listDirectoryPeople"


}