package com.example.quizapp.repository

import android.util.Log
import com.example.quizapp.network.models.*
import com.example.quizapp.R
import com.example.quizapp.network.CategoryRequest
import com.example.quizapp.network.NetworkConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class QuizRepository {

    private val apiService = NetworkConfig.quizApiService
    private val TAG = "QuizRepository"

    /**
     * Get all categories from backend
     */
    suspend fun getAllCategories(): Result<List<Category>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getAllCategories()
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.success && apiResponse.data != null) {
                        // Map CategoryResponse to Category model
                        val categoryList = apiResponse.data.map { categoryResponse ->
                            // Map icon names to drawable resources
                            val iconRes = when (categoryResponse.icon?.lowercase()) {
                                "science" -> R.drawable.cat1
                                "history" -> R.drawable.cat2
                                "sport", "sports" -> R.drawable.cat3
                                "art" -> R.drawable.cat4
                                // Add more mappings as needed
                                else -> R.drawable.ic_launcher_background
                            }

                            Category(
                                id = categoryResponse.id ?: categoryResponse._id ?: "",
                                name = categoryResponse.name,
                                description = categoryResponse.description,
                                iconRes = iconRes,
                                questionCount = categoryResponse.questionCount
                            )
                        }.sortedBy { it.name } // Sort by name or use order field

                        Result.success(categoryList)
                    } else {
                        Result.failure(Exception(apiResponse?.message ?: "Failed to fetch categories"))
                    }
                } else {
                    Result.failure(Exception("Error: ${response.code()}"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching categories", e)
                Result.failure(Exception("Unexpected error: ${e.message}"))
            }
        }
    }

    /**
     * Get all questions with optional filters
     */
    suspend fun getAllQuestions(
        category: String? = null,
        limit: Int = 10,
        page: Int = 1,
        difficulty: String? = null
    ): Result<List<QuestionResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Fetching questions - category: $category, limit: $limit, page: $page")

                val response = apiService.getAllQuestions(
                    category = category,
                    limit = limit,
                    page = page,
                    difficulty = difficulty
                )

                Log.d(TAG, "Response code: ${response.code()}")

                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.success && apiResponse.data != null) {
                        Log.d(TAG, "Questions fetched successfully: ${apiResponse.data.size} questions")
                        Result.success(apiResponse.data)
                    } else {
                        val errorMessage = apiResponse?.message ?: "Failed to fetch questions"
                        Log.e(TAG, "API returned error: $errorMessage")
                        Result.failure(Exception(errorMessage))
                    }
                } else {
                    val errorMessage = "Failed to fetch questions: ${response.code()}"
                    Log.e(TAG, "HTTP Error: $errorMessage")
                    Result.failure(Exception(errorMessage))
                }

            } catch (e: HttpException) {
                Log.e(TAG, "HTTP Exception: ${e.code()} - ${e.message()}")
                Result.failure(Exception("Network error: ${e.message()}"))
            } catch (e: SocketTimeoutException) {
                Log.e(TAG, "Timeout Exception", e)
                Result.failure(Exception("Request timed out. Please check your internet connection."))
            } catch (e: IOException) {
                Log.e(TAG, "IO Exception", e)
                Result.failure(Exception("Network error. Please check your internet connection."))
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected Exception", e)
                Result.failure(Exception("Unexpected error: ${e.message}"))
            }
        }
    }

    /**
     * Create a new question
     */
    suspend fun createQuestion(questionRequest: QuestionRequest): Result<QuestionResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Creating question: $questionRequest")

                val response = apiService.createQuestion(questionRequest)

                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.success && apiResponse.data != null) {
                        Log.d(TAG, "Question created successfully: ${apiResponse.data}")
                        Result.success(apiResponse.data)
                    } else {
                        val errorMessage = apiResponse?.message ?: "Unknown error occurred"
                        Log.e(TAG, "API returned error: $errorMessage")
                        Result.failure(Exception(errorMessage))
                    }
                } else {
                    val errorMessage = when (response.code()) {
                        400 -> "Invalid question data. Please check all fields."
                        409 -> "This question already exists."
                        429 -> "Too many requests. Please wait before creating another question."
                        500 -> "Server error. Please try again later."
                        else -> "Failed to create question: ${response.code()}"
                    }
                    Log.e(TAG, "HTTP Error: ${response.code()} - $errorMessage")
                    Result.failure(Exception(errorMessage))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error creating question", e)
                Result.failure(Exception("Unexpected error: ${e.message}"))
            }
        }
    }

    /**
     * Get questions by category
     */
    suspend fun getQuestionsByCategory(
        category: String,
        limit: Int = 10
    ): Result<List<QuestionResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Fetching questions for category: $category with limit: $limit")

                val response = apiService.getQuestionsByCategory(category, limit)

                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.success && apiResponse.data != null) {
                        Log.d(TAG, "Questions fetched successfully: ${apiResponse.data.size} questions")
                        Result.success(apiResponse.data)
                    } else {
                        val errorMessage = apiResponse?.message ?: "Failed to fetch questions"
                        Log.e(TAG, "API returned error: $errorMessage")
                        Result.failure(Exception(errorMessage))
                    }
                } else {
                    val errorMessage = "Failed to fetch questions: ${response.code()}"
                    Log.e(TAG, "HTTP Error: $errorMessage")
                    Result.failure(Exception(errorMessage))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching questions by category", e)
                Result.failure(Exception("Error: ${e.message}"))
            }
        }
    }

    /**
     * Create a new category
     */
    suspend fun createCategory(categoryRequest: CategoryRequest): Result<CategoryResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.createCategory(categoryRequest)

                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.success && apiResponse.data != null) {
                        Result.success(apiResponse.data)
                    } else {
                        Result.failure(Exception(apiResponse?.message ?: "Failed to create category"))
                    }
                } else {
                    Result.failure(Exception("Error: ${response.code()}"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error creating category", e)
                Result.failure(Exception("Error: ${e.message}"))
            }
        }
    }
}