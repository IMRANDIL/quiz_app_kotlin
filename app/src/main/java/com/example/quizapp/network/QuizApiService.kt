package com.example.quizapp.network

import com.example.quizapp.network.models.*
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.*

interface QuizApiService {

    // ============ QUESTION ENDPOINTS ============

    // Create a new question
    @POST("questions")
    suspend fun createQuestion(@Body question: QuestionRequest): Response<ApiResponse<QuestionResponse>>

    // Get all questions with filters
    @GET("questions")
    suspend fun getAllQuestions(
        @Query("category") category: String? = null,
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int = 1,
        @Query("difficulty") difficulty: String? = null
    ): Response<ApiResponse<List<QuestionResponse>>>

    // Get a single question by ID
    @GET("questions/{id}")
    suspend fun getQuestion(@Path("id") id: String): Response<ApiResponse<QuestionResponse>>

    // Get questions by category
    @GET("questions/category/{category}")
    suspend fun getQuestionsByCategory(
        @Path("category") category: String,
        @Query("limit") limit: Int = 10
    ): Response<ApiResponse<List<QuestionResponse>>>

    // Update question
    @PUT("questions/{id}")
    suspend fun updateQuestion(
        @Path("id") id: String,
        @Body question: QuestionRequest
    ): Response<ApiResponse<QuestionResponse>>

    // Delete question
    @DELETE("questions/{id}")
    suspend fun deleteQuestion(@Path("id") id: String): Response<ApiResponse<Any>>

    // ============ CATEGORY ENDPOINTS ============

    // Get all categories
    @GET("categories")
    suspend fun getAllCategories(): Response<ApiResponse<List<CategoryResponse>>>

    // Get category by ID
    @GET("categories/{id}")
    suspend fun getCategoryById(@Path("id") id: String): Response<ApiResponse<CategoryResponse>>

    // Create new category
    @POST("categories")
    suspend fun createCategory(@Body category: CategoryRequest): Response<ApiResponse<CategoryResponse>>

    // Update category
    @PUT("categories/{id}")
    suspend fun updateCategory(
        @Path("id") id: String,
        @Body category: CategoryRequest
    ): Response<ApiResponse<CategoryResponse>>

    // Delete category
    @DELETE("categories/{id}")
    suspend fun deleteCategory(@Path("id") id: String): Response<ApiResponse<Any>>
}

// Request model for creating/updating categories
data class CategoryRequest(
    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("icon")
    val icon: String? = null,

    @SerializedName("order")
    val order: Int? = null
)