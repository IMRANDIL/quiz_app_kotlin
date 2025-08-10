package com.example.quizapp.Score

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.quizapp.MainActivity
import com.example.quizapp.R

class ScoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val score = intent.getIntExtra("score", 0)
        val totalQuestions = intent.getIntExtra("totalQuestions", 10)
        val correctAnswers = intent.getIntExtra("correctAnswers", 0)
        val totalPossibleScore = intent.getIntExtra("totalPossibleScore", 100)
        val category = intent.getStringExtra("category")

        Log.d("ScoreActivity", "Received:")
        Log.d("ScoreActivity", "Score: $score")
        Log.d("ScoreActivity", "Total Questions: $totalQuestions")
        Log.d("ScoreActivity", "Correct Answers: $correctAnswers")
        Log.d("ScoreActivity", "Total Possible Score: $totalPossibleScore")
        Log.d("ScoreActivity", "Category: $category")

        setContent {
            ScoreScreen(
                score = score,
                totalQuestions = totalQuestions,
                correctAnswers = correctAnswers,
                totalPossibleScore = totalPossibleScore,
                category = category
            ) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}