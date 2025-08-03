package com.example.quizapp.Quiz

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.quizapp.R
import com.example.quizapp.ui.screens.CreateQuizScreen
import com.example.quizapp.ui.theme.QuizAppTheme

class CreateQuizActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContent {
            QuizAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { paddingValues ->
                    CreateQuizScreen(
                        onBackClick = {
                            finish() // This will close the activity and go back
                        }
                    )
                }
            }
        }
    }
}