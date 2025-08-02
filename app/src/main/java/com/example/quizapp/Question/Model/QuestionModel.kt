package com.example.quizapp.Question.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize  // ✅ fixed


@Parcelize
data class QuestionModel(
    val id: Int,
    val question: String?,
    val answer_1: String?,
    val answer_2: String?,
    val answer_3: String?,
    val answer_4: String?,
    val correct_answer: String?,
    val score: Int,
    val pickPath: String?,
    val clickedAnswer: String?
): Parcelable
