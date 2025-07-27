//package com.example.quizapp
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import com.example.quizapp.Dashboard.screens.MainScreen
//import com.example.quizapp.ui.theme.QuizAppTheme
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////        enableEdgeToEdge()
//        setContent {
//            QuizAppTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
//                    MainScreen()
//                }
//            }
//        }
//    }
//}


package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.quizapp.Dashboard.screens.MainScreen
import com.example.quizapp.ui.theme.QuizAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
        setContent {
            QuizApp()
        }
    }
}

// ✅ Reusable Composable Function
@Composable
fun QuizApp() {
    QuizAppTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            // If MainScreen needs paddingValues, pass it
            MainScreen(
                onSinglePlayerClick = {
                    // Handle single player click
                },
                onBoardClick = {
                    // Handle board click
                }
            )
        }
    }
}

// ✅ Preview for Android Studio
@Preview()
@Composable
fun QuizAppPreview() {
    QuizApp()
}
