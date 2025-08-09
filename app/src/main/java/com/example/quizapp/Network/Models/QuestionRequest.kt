package com.example.quizapp.network.models

// This represents the data we send TO MongoDB when creating a question
data class QuestionRequest(
    val question: String,
    val answer_1: String,
    val answer_2: String,
    val answer_3: String,
    val answer_4: String,
    val correct_answer: String,
    val score: Int = 10,
    val category: String? = null
) {
    // Validation helper function
    fun isValid(): Pair<Boolean, String?> {
        return when {
            question.isBlank() -> false to "Question cannot be empty"
            question.length < 10 -> false to "Question must be at least 10 characters long"
            answer_1.isBlank() -> false to "Option A cannot be empty"
            answer_2.isBlank() -> false to "Option B cannot be empty"
            answer_3.isBlank() -> false to "Option C cannot be empty"
            answer_4.isBlank() -> false to "Option D cannot be empty"
            correct_answer.isBlank() -> false to "Correct answer cannot be empty"
            !listOf(answer_1, answer_2, answer_3, answer_4).contains(correct_answer) ->
                false to "Correct answer must match one of the options exactly"
            score < 1 || score > 100 -> false to "Score must be between 1 and 100"
            category != null && category.length > 50 -> false to "Category cannot exceed 50 characters"
            else -> true to null
        }
    }
}