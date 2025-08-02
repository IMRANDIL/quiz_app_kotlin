package com.example.quizapp.Question

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizapp.Question.Model.QuestionModel
import com.example.quizapp.R
import com.example.quizapp.Score.ScoreActivity

class QuestionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        val receivedList = intent.getParcelableArrayListExtra<QuestionModel>("questions")?:arrayListOf()
        Log.d("QuestionActivity", "Received ${receivedList.size} questions")
        setContent { QuestionScreen(
            questions = receivedList,
            onBackClick = {finish()},
            onFinish = {
                val intent = Intent(this, ScoreActivity::class.java)
                intent.putExtra("score", it)
                startActivity(intent)
                finish()
            }
        )}
    }
}