package com.example.quizapp.network

import com.example.quizapp.network.models.ApiResponse
import com.example.quizapp.network.models.QuestionRequest
import com.example.quizapp.network.models.QuestionResponse
import retrofit2.Response
import retrofit2.http.*

// This interface defines what network operations we can do
interface QuizApiService {

    // Create a new question
    @POST("questions")
    suspend fun createQuestion(@Body question: QuestionRequest): Response<ApiResponse<QuestionResponse>>

    // Get all questions
    @GET("questions")
    suspend fun getAllQuestions(): Response<ApiResponse<List<QuestionResponse>>>

    // Get a single question by ID
    @GET("questions/{id}")
    suspend fun getQuestion(@Path("id") id: String): Response<ApiResponse<QuestionResponse>>

    // Get all categories
    @GET("questions/categories")
    suspend fun getAllCategories(): Response<ApiResponse<List<String>>>

}