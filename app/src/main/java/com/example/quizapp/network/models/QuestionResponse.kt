package com.example.quizapp.network.models

import com.google.gson.annotations.SerializedName

data class QuestionResponse(
    @SerializedName("_id")
    val _id: String,              // MongoDB's unique ID for the question
    val question: String,
    val answer_1: String,
    val answer_2: String,
    val answer_3: String,
    val answer_4: String,
    val correct_answer: String,
    val score: Int,
    val category: Category?, // Changed to expect a CategoryResponse object
    @SerializedName("createdAt")
    val createdAt: String,        // When the question was created
    @SerializedName("updatedAt")
    val updatedAt: String         // When the question was last updated
) {
    // Helper function to get all answer options as a list
    fun getAllAnswers(): List<String> {
        return listOf(answer_1, answer_2, answer_3, answer_4)
    }

    // Helper function to get the correct answer index (0-based)
    fun getCorrectAnswerIndex(): Int {
        return getAllAnswers().indexOf(correct_answer)
    }

    // Helper function to check if an answer is correct
    fun isCorrectAnswer(answer: String): Boolean {
        return answer == correct_answer
    }

    // Helper function to get formatted category (capitalize first letter)
    fun getFormattedCategory(): String {
        return category?.name?.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        } ?: "General"
    }
}



// This wraps all responses from MongoDB (success or error)
data class ApiResponse<T>(
    val success: Boolean,         // true if operation worked, false if error
    val message: String,          // Success message or error description
    val data: T?,                 // The actual data (can be null if error)
    val errors: List<ValidationError>? = null // Validation errors if any
)

// For handling validation errors
data class ValidationError(
    val field: String,
    val message: String,
    val value: String?
)
