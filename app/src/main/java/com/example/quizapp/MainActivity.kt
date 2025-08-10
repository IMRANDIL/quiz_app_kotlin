package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.quizapp.Dashboard.screens.MainScreen
import com.example.quizapp.Leader.LeaderActivity
import com.example.quizapp.Question.Model.QuestionModel
import com.example.quizapp.Question.QuestionActivity
import com.example.quizapp.Quiz.CreateQuizActivity
import com.example.quizapp.network.models.Category
import com.example.quizapp.ui.theme.QuizAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        Log.d("MainActivity", "Real MainActivity started!")

        setContent {
            QuizApp(questionList())
        }
    }

    private fun questionList(): List<QuestionModel> {
        val questions = listOf(
            QuestionModel(
                id = 1.toString(),
                question = "Which planet is the largest planet in the solar system?",
                answer_1 = "Earth",
                answer_2 = "Mars",
                answer_3 = "Neptune",
                answer_4 = "Jupiter",
                correct_answer = "d",
                clickedAnswer = null,
                score = 5,
                pickPath = "q_1",
                category = Category(
                    id = "science_id", // Provide an appropriate ID
                    name = "Science",
                    description = "Test your knowledge of science",
                    iconRes = null
                )
            ),
            QuestionModel(
                id = 2.toString(),
                question = "Which country is the largest country in the world by land area?",
                answer_1 = "Russia",
                answer_2 = "Canada",
                answer_3 = "United States",
                answer_4 = "China",
                correct_answer = "a",
                clickedAnswer = null,
                score = 5,
                pickPath = "q_2",
                category = Category(
                    id = "geography_id",
                    name = "Geography",
                    description = "Test your knowledge of geography",
                    iconRes = null
                )
            ),
            QuestionModel(
                id = 3.toString(),
                question = "Which of the following substances is used as an anti-cancer medication?",
                answer_1 = "Cheese",
                answer_2 = "Lemon juice",
                answer_3 = "Cannabis",
                answer_4 = "Paspalum",
                correct_answer = "c",
                clickedAnswer = null,
                score = 5,
                pickPath = "q_3",
                category = Category(
                    id = "science_id",
                    name = "Science",
                    description = "Test your knowledge of science",
                    iconRes = null
                )
            ),
            QuestionModel(
                id = 4.toString(),
                question = "Which moon has an atmosphere?",
                answer_1 = "Luna",
                answer_2 = "Phobos",
                answer_3 = "Venus' moon",
                answer_4 = "None of the above",
                correct_answer = "d",
                clickedAnswer = null,
                score = 5,
                pickPath = "q_4",
                category = Category(
                    id = "science_id",
                    name = "Science",
                    description = "Test your knowledge of science",
                    iconRes = null
                )
            ),
            QuestionModel(
                id = 5.toString(),
                question = "Which symbol represents the element with atomic number 6?",
                answer_1 = "O",
                answer_2 = "H",
                answer_3 = "C",
                answer_4 = "N",
                correct_answer = "c",
                clickedAnswer = null,
                score = 5,
                pickPath = "q_5",
                category = Category(
                    id = "science_id",
                    name = "Science",
                    description = "Test your knowledge of science",
                    iconRes = null
                )
            ),
            QuestionModel(
                id = 6.toString(),
                question = "Who is credited with inventing theater as we know it?",
                answer_1 = "Shakespeare",
                answer_2 = "Arthur Miller",
                answer_3 = "Ashkouri",
                answer_4 = "Ancient Greeks",
                correct_answer = "d",
                clickedAnswer = null,
                score = 5,
                pickPath = "q_6",
                category = Category(
                    id = "history_id",
                    name = "History",
                    description = "Test your knowledge of history",
                    iconRes = null
                )
            ),
            QuestionModel(
                id = 7.toString(),
                question = "Which ocean is the largest?",
                answer_1 = "Pacific",
                answer_2 = "Atlantic",
                answer_3 = "Indian",
                answer_4 = "Arctic",
                correct_answer = "a",
                clickedAnswer = null,
                score = 5,
                pickPath = "q_7",
                category = Category(
                    id = "geography_id",
                    name = "Geography",
                    description = "Test your knowledge of geography",
                    iconRes = null
                )
            ),
            QuestionModel(
                id = 8.toString(),
                question = "Which religions are most practiced?",
                answer_1 = "Islam, Christianity, Judaism",
                answer_2 = "Buddhism, Hinduism, Sikhism",
                answer_3 = "Zoroastrianism, Brahmanism",
                answer_4 = "Taoism, Shintoism",
                correct_answer = "a",
                clickedAnswer = null,
                score = 5,
                pickPath = "q_8",
                category = Category(
                    id = "history_id",
                    name = "History",
                    description = "Test your knowledge of history",
                    iconRes = null
                )
            ),
            QuestionModel(
                id = 9.toString(),
                question = "Which continent has the most independent countries?",
                answer_1 = "Asia",
                answer_2 = "Europe",
                answer_3 = "Africa",
                answer_4 = "Americas",
                correct_answer = "c",
                clickedAnswer = null,
                score = 5,
                pickPath = "q_9",
                category = Category(
                    id = "geography_id",
                    name = "Geography",
                    description = "Test your knowledge of geography",
                    iconRes = null
                )
            ),
            QuestionModel(
                id = 10.toString(),
                question = "Which ocean has the greatest average depth?",
                answer_1 = "Pacific",
                answer_2 = "Atlantic",
                answer_3 = "Indian",
                answer_4 = "Southern",
                correct_answer = "d",
                clickedAnswer = null,
                score = 5,
                pickPath = "q_10",
                category = Category(
                    id = "geography_id",
                    name = "Geography",
                    description = "Test your knowledge of geography",
                    iconRes = null
                )
            )
        )


        Log.d("MainActivity", "Created ${questions.size} questions")
        return questions
    }
}

@Composable
fun QuizApp(questionList: List<QuestionModel>) {
    val context = LocalContext.current
    QuizAppTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            MainScreen(
                onCreateQuizClick = {
                    Log.d("MainActivity", "Starting CreateQuizActivity")
                    val intent = Intent(context, CreateQuizActivity::class.java)
                    context.startActivity(intent)
                },
                onSinglePlayerClick = {
                    Log.d("MainActivity", "Starting QuestionActivity with ${questionList.size} questions")
                    val intent = Intent(context, QuestionActivity::class.java)
                    intent.putParcelableArrayListExtra("questions", ArrayList(questionList))
                    context.startActivity(intent)
                },
                onMultiPlayerClick = {
                    // Handle multiplayer click - you can implement this later
                    Log.d("MainActivity", "Multiplayer clicked - not implemented yet")
                },
                onBoardClick = {
                    val intent = Intent(context, LeaderActivity::class.java)
                    context.startActivity(intent)
                },
                onSeeAllCategoriesClick = {
                    Log.d("MainActivity", "Starting AllCategoriesActivity")
                    val intent = Intent(context, AllCategoriesActivity::class.java)
                    context.startActivity(intent)
                }
            )
        }
    }
}