package com.example.quizapp.Question

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.quizapp.Question.Model.QuestionModel
import com.example.quizapp.R
import com.example.quizapp.Score.ScoreActivity
import com.example.quizapp.network.models.Category
import com.example.quizapp.repository.QuizRepository
import com.example.quizapp.ui.theme.QuizAppTheme
import kotlinx.coroutines.launch

class QuestionActivity : ComponentActivity() {

    private lateinit var repository: QuizRepository
    private var categoryName: String? = null
    private var categoryId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        repository = QuizRepository()

        // FIX: Correctly map the intent parameters
        // "category" contains the category NAME (e.g., "Science")
        // "categoryId" contains the category ID (e.g., "1")
        categoryName = intent.getStringExtra("category")      // This is the category NAME
        categoryId = intent.getStringExtra("categoryId")      // This is the category ID

        val categoryObject = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("categoryObject", Category::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Category>("categoryObject")
        }

        Log.d("QuestionActivity", "Category Name: $categoryName, Category ID: $categoryId")

        // Check if questions were passed directly (from MainActivity)
        val receivedList = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra("questions", QuestionModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra<QuestionModel>("questions")
        }

        if (receivedList != null && receivedList.isNotEmpty()) {
            // If questions were passed directly, use them
            Log.d("QuestionActivity", "Using ${receivedList.size} questions from intent")

            // Filter by category ID if specified
            val filteredQuestions = if (categoryId != null) {
                receivedList.filter { question ->
                    question.category?.id?.equals(categoryId, ignoreCase = true) == true
                }
            } else {
                receivedList
            }

            if (filteredQuestions.isNotEmpty()) {
                // Limit to 10 questions for 5-minute timer
                val limitedQuestions = filteredQuestions.take(10)
                startQuizWithQuestions(ArrayList(limitedQuestions))
            } else {
                showNoQuestionsForCategory()
            }
        } else if (categoryId != null) {
            // Load questions for the specific category from API
            // Use categoryName for the API call
            loadQuestionsForCategory(categoryId!!)
        } else {
            // No questions and no category - load all questions
            loadAllQuestions()
        }
    }

    private fun loadQuestionsForCategory(category: String) {
        // Show loading state
        setContent {
            QuizAppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(id = R.color.grey)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = colorResource(id = R.color.purple),
                            modifier = Modifier.size(50.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Loading $categoryName questions...",  // Use category name for display
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.navy_blue),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Load questions from API
        lifecycleScope.launch {
            try {
                repository.getAllQuestions(
                    category = category,  // Pass the category NAME to the API
                    limit = 10 // Limit to 10 questions for 5-minute timer
                ).onSuccess { questionList ->
                    Log.d("QuestionActivity", "Loaded ${questionList.size} questions for $category")

                    if (questionList.isNotEmpty()) {
                        // Take only first 10 questions if more are returned
                        val limitedQuestions = questionList.take(10)

                        // Convert API response to QuestionModel
                        val questions = limitedQuestions.map { apiQuestion ->
                            QuestionModel(
                                id = apiQuestion._id,
                                question = apiQuestion.question,
                                answer_1 = apiQuestion.answer_1,
                                answer_2 = apiQuestion.answer_2,
                                answer_3 = apiQuestion.answer_3,
                                answer_4 = apiQuestion.answer_4,
                                correct_answer = when(apiQuestion.correct_answer) {
                                    apiQuestion.answer_1 -> "a"
                                    apiQuestion.answer_2 -> "b"
                                    apiQuestion.answer_3 -> "c"
                                    apiQuestion.answer_4 -> "d"
                                    else -> apiQuestion.correct_answer
                                },
                                clickedAnswer = null,
                                score = apiQuestion.score ?: 10,
                                pickPath = null,
                                category = apiQuestion.category
                            )
                        }
                        startQuizWithQuestions(ArrayList(questions))
                    } else {
                        showNoQuestionsForCategory()
                    }
                }.onFailure { error ->
                    Log.e("QuestionActivity", "Failed to load questions: ${error.message}")
                    showErrorState(error.message ?: "Failed to load questions")
                }
            } catch (e: Exception) {
                Log.e("QuestionActivity", "Error loading questions", e)
                showErrorState("Error loading questions: ${e.message}")
            }
        }
    }

    private fun loadAllQuestions() {
        // Show loading state
        setContent {
            QuizAppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(id = R.color.grey)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = colorResource(id = R.color.purple),
                            modifier = Modifier.size(50.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Loading questions...",
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.navy_blue),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        lifecycleScope.launch {
            repository.getAllQuestions(limit = 10) // Limit to 10 questions
                .onSuccess { questionList ->
                    val questions = questionList.map { apiQuestion ->
                        QuestionModel(
                            id = apiQuestion._id,
                            question = apiQuestion.question,
                            answer_1 = apiQuestion.answer_1,
                            answer_2 = apiQuestion.answer_2,
                            answer_3 = apiQuestion.answer_3,
                            answer_4 = apiQuestion.answer_4,
                            correct_answer = when(apiQuestion.correct_answer) {
                                apiQuestion.answer_1 -> "a"
                                apiQuestion.answer_2 -> "b"
                                apiQuestion.answer_3 -> "c"
                                apiQuestion.answer_4 -> "d"
                                else -> apiQuestion.correct_answer
                            },
                            clickedAnswer = null,
                            score = apiQuestion.score ?: 10,
                            pickPath = null,
                            category = apiQuestion.category
                        )
                    }

                    if (questions.isNotEmpty()) {
                        startQuizWithQuestions(ArrayList(questions))
                    } else {
                        showNoQuestionsForCategory()
                    }
                }
                .onFailure { error ->
                    showErrorState(error.message ?: "Failed to load questions")
                }
        }
    }

    private fun startQuizWithQuestions(questions: ArrayList<QuestionModel>) {
        setContent {
            QuizAppTheme {
                QuestionScreen(
                    questions = questions,
                    categoryName = categoryName,  // Pass the correct category name
                    categoryId = categoryId,      // Pass the correct category ID
                    onBackClick = { finish() },
                    onFinish = { finalScore, correctAnswers, totalPossibleScore, timeSpent ->
                        Log.d("QuestionActivity", "Quiz finished:")
                        Log.d("QuestionActivity", "Final Score: $finalScore")
                        Log.d("QuestionActivity", "Correct Answers: $correctAnswers")
                        Log.d("QuestionActivity", "Total Possible Score: $totalPossibleScore")
                        Log.d("QuestionActivity", "Time Spent: $timeSpent seconds")

                        val intent = Intent(this@QuestionActivity, ScoreActivity::class.java).apply {
                            putExtra("score", finalScore)
                            putExtra("totalQuestions", questions.size)
                            putExtra("correctAnswers", correctAnswers)
                            putExtra("totalPossibleScore", totalPossibleScore)
                            putExtra("category", categoryName)  // Pass category name
                            putExtra("timeSpent", timeSpent)
                        }
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }

    private fun showNoQuestionsForCategory() {
        setContent {
            QuizAppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(id = R.color.grey)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text(
                            text = "📚",
                            fontSize = 48.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No Questions Available",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.navy_blue)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (categoryName != null)
                                "No questions found for $categoryName category"
                            else
                                "No questions available at the moment",
                            fontSize = 14.sp,
                            color = colorResource(id = R.color.navy_blue).copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { finish() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.purple)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Text(
                                text = "Go Back",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }

    private fun showErrorState(message: String) {
        setContent {
            QuizAppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(id = R.color.grey)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text(
                            text = "❌",
                            fontSize = 48.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Error Loading Questions",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.red)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = message,
                            fontSize = 14.sp,
                            color = colorResource(id = R.color.navy_blue).copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = { finish() },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.height(48.dp)
                            ) {
                                Text(
                                    text = "Go Back",
                                    fontSize = 16.sp
                                )
                            }
                            Button(
                                onClick = {
                                    // Retry loading
                                    if (categoryId != null) {
                                        loadQuestionsForCategory(categoryId!!)
                                    } else {
                                        loadAllQuestions()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(id = R.color.purple)
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.height(48.dp)
                            ) {
                                Text(
                                    text = "Retry",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}