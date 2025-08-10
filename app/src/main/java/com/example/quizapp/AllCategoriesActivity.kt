package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.quizapp.Dashboard.screens.AllCategoriesScreen
import com.example.quizapp.Question.QuestionActivity
import com.example.quizapp.network.models.Category
import com.example.quizapp.ui.theme.QuizAppTheme

class AllCategoriesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContent {
            val context = LocalContext.current

            QuizAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { paddingValues ->
                    AllCategoriesScreen(
                        onBackClick = {
                            finish() // Go back to MainActivity
                        },
                        onCategoryClick = { category ->
                            Log.d("AllCategoriesActivity", "Category clicked: ${category.name}")

                            // Start QuestionActivity with the selected category
                            // This will use the same single player quiz screen
                            val intent = Intent(context, QuestionActivity::class.java).apply {
                                // Pass the category name to filter questions
                                putExtra("category", category.name)
                                putExtra("categoryId", category.id)

                                // Optional: You can also pass the whole category object if needed
                                putExtra("categoryObject", category)
                            }
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}