package com.example.quizapp.ui.screens

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.R
import com.example.quizapp.network.models.QuestionRequest
import com.example.quizapp.repository.QuizRepository
import kotlinx.coroutines.delay
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
    var isCreating by remember { mutableStateOf(false) } // New state for creation animation

    val repository = remember { QuizRepository() }
    val scope = rememberCoroutineScope()

    // Focus management
    val focusManager = LocalFocusManager.current
    val questionFocusRequester = remember { FocusRequester() }

    // Color scheme
    val primaryColor = colorResource(id = R.color.blue)
    val backgroundColor = colorResource(id = R.color.grey)
    val surfaceColor = Color.White
    val successColor = Color(0xFF4CAF50)
    val errorColor = Color(0xFFE53E3E)
    val warningColor = Color(0xFFFF9800)

    // Animation states
    var buttonPressed by remember { mutableStateOf(false) }
    val buttonScale by animateFloatAsState(
        targetValue = if (buttonPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "buttonScale"
    )

    // Creation animation states
    val creationRotation by animateFloatAsState(
        targetValue = if (isCreating) 360f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "creationRotation"
    )

    val creationPulse by animateFloatAsState(
        targetValue = if (isCreating) 1.1f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "creationPulse"
    )

    // Floating animation for creation state
    val floatingOffset by animateFloatAsState(
        targetValue = if (isCreating) -10f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatingOffset"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        backgroundColor.copy(alpha = 0.3f),
                        backgroundColor
                    )
                )
            )
    ) {
        // Enhanced Top Bar
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                Color.White.copy(alpha = 0.2f),
                                CircleShape
                            )
                            .then(
                                if (isCreating) Modifier
                                    .scale(creationPulse)
                                    .rotate(creationRotation)
                                else Modifier
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "✏️",
                            fontSize = 16.sp
                        )
                    }
                    Text(
                        "Create Quiz Question",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            },
            navigationIcon = {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(40.dp)
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            CircleShape
                        )
                        .clickable { onBackClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "←",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier.background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        primaryColor,
                        primaryColor.copy(alpha = 0.8f)
                    )
                )
            )
        )

        // Creation Animation Overlay
        AnimatedVisibility(
            visible = isCreating,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                primaryColor.copy(alpha = 0.3f),
                                primaryColor,
                                successColor,
                                primaryColor,
                                primaryColor.copy(alpha = 0.3f)
                            )
                        )
                    )
            )
        }

        // Content with enhanced cards
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
                .offset(y = floatingOffset.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // Question Section Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(20.dp))
                    .animateContentSize()
                    .then(
                        if (isCreating) Modifier.scale(creationPulse * 0.98f) else Modifier
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCreating)
                        surfaceColor.copy(alpha = 0.9f)
                    else surfaceColor
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    primaryColor.copy(alpha = 0.1f),
                                    CircleShape
                                )
                                .then(
                                    if (isCreating) Modifier.rotate(creationRotation * 0.5f) else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "❓",
                                fontSize = 20.sp
                            )
                        }
                        Column {
                            Text(
                                "Question",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                "Write a clear, engaging question",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    OutlinedTextField(
                        value = question,
                        onValueChange = {
                            question = it
                            errorMessage = null
                        },
                        label = { Text("Enter your question", color = primaryColor) },
                        placeholder = { Text("What would you like to ask?") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(questionFocusRequester), // Add focus requester
                        shape = RoundedCornerShape(16.dp),
                        minLines = 3,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Next
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor,
                            cursorColor = primaryColor,
                            focusedLabelColor = primaryColor
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${question.length}/500 characters",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        if (question.length >= 10) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(successColor, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "✓",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    "Good length",
                                    fontSize = 12.sp,
                                    color = successColor,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // Answer Options Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(20.dp))
                    .animateContentSize()
                    .then(
                        if (isCreating) Modifier.scale(creationPulse * 0.98f) else Modifier
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCreating)
                        surfaceColor.copy(alpha = 0.9f)
                    else surfaceColor
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    primaryColor.copy(alpha = 0.1f),
                                    CircleShape
                                )
                                .then(
                                    if (isCreating) Modifier.rotate(-creationRotation * 0.3f) else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "📝",
                                fontSize = 20.sp
                            )
                        }
                        Column {
                            Text(
                                "Answer Options",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                "Add 4 possible answers",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    // Answer fields
                    EnhancedAnswerField("A", answer1, { answer1 = it }, correctAnswer, primaryColor, successColor, isCreating)
                    EnhancedAnswerField("B", answer2, { answer2 = it }, correctAnswer, primaryColor, successColor, isCreating)
                    EnhancedAnswerField("C", answer3, { answer3 = it }, correctAnswer, primaryColor, successColor, isCreating)
                    EnhancedAnswerField("D", answer4, { answer4 = it }, correctAnswer, primaryColor, successColor, isCreating)
                }
            }

            // Correct Answer Selection Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(20.dp))
                    .animateContentSize()
                    .then(
                        if (isCreating) Modifier.scale(creationPulse * 0.98f) else Modifier
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCreating)
                        surfaceColor.copy(alpha = 0.9f)
                    else surfaceColor
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    successColor.copy(alpha = 0.1f),
                                    CircleShape
                                )
                                .then(
                                    if (isCreating) Modifier.scale(creationPulse) else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "✅",
                                fontSize = 20.sp
                            )
                        }
                        Column {
                            Text(
                                "Correct Answer",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                "Select which option is correct",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    // Correct answer selector
                    CorrectAnswerSelector(
                        options = listOf(
                            "A" to answer1,
                            "B" to answer2,
                            "C" to answer3,
                            "D" to answer4
                        ),
                        selectedAnswer = correctAnswer,
                        onAnswerSelected = {
                            correctAnswer = it
                            errorMessage = null
                        },
                        primaryColor = primaryColor,
                        successColor = successColor,
                        isCreating = isCreating
                    )
                }
            }

            // Category Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(20.dp))
                    .animateContentSize()
                    .then(
                        if (isCreating) Modifier.scale(creationPulse * 0.98f) else Modifier
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCreating)
                        surfaceColor.copy(alpha = 0.9f)
                    else surfaceColor
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    warningColor.copy(alpha = 0.1f),
                                    CircleShape
                                )
                                .then(
                                    if (isCreating) Modifier.rotate(creationRotation * 0.8f) else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "🏷️",
                                fontSize = 20.sp
                            )
                        }
                        Column {
                            Text(
                                "Category",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                "Optional - helps organize questions",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Category (Optional)", color = warningColor) },
                        placeholder = { Text("e.g., Science, History, Math...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = warningColor,
                            cursorColor = warningColor,
                            focusedLabelColor = warningColor
                        )
                    )
                }
            }

            // Error/Success Messages
            AnimatedVisibility(
                visible = errorMessage != null || successMessage != null,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                ) + fadeIn(),
                exit = slideOutVertically(
                    targetOffsetY = { -it },
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                ) + fadeOut()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (errorMessage != null)
                            errorColor.copy(alpha = 0.1f)
                        else successColor.copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    if (errorMessage != null) errorColor else successColor,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                if (errorMessage != null) "⚠️" else "✅",
                                fontSize = 16.sp
                            )
                        }
                        Text(
                            text = errorMessage ?: successMessage ?: "",
                            color = if (errorMessage != null) errorColor else successColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Enhanced Submit Button
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = if (buttonPressed) 4.dp else 12.dp,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .scale(buttonScale)
                    .then(
                        if (isCreating) Modifier.scale(creationPulse) else Modifier
                    ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Button(
                    onClick = {
                        buttonPressed = true

                        // Validate form
                        if (question.isBlank() || answer1.isBlank() || answer2.isBlank() ||
                            answer3.isBlank() || answer4.isBlank() || correctAnswer.isBlank()) {
                            errorMessage = "Please fill all required fields"
                            buttonPressed = false
                            return@Button
                        }

                        val answers = listOf(answer1, answer2, answer3, answer4)
                        if (!answers.contains(correctAnswer)) {
                            errorMessage = "Correct answer must match one of the options exactly"
                            buttonPressed = false
                            return@Button
                        }

                        // Start creation animation
                        isCreating = true
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
                                    isCreating = false
                                    buttonPressed = false

                                    // Clear form after showing success message
                                    delay(1500)
                                    question = ""
                                    answer1 = ""
                                    answer2 = ""
                                    answer3 = ""
                                    answer4 = ""
                                    correctAnswer = ""
                                    category = ""
                                    successMessage = null

                                    // Focus back to question field after clearing
                                    delay(100)
                                    questionFocusRequester.requestFocus()
                                }
                                .onFailure { error ->
                                    Log.e("CreateQuizScreen", "Failed to create question", error)
                                    errorMessage = error.message ?: "Failed to create question"
                                    isLoading = false
                                    isCreating = false
                                    buttonPressed = false
                                }
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCreating) successColor else primaryColor,
                        disabledContainerColor = primaryColor.copy(alpha = 0.6f)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    if (isLoading) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier
                                    .size(24.dp)
                                    .rotate(creationRotation),
                                strokeWidth = 3.dp
                            )
                            Text(
                                if (isCreating) "Creating Magic..." else "Creating...",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        Color.White.copy(alpha = 0.2f),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "📤",
                                    fontSize = 16.sp
                                )
                            }
                            Text(
                                "Create Question",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Add some bottom spacing
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun EnhancedAnswerField(
    option: String,
    value: String,
    onValueChange: (String) -> Unit,
    correctAnswer: String,
    primaryColor: Color,
    successColor: Color,
    isCreating: Boolean = false
) {
    val isCorrect = value == correctAnswer && value.isNotBlank()

    val fieldScale by animateFloatAsState(
        targetValue = if (isCreating && isCorrect) 1.02f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "fieldScale"
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Option $option", color = if (isCorrect) successColor else primaryColor) },
        placeholder = { Text("Enter option $option") },
        modifier = Modifier
            .fillMaxWidth()
            .scale(fieldScale)
            .then(
                if (isCorrect) Modifier.border(
                    2.dp,
                    successColor.copy(alpha = 0.5f),
                    RoundedCornerShape(16.dp)
                ) else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        leadingIcon = {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        if (isCorrect) successColor else primaryColor.copy(alpha = 0.1f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    color = if (isCorrect) Color.White else primaryColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        trailingIcon = {
            if (isCorrect) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(successColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "✓",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (isCorrect) successColor else primaryColor,
            cursorColor = primaryColor,
            focusedLabelColor = if (isCorrect) successColor else primaryColor
        )
    )
}

@Composable
fun CorrectAnswerSelector(
    options: List<Pair<String, String>>,
    selectedAnswer: String,
    onAnswerSelected: (String) -> Unit,
    primaryColor: Color,
    successColor: Color,
    isCreating: Boolean = false
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Tap to select the correct answer:",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )

        // Simple grid layout using Row and Column
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Option A
                val (optionA, textA) = options[0]
                if (textA.isNotBlank()) {
                    AnswerOptionCard(
                        optionLetter = optionA,
                        optionText = textA,
                        isSelected = textA == selectedAnswer,
                        onSelected = { onAnswerSelected(textA) },
                        primaryColor = primaryColor,
                        successColor = successColor,
                        isCreating = isCreating,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                // Option B
                val (optionB, textB) = options[1]
                if (textB.isNotBlank()) {
                    AnswerOptionCard(
                        optionLetter = optionB,
                        optionText = textB,
                        isSelected = textB == selectedAnswer,
                        onSelected = { onAnswerSelected(textB) },
                        primaryColor = primaryColor,
                        successColor = successColor,
                        isCreating = isCreating,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Option C
                val (optionC, textC) = options[2]
                if (textC.isNotBlank()) {
                    AnswerOptionCard(
                        optionLetter = optionC,
                        optionText = textC,
                        isSelected = textC == selectedAnswer,
                        onSelected = { onAnswerSelected(textC) },
                        primaryColor = primaryColor,
                        successColor = successColor,
                        isCreating = isCreating,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                // Option D
                val (optionD, textD) = options[3]
                if (textD.isNotBlank()) {
                    AnswerOptionCard(
                        optionLetter = optionD,
                        optionText = textD,
                        isSelected = textD == selectedAnswer,
                        onSelected = { onAnswerSelected(textD) },
                        primaryColor = primaryColor,
                        successColor = successColor,
                        isCreating = isCreating,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun AnswerOptionCard(
    optionLetter: String,
    optionText: String,
    isSelected: Boolean,
    onSelected: () -> Unit,
    primaryColor: Color,
    successColor: Color,
    isCreating: Boolean = false,
    modifier: Modifier = Modifier
) {
    val cardScale by animateFloatAsState(
        targetValue = if (isCreating && isSelected) 1.05f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cardScale"
    )

    Card(
        modifier = modifier
            .height(80.dp)
            .clickable { onSelected() }
            .animateContentSize()
            .scale(cardScale),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                successColor.copy(alpha = if (isCreating) 0.2f else 0.1f)
            else Color.Gray.copy(alpha = 0.05f)
        ),
        border = if (isSelected)
            androidx.compose.foundation.BorderStroke(
                width = if (isCreating) 3.dp else 2.dp,
                color = successColor
            )
        else androidx.compose.foundation.BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        if (isSelected) successColor else primaryColor.copy(alpha = 0.7f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = optionLetter,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = optionText.take(20) + if (optionText.length > 20) "..." else "",
                fontSize = 11.sp,
                color = if (isSelected) successColor else Color.Black,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

@Preview
@Composable
fun CreateQuizScreenPreview() {
    CreateQuizScreen(onBackClick = {})
}