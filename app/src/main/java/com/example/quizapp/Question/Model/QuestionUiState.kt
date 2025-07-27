package com.example.quizapp.Question.Model

data class QuestionUiState(
    val questions:List<QuestionModel> = emptyList(),
    val currentIndex:Int=0,
    val score:Int=0,
)
