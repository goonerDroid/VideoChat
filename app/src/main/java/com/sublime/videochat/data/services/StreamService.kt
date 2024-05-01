package com.sublime.videochat.data.services

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

interface StreamService {
    @GET("token")
    suspend fun getAuthData(
        @Query("user_id") userId: String?,
    ): GetAuthDataResponse

    companion object {
        private const val BASE_URL = "http://localhost:8090/"

        private val json = Json { ignoreUnknownKeys = true }

        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

        val instance = retrofit.create<StreamService>()
    }
}