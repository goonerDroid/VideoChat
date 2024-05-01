package com.sublime.videochat.util

import kotlinx.coroutines.flow.MutableStateFlow

object AppConfig {

    //State of config values
    val currentEnvironment = MutableStateFlow<StreamEnvironment?>(null)

    val availableEnvironments = listOf(//TODO Change the sharelink, display name
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
}