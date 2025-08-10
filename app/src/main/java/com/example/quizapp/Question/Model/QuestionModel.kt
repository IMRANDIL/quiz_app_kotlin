package com.example.quizapp.Question.Model

import android.os.Parcelable
import com.example.quizapp.network.models.Category
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestionModel(
    val id: String = "",  // Changed to String to match API
    val question: String?,
    val answer_1: String?,
    val answer_2: String?,
    val answer_3: String?,
    val answer_4: String?,
    val correct_answer: String?,
    val score: Int,
    val pickPath: String?,
    val clickedAnswer: String?,
    val category: Category? // ✅ Added category field
): Parcelable