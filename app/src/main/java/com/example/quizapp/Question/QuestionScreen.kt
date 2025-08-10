package com.example.quizapp.Question

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.Question.Model.QuestionModel
import com.example.quizapp.Question.Model.QuestionUiState
import com.example.quizapp.Question.components.AnswerItem
import com.example.quizapp.R
import com.example.quizapp.network.models.Category
import kotlinx.coroutines.delay

@Composable
fun QuestionScreen(
    questions: List<QuestionModel>,
    categoryName: String? = null,
    categoryId: String? = null,
    onFinish: (finalScore: Int, correctAnswers: Int, totalPossibleScore: Int) -> Unit = { _, _, _ -> },
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
                IconButton(onClick = { onBackClick() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "Back"
                    )
                }
                Spacer(modifier = Modifier.width(width = 16.dp))
                Text(
                    text = if (categoryId != null) {
                        "$categoryId Quiz"
                    } else {
                        "Single Player"
                    },
                    fontSize = 20.sp,
                    color = colorResource(id = R.color.navy_blue),
                    fontWeight = FontWeight.Bold
                )
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
                if (selectedAnswer == null) {
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
                // Calculate detailed results
                val (finalScore, correctAnswers, totalPossibleScore) = calculateDetailedScore(state.questions)
                onFinish(finalScore, correctAnswers, totalPossibleScore)
            } else {
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
        // Add each question's score to total possible score
        totalPossibleScore += question.score

        // Check if answer is correct
        if (question.clickedAnswer == question.correct_answer) {
            totalScore += question.score
            correctAnswers++
        }
    }

    Log.d("QuestionScreen", "Score Calculation:")
    Log.d("QuestionScreen", "Correct Answers: $correctAnswers/${questions.size}")
    Log.d("QuestionScreen", "Score Earned: $totalScore")
    Log.d("QuestionScreen", "Total Possible Score: $totalPossibleScore")

    return Triple(totalScore, correctAnswers, totalPossibleScore)
}