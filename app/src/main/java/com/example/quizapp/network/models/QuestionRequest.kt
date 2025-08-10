// network/models/QuestionRequest.kt
package com.example.quizapp.network.models

import com.google.gson.annotations.SerializedName

data class QuestionRequest(
    @SerializedName("question")
    val question: String,

    @SerializedName("answer_1")
    val answer_1: String,

    @SerializedName("answer_2")
    val answer_2: String,

    @SerializedName("answer_3")
    val answer_3: String,

    @SerializedName("answer_4")
    val answer_4: String,

    @SerializedName("correct_answer")
    val correct_answer: String,

    @SerializedName("score")
    val score: Int = 10,

    @SerializedName("category")
    val category: String? = null, // Can be category name (backend will find/create)

    @SerializedName("categoryId")
    val categoryId: String? = null, // Or direct category ID

    @SerializedName("difficulty")
    val difficulty: String? = "medium" // easy, medium, hard
)