package com.example.quizapp.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.R
import com.example.quizapp.network.models.QuestionRequest
import com.example.quizapp.repository.QuizRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuizScreen(onBackClick: () -> Unit) {

    // State for form fields
    var question by remember { mutableStateOf("") }
    var answer1 by remember { mutableStateOf("") }
    var answer2 by remember { mutableStateOf("") }
    var answer3 by remember { mutableStateOf("") }
    var answer4 by remember { mutableStateOf("") }
    var correctAnswer by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    // State for UI
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    val repository = remember { QuizRepository() }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.grey))
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Text(
                    "Create Quiz Question",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                TextButton(onClick = onBackClick) {
                    Text("← Back", color = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(id = R.color.blue)
            )
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Question Input
            OutlinedTextField(
                value = question,
                onValueChange = {
                    question = it
                    errorMessage = null
                },
                label = { Text("Question") },
                placeholder = { Text("Enter your question here...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Answer Options
            Text(
                text = "Answer Options",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            OutlinedTextField(
                value = answer1,
                onValueChange = { answer1 = it },
                label = { Text("Option A") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = answer2,
                onValueChange = { answer2 = it },
                label = { Text("Option B") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = answer3,
                onValueChange = { answer3 = it },
                label = { Text("Option C") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = answer4,
                onValueChange = { answer4 = it },
                label = { Text("Option D") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Correct Answer
            OutlinedTextField(
                value = correctAnswer,
                onValueChange = { correctAnswer = it },
                label = { Text("Correct Answer") },
                placeholder = { Text("Enter the correct answer exactly as above") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Category (Optional)
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category (Optional)") },
                placeholder = { Text("e.g., Science, History, Math...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Error Message
            if (errorMessage != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f))
                ) {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Success Message
            if (successMessage != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.Green.copy(alpha = 0.1f))
                ) {
                    Text(
                        text = successMessage!!,
                        color = Color.Green,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Create Button
            Button(
                onClick = {
                    // Validate form
                    if (question.isBlank() || answer1.isBlank() || answer2.isBlank() ||
                        answer3.isBlank() || answer4.isBlank() || correctAnswer.isBlank()) {
                        errorMessage = "Please fill all required fields"
                        return@Button
                    }

                    val answers = listOf(answer1, answer2, answer3, answer4)
                    if (!answers.contains(correctAnswer)) {
                        errorMessage = "Correct answer must match one of the options exactly"
                        return@Button
                    }

                    // Create question
                    isLoading = true
                    errorMessage = null
                    successMessage = null

                    scope.launch {
                        val questionRequest = QuestionRequest(
                            question = question,
                            answer_1 = answer1,
                            answer_2 = answer2,
                            answer_3 = answer3,
                            answer_4 = answer4,
                            correct_answer = correctAnswer,
                            score = 10,
                            category = category.ifBlank { null }
                        )

                        Log.d("CreateQuizScreen", "Creating question: $questionRequest")

                        repository.createQuestion(questionRequest)
                            .onSuccess {
                                Log.d("CreateQuizScreen", "Question created successfully!")
                                successMessage = "Question created successfully!"
                                isLoading = false

                                // Clear form after 2 seconds
                                kotlinx.coroutines.delay(2000)
                                question = ""
                                answer1 = ""
                                answer2 = ""
                                answer3 = ""
                                answer4 = ""
                                correctAnswer = ""
                                category = ""
                                successMessage = null
                            }
                            .onFailure { error ->
                                Log.e("CreateQuizScreen", "Failed to create question", error)
                                errorMessage = error.message ?: "Failed to create question"
                                isLoading = false
                            }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.blue)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        "Create Question",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CreateQuizScreenPreview() {
    CreateQuizScreen(onBackClick = {})
}
