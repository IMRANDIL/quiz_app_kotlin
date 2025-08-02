package com.example.quizapp.Question

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

@Composable
fun QuestionScreen(
    questions: List<QuestionModel>,
    onFinish: (finalScore: Int) -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    var state by remember {
        mutableStateOf(
            value = QuestionUiState(questions = questions)
        )
    }

    val currentQuestion = state.questions[state.currentIndex]

    // ✅ Fix: Properly sync selectedAnswer with current question's clickedAnswer
    var selectedAnswer by remember(state.currentIndex) {
        mutableStateOf(currentQuestion.clickedAnswer)
    }

    val context = LocalContext.current
    val imageResId = remember(currentQuestion.pickPath) {
        context.resources.getIdentifier(
            currentQuestion.pickPath ?: "",
            "drawable",
            context.packageName
        )
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
                    text = "Single Player",
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

                // ✅ Check if current question is answered
                val isCurrentQuestionAnswered = currentQuestion.clickedAnswer != null

                IconButton(
                    onClick = {
                        // ✅ Only allow going back if current question is answered AND not on first question
                        if (state.currentIndex > 0 && isCurrentQuestionAnswered) {
                            state = state.copy(currentIndex = state.currentIndex - 1)
                            // selectedAnswer will be updated automatically by remember(state.currentIndex)
                        }
                    },
                    enabled = state.currentIndex > 0 && isCurrentQuestionAnswered // ✅ Disable if can't go back
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.left_arrow),
                        contentDescription = "left arrow",
                        tint = if (state.currentIndex > 0 && isCurrentQuestionAnswered)
                            colorResource(id = R.color.navy_blue)
                        else
                            Color.Gray // ✅ Gray out when disabled
                    )
                }

                IconButton(
                    onClick = {
                        if (state.currentIndex == state.questions.size - 1) {
                            // ✅ Only finish if current question is answered
                            if (isCurrentQuestionAnswered) {
                                val finalScore = calculateFinalScore(state.questions)
                                onFinish(finalScore)
                            }
                        } else {
                            // ✅ Only allow going forward if current question is answered
                            if (isCurrentQuestionAnswered) {
                                state = state.copy(currentIndex = state.currentIndex + 1)
                                // selectedAnswer will be updated automatically by remember(state.currentIndex)
                            }
                        }
                    },
                    enabled = isCurrentQuestionAnswered // ✅ Disable if question not answered
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.right_arrow),
                        contentDescription = "right arrow",
                        tint = if (isCurrentQuestionAnswered)
                            colorResource(id = R.color.navy_blue)
                        else
                            Color.Gray // ✅ Gray out when disabled
                    )
                }
            }
        }

        item {
            LinearProgressIndicator(
                progress = (state.currentIndex + 1).toFloat() / state.questions.size,
                color = colorResource(id = R.color.orange),
                trackColor = Color(0xffd1d1d1),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(14.dp)
                    .clip(shape = RoundedCornerShape(percent = 50))
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
                // ✅ Fix: Update the question's clicked answer without changing total score
                val updatedQuestions = state.questions.toMutableList()
                updatedQuestions[state.currentIndex] = updatedQuestions[state.currentIndex].copy(
                    clickedAnswer = answerLetter
                )

                selectedAnswer = answerLetter
                state = state.copy(questions = updatedQuestions)
                // Note: We don't update score here anymore - calculate it at the end
            }
        }

        item {
            Spacer(Modifier.height(32.dp))
        }
    }
}

// ✅ Helper function to calculate final score
private fun calculateFinalScore(questions: List<QuestionModel>): Int {
    return questions.sumOf { question ->
        if (question.clickedAnswer == question.correct_answer) {
            question.score
        } else {
            0
        }
    }
}

@Preview
@Composable
fun QuestionScreenPreview() {
    val questions = listOf(
        QuestionModel(
            id = 1,
            question = "What is the capital of France?",
            answer_1 = "Paris",
            answer_2 = "London",
            answer_3 = "Berlin",
            answer_4 = "Madrid",
            correct_answer = "a", // Should be "a" not "Paris"
            score = 10,
            pickPath = "q_1",
            clickedAnswer = null
        )
    )
    QuestionScreen(questions = questions)
}