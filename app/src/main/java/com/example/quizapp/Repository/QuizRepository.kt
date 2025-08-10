package com.example.quizapp.repository

import android.util.Log
import com.example.quizapp.network.models.Category
import com.example.quizapp.R
import com.example.quizapp.network.NetworkConfig
import com.example.quizapp.network.models.QuestionRequest
import com.example.quizapp.network.models.QuestionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class QuizRepository {

    private val apiService = NetworkConfig.quizApiService
    private val TAG = "QuizRepository"

    /**
     * Create a new question
     */
    suspend fun createQuestion(questionRequest: QuestionRequest): Result<QuestionResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Creating question: $questionRequest")
                Log.d(TAG, "Using base URL: ${NetworkConfig.getCurrentBaseUrl()}")

                val response = apiService.createQuestion(questionRequest)

                Log.d(TAG, "Response code: ${response.code()}")
                Log.d(TAG, "Response body: ${response.body()}")

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
                    // Handle HTTP errors
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

            } catch (e: HttpException) {
                Log.e(TAG, "HTTP Exception: ${e.code()} - ${e.message()}")
                val errorMessage = when (e.code()) {
                    400 -> "Invalid request data"
                    401 -> "Authentication required"
                    403 -> "Access forbidden"
                    404 -> "Service not found"
                    500 -> "Server error"
                    else -> "Network error: ${e.message()}"
                }
                Result.failure(Exception(errorMessage))
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
     * Get all questions
     */
    suspend fun getAllQuestions(
        category: String? = null,
        limit: Int = 10,
        page: Int = 1
    ): Result<List<QuestionResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Fetching questions - category: $category, limit: $limit, page: $page")

                val response = apiService.getAllQuestions(
                    category = category,
                    limit = limit,
                    page = page
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
     * Get a single question by ID
     */
    suspend fun getQuestion(id: String): Result<QuestionResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Fetching question with ID: $id")

                val response = apiService.getQuestion(id)

                Log.d(TAG, "Response code: ${response.code()}")

                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.success && apiResponse.data != null) {
                        Log.d(TAG, "Question fetched successfully: ${apiResponse.data}")
                        Result.success(apiResponse.data)
                    } else {
                        val errorMessage = apiResponse?.message ?: "Question not found"
                        Log.e(TAG, "API returned error: $errorMessage")
                        Result.failure(Exception(errorMessage))
                    }
                } else {
                    val errorMessage = when (response.code()) {
                        404 -> "Question not found"
                        else -> "Failed to fetch question: ${response.code()}"
                    }
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
     * Test connection to backend
     */
    suspend fun testConnection(): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Testing connection to: ${NetworkConfig.getCurrentBaseUrl()}")

                // Try to get questions as a connection test
                val response = apiService.getAllQuestions()

                if (response.isSuccessful) {
                    Result.success("Connection successful!")
                } else {
                    Result.failure(Exception("Connection failed: ${response.code()}"))
                }

            } catch (e: Exception) {
                Log.e(TAG, "Connection test failed", e)
                Result.failure(Exception("Connection failed: ${e.message}"))
            }
        }
    }


    suspend fun getAllCategories(): Result<List<Category>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getAllCategories()
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.success && apiResponse.data != null) {
                        // Map List<String> -> List<Category>
                        val categoryList = apiResponse.data.mapIndexed { index, name ->
                            // Map category names to appropriate icons
                            val iconRes = when (name.lowercase()) {
                                "science" -> R.drawable.cat1
                                "history" -> R.drawable.cat2
                                "sport", "sports" -> R.drawable.cat3
                                "art" -> R.drawable.cat4
                                // Add more mappings for other categories if you have icons
                                else -> R.drawable.ic_launcher_background // default icon
                            }

                            Category(
                                id = (index + 1).toString(), // Generate ID based on index
                                name = name,
                                iconRes = iconRes
                            )
                        }
                        Result.success(categoryList)
                    } else {
                        Result.failure(Exception(apiResponse?.message ?: "Failed to fetch categories"))
                    }
                } else {
                    Result.failure(Exception("Error: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Unexpected error: ${e.message}"))
            }
        }
    }


    suspend fun getQuestionsByCategory(
        category: String,
        limit: Int = 10
    ): Result<List<QuestionResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Fetching questions for category: $category with limit: $limit")

                val response = apiService.getQuestionsByCategory(category, limit)

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



}