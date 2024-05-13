package com.sublime.videochat.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.sublime.videochat.R
import com.sublime.videochat.data.GoogleAccountRepository
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.getstream.video.android.datastore.delegate.StreamUserDataStore
import javax.inject.Singleton

@dagger.Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserDataStore(): StreamUserDataStore {
        return StreamUserDataStore.instance()
    }

    @Provides
    @Singleton
    fun provideGoogleSignInClient(
        @ApplicationContext context: Context,
    ): GoogleSignInClient = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
//        .requestIdToken(context.getString(R.string.default_web_client_id)) //TODO Check this token
        .requestScopes(Scope("https://www.googleapis.com/auth/directory.readonly"))
        .build()
        .let { gso -> GoogleSignIn.getClient(context, gso) }

    @Provides
    fun provideGoogleAccountRepository(
        @ApplicationContext context: Context,
        googleSignInClient: GoogleSignInClient,
    ) = GoogleAccountRepository(context, googleSignInClient)
}
