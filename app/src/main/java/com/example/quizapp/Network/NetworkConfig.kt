package com.example.quizapp.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkConfig {

    // Your backend server URL - Update this based on how you're running your app
    private val BASE_URL = "http://10.0.2.2:3000/api/"

    // This logs network requests for debugging
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // This sets up the HTTP client with better error handling
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                // Add request ID for debugging
                .addHeader("X-Client", "Android-QuizApp")
                .build()
            chain.proceed(request)
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        // Add retry logic
        .retryOnConnectionFailure(true)
        .build()

    // This creates the main network client
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // This creates our API service
    val quizApiService: QuizApiService = retrofit.create(QuizApiService::class.java)

    // Helper function to get current base URL (for debugging)
    fun getCurrentBaseUrl(): String = BASE_URL
}