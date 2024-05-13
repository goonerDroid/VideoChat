package com.sublime.videochat.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.getstream.log.taggedLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * Main entry point for remote / local configuration
 */
@OptIn(ExperimentalStdlibApi::class)
object AppConfig {
    // Constants
    private val logger by taggedLogger("RemoteConfig")
    private const val SHARED_PREF_NAME = "stream_video_app"
    private const val SELECTED_ENV = "selected_env_v2"

    //State of config values
    val currentEnvironment = MutableStateFlow<StreamEnvironment?>(null)

    val availableEnvironments = listOf(
        //TODO Change the sharelink, display name
        StreamEnvironment(
            env = "pronto",
            aliases = listOf("stream-calls-dogfood"),
            displayName = "Pronto",
            sharelink = "https://pronto.getstream.io/join/",
        ),
        StreamEnvironment(
            env = "demo",
            aliases = listOf(""),
            displayName = "Demo",
            sharelink = "https://getstream.io/video/demos/join/",
        ),
        StreamEnvironment(
            env = "staging",
            aliases = emptyList(),
            displayName = "Staging",
            sharelink = "https://staging.getstream.io/join/",
        ),
    )

    // Data
    private lateinit var environment: StreamEnvironment
    private lateinit var prefs: SharedPreferences

    // Utilities
    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    // API
    /**
     * Setup the remote configuration.
     * Will automatically put config into [AppConfig.config]
     *
     * @param context an android context.
     * @param coroutineScope the scope used to run [onLoaded]
     */
    @Suppress("KDocUnresolvedReference")
    @OptIn(DelicateCoroutinesApi::class)
    fun load(
        context: Context,
        coroutineScope: CoroutineScope = GlobalScope,
        onLoaded: suspend () -> Unit = {},
    ) {
        // Load prefs
        prefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        try {
            val jsonAdapter: JsonAdapter<StreamEnvironment> = moshi.adapter()
            val selectedEnvData = prefs.getString(SELECTED_ENV, null)
            val selectedEnvironment = selectedEnvData?.let {
                jsonAdapter.fromJson(it)
            }
            val which = selectedEnvironment ?: availableEnvironments[0]
            selectEnv(which)
            currentEnvironment.value = which
            coroutineScope.launch {
                onLoaded()
            }
        } catch (e: Exception) {
            logger.e(e) { "Failed to parse  remote config. Deeplinks not working!" }
        }
    }


    fun selectEnv(which: StreamEnvironment) {
        val jsonAdapter: JsonAdapter<StreamEnvironment> = moshi.adapter()
        // Select default environment from config if none is in prefs
        environment = which
        // Update selected env
        prefs.edit(commit = true) {
            putString(SELECTED_ENV, jsonAdapter.toJson(environment))
        }
        currentEnvironment.value = environment
    }
}