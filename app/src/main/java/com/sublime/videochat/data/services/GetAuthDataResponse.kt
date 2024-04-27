package com.sublime.videochat.data.services

import kotlinx.serialization.Serializable

@Serializable
data class GetAuthDataResponse(val userId: String, val apiKey: String, val token: String)