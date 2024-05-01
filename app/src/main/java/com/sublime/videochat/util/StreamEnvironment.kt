package com.sublime.videochat.util

import androidx.annotation.Keep
import com.squareup.moshi.Json


@Keep
data class StreamEnvironment(
    @Json(name = "env") var env: String,
    @Json(name = "aliases") var aliases: List<String> = emptyList(),
    @Json(name = "displayName") var displayName: String,
    @Json(name = "sharelink") var sharelink: String? = null,
)