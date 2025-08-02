package com.example.quizapp.Leader

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizapp.Leader.Model.UserModel
import com.example.quizapp.R

class LeaderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val topUsers = loadData().sortedByDescending { it.score }.take(3)
        val otherUsers = loadData().filterNot { topUsers.contains(it) }

    }

    private fun loadData():List<UserModel> {
        return listOf(
            UserModel(id = 1, name = "John Doe", pic = "person1", score = 1000),
            UserModel(id = 2, name = "Jane Smith", pic = "person2", score = 950),
            UserModel(id = 3, name = "Alice Johnson", pic = "person3", score = 950),
            UserModel(id = 4, name = "Bob Brown", pic = "person4", score = 845),
            UserModel(id = 5, name = "Eve White", pic = "person5", score = 807),
            UserModel(id = 6, name = "Charlie Green", pic = "person6", score = 715),
            UserModel(id = 7, name = "David Black", pic = "person7", score = 700),
            UserModel(id = 8, name = "Grace Gray", pic = "person8", score = 685),
            UserModel(id = 9, name = "Frank Red", pic = "person9", score = 604),
            UserModel(id = 10, name = "Helen Yellow", pic = "person10", score = 559),
        )
    }
}