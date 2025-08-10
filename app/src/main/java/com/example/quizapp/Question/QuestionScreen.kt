package com.example.quizapp.Question

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.Question.Model.QuestionModel
import com.example.quizapp.Question.Model.QuestionUiState
import com.example.quizapp.Question.components.AnswerItem
import com.example.quizapp.R
import kotlinx.coroutines.delay

@Composable
fun QuestionScreen(
    questions: List<QuestionModel>,
    categoryName: String? = null,
    categoryId: String? = null,
    onFinish: (finalScore: Int, correctAnswers: Int, totalPossibleScore: Int, timeSpent: Int) -> Unit = { _, _, _, _ -> },
    onBackClick: () -> Unit = {},
) {
    var state by remember {
        mutableStateOf(
            value = QuestionUiState(questions = questions)
        )
    }

    val currentQuestion = state.questions[state.currentIndex]

    var selectedAnswer by remember(state.currentIndex) {
        mutableStateOf(currentQuestion.clickedAnswer)
    }

    // Timer states
    val totalTimeInSeconds = 5 * 60 // 5 minutes = 300 seconds
    var timeRemaining by remember { mutableStateOf(totalTimeInSeconds) }
    var isTimerRunning by remember { mutableStateOf(true) }
    var showTimeWarning by remember { mutableStateOf(false) }

    // Calculate time spent
    val timeSpent = totalTimeInSeconds - timeRemaining

    // Timer effect
    LaunchedEffect(isTimerRunning) {
        while (isTimerRunning && timeRemaining > 0) {
            delay(1000) // Wait 1 second
            timeRemaining--

            // Show warning when less than 30 seconds remaining
            if (timeRemaining <= 30 && !showTimeWarning) {
                showTimeWarning = true
            }

            // Auto-submit when time is up
            if (timeRemaining == 0) {
                isTimerRunning = false
                val (finalScore, correctAnswers, totalPossibleScore) = calculateDetailedScore(state.questions)
                onFinish(finalScore, correctAnswers, totalPossibleScore, timeSpent)
            }
        }
    }

    val context = LocalContext.current
    val imageResId = remember(currentQuestion.pickPath) {
        if (currentQuestion.pickPath != null) {
            context.resources.getIdentifier(
                currentQuestion.pickPath,
                "drawable",
                context.packageName
            )
        } else {
            0
        }
    }

    // Format time display
    val minutes = timeRemaining / 60
    val seconds = timeRemaining % 60
    val timeDisplay = String.format("%02d:%02d", minutes, seconds)

    // Timer color based on remaining time
    val timerColor = when {
        timeRemaining <= 10 -> Color.Red
        timeRemaining <= 30 -> colorResource(id = R.color.orange)
        else -> colorResource(id = R.color.navy_blue)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.grey))
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    isTimerRunning = false // Stop timer when going back
                    onBackClick()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "Back"
                    )
                }
                Spacer(modifier = Modifier.width(width = 16.dp))
                Text(
                    text = if (categoryName != null) {
                        "$categoryName Quiz"
                    } else {
                        "Single Player"
                    },
                    fontSize = 20.sp,
                    color = colorResource(id = R.color.navy_blue),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                // Timer display
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (timeRemaining <= 30)
                            timerColor.copy(alpha = 0.1f)
                        else
                            Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⏱",
                            fontSize = 16.sp,
                            color = timerColor
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = timeDisplay,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = timerColor
                        )
                    }
                }
            }
        }

        // Warning message for low time
        if (showTimeWarning && timeRemaining > 0 && timeRemaining <= 30) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Red.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "⚠️ Time is running out! $timeRemaining seconds remaining",
                        color = Color.Red,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Question ${state.currentIndex + 1}/${state.questions.size}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                // Show points for this question
                Text(
                    text = "${currentQuestion.score} pts",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(id = R.color.orange)
                )
            }
        }

        item {
            LinearProgressIndicator(
                progress = { (state.currentIndex + 1).toFloat() / state.questions.size },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(14.dp)
                    .clip(shape = RoundedCornerShape(percent = 50)),
                color = colorResource(id = R.color.orange),
                trackColor = Color(0xffd1d1d1),
            )
        }

        item {
            Text(
                text = currentQuestion.question ?: "",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.navy_blue),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (imageResId != 0 && currentQuestion.pickPath != null) {
            item {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                        .height(200.dp)
                        .clip(shape = RoundedCornerShape(size = 12.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        itemsIndexed(
            listOf(
                currentQuestion.answer_1 ?: "",
                currentQuestion.answer_2 ?: "",
                currentQuestion.answer_3 ?: "",
                currentQuestion.answer_4 ?: ""
            )
        ) { index, answer ->
            val answerLetter = listOf("a", "b", "c", "d")[index]
            val isCorrect = selectedAnswer != null && answerLetter == currentQuestion.correct_answer
            val isWrong = selectedAnswer == answerLetter && !isCorrect

            AnswerItem(
                text = answer,
                isCorrect = isCorrect,
                isWrong = isWrong,
                isSelected = selectedAnswer != null
            ) {
                if (selectedAnswer == null && isTimerRunning) {
                    val updatedQuestions = state.questions.toMutableList()
                    updatedQuestions[state.currentIndex] = updatedQuestions[state.currentIndex].copy(
                        clickedAnswer = answerLetter
                    )

                    selectedAnswer = answerLetter
                    state = state.copy(questions = updatedQuestions)
                }
            }
        }

        item {
            Spacer(Modifier.height(32.dp))
        }
    }

    LaunchedEffect(selectedAnswer) {
        if (selectedAnswer != null) {
            delay(400)

            if (state.currentIndex == state.questions.size - 1) {
                // Last question - stop timer and finish
                isTimerRunning = false
                val (finalScore, correctAnswers, totalPossibleScore) = calculateDetailedScore(state.questions)
                onFinish(finalScore, correctAnswers, totalPossibleScore, timeSpent)
            } else {
                // Move to next question
                state = state.copy(currentIndex = state.currentIndex + 1)
            }
        }
    }
}

// Updated helper function that returns all needed values
private fun calculateDetailedScore(questions: List<QuestionModel>): Triple<Int, Int, Int> {
    var totalScore = 0
    var correctAnswers = 0
    var totalPossibleScore = 0

    questions.forEach { question ->
        // Each question is worth its score value (typically 10 points from DB)
        totalPossibleScore += question.score

        // Check if answer is correct
        if (question.clickedAnswer == question.correct_answer) {
            totalScore += question.score
            correctAnswers++
        }
    }

    // Log to debug
    Log.d("Score", "Questions: ${questions.size}")
    Log.d("Score", "Correct: $correctAnswers")
    Log.d("Score", "Score per question: ${questions.firstOrNull()?.score}")
    Log.d("Score", "Total Score: $totalScore")
    Log.d("Score", "Max Possible: $totalPossibleScore")

    return Triple(totalScore, correctAnswers, totalPossibleScore)
}