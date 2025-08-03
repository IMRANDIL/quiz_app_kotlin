package com.example.quizapp.repository

import android.util.Log
import com.example.quizapp.network.NetworkConfig
import com.example.quizapp.network.models.QuestionRequest
import com.example.quizapp.network.models.QuestionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuizRepository {

    private val apiService = NetworkConfig.quizApiService

    // Create a new question in MongoDB
    suspend fun createQuestion(question: QuestionRequest): Result<QuestionResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("QuizRepository", "Creating question: ${question.question}")

                val response = apiService.createQuestion(question)

                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.let { data ->
                        Log.d("QuizRepository", "Question created successfully with ID: ${data._id}")
                        Result.success(data)
                    } ?: Result.failure(Exception("No data received"))
                } else {
                    val errorMsg = response.body()?.message ?: "Unknown error"
                    Log.e("QuizRepository", "Failed to create question: $errorMsg")
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e("QuizRepository", "Network error creating question", e)
                Result.failure(e)
            }
        }
    }

    // Get all questions from MongoDB
    suspend fun getAllQuestions(): Result<List<QuestionResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("QuizRepository", "Getting all questions from MongoDB")

                val response = apiService.getAllQuestions()

                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.let { data ->
                        Log.d("QuizRepository", "Retrieved ${data.size} questions from MongoDB")
                        Result.success(data)
                    } ?: Result.failure(Exception("No data received"))
                } else {
                    val errorMsg = response.body()?.message ?: "Unknown error"
                    Log.e("QuizRepository", "Failed to get questions: $errorMsg")
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e("QuizRepository", "Network error getting questions", e)
                Result.failure(e)
            }
        }
    }

    // Test connection to MongoDB (for debugging)
    suspend fun testConnection(): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("QuizRepository", "Testing MongoDB connection...")

                val response = apiService.getAllQuestions()

                if (response.isSuccessful) {
                    Log.d("QuizRepository", "MongoDB connection successful!")
                    Result.success("Connection successful")
                } else {
                    Log.e("QuizRepository", "MongoDB connection failed: ${response.code()}")
                    Result.failure(Exception("Connection failed: ${response.code()}"))
                }
            } catch (e: Exception) {
                Log.e("QuizRepository", "MongoDB connection error", e)
                Result.failure(e)
            }
        }
    }
}