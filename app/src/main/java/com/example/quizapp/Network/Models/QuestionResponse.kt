package com.example.quizapp.network.models

// This represents the data MongoDB sends BACK to us after creating/getting a question
data class QuestionResponse(
    val _id: String,              // MongoDB's unique ID for the question
    val question: String,
    val answer_1: String,
    val answer_2: String,
    val answer_3: String,
    val answer_4: String,
    val correct_answer: String,
    val score: Int,
    val category: String?,
    val createdAt: String         // When the question was created
)

// This wraps all responses from MongoDB (success or error)
data class ApiResponse<T>(
    val success: Boolean,         // true if operation worked, false if error
    val message: String,          // Success message or error description
    val data: T?                  // The actual data (can be null if error)
)