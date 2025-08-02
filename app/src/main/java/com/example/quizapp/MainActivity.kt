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
import com.example.quizapp.Question.Model.QuestionModel
import com.example.quizapp.Question.QuestionActivity
import com.example.quizapp.ui.theme.QuizAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        // ✅ Add logging to confirm this is the real MainActivity
        Log.d("MainActivity", "Real MainActivity started!")

        setContent {
            QuizApp(questionList())
        }
    }

    private fun questionList(): List<QuestionModel> {
        val questions = listOf(
            QuestionModel(
                1,
                "Which planet is the largest planet in the solar system?",
                "Earth",
                "Mars",
                "Neptune",
                "Jupiter",
                "d",
                5,
                "q_1",
                null
            ),
            QuestionModel(
                2,
                "Which country is the largest country in the world by land area?",
                "Russia",
                "Canada",
                "United States",
                "China",
                "a",
                5,
                "q_2",
                null
            ),
            QuestionModel(
                3,
                "Which of the following substances is used as an anti-cancer medication?",
                "Cheese",
                "Lemon juice",
                "Cannabis",
                "Paspalum",
                "c",
                5,
                "q_3",
                null
            ),
            QuestionModel(
                4,
                "Which moon has an atmosphere?",
                "Luna",
                "Phobos",
                "Venus' moon",
                "None of the above",
                "d",
                5,
                "q_4",
                null
            ),
            QuestionModel(
                5,
                "Which symbol represents the element with atomic number 6?",
                "O",
                "H",
                "C",
                "N",
                "c",
                5,
                "q_5",
                null
            ),
            QuestionModel(
                6,
                "Who is credited with inventing theater as we know it?",
                "Shakespeare",
                "Arthur Miller",
                "Ashkouri",
                "Ancient Greeks",
                "d",
                5,
                "q_6",
                null
            ),
            QuestionModel(
                7,
                "Which ocean is the largest?",
                "Pacific",
                "Atlantic",
                "Indian",
                "Arctic",
                "a",
                5,
                "q_7",
                null
            ),
            QuestionModel(
                8,
                "Which religions are most practiced?",
                "Islam, Christianity, Judaism",
                "Buddhism, Hinduism, Sikhism",
                "Zoroastrianism, Brahmanism",
                "Taoism, Shintoism",
                "a",
                5,
                "q_8",
                null
            ),
            QuestionModel(
                9,
                "Which continent has the most independent countries?",
                "Asia",
                "Europe",
                "Africa",
                "Americas",
                "c",
                5,
                "q_9",
                null
            ),
            QuestionModel(
                10,
                "Which ocean has the greatest average depth?",
                "Pacific",
                "Atlantic",
                "Indian",
                "Southern",
                "d",
                5,
                "q_10",
                null
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
                onSinglePlayerClick = {
                    Log.d("MainActivity", "Starting QuestionActivity with ${questionList.size} questions")
                    val intent = Intent(context, QuestionActivity::class.java)
                    intent.putParcelableArrayListExtra("questions", ArrayList(questionList))
                    context.startActivity(intent)
                },
                onBoardClick = {
                    // Handle board click
                }
            )
        }
    }
}

// ✅ REMOVE OR COMMENT OUT THE PREVIEW TO AVOID CONFUSION
// @Preview()
// @Composable
// fun QuizAppPreview() {
//     // Preview code removed to avoid confusion
// }