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
)