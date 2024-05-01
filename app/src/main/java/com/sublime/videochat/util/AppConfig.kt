package com.sublime.videochat.util

import kotlinx.coroutines.flow.MutableStateFlow

object AppConfig {

    //State of config values
    val currentEnvironment = MutableStateFlow<StreamEnvironment?>(null)
}