package com.example.quizapp.Score

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.R
import kotlinx.coroutines.delay

@Composable
fun ScoreScreen(
    score: Int,
    totalQuestions: Int = 10,
    correctAnswers: Int = 0,
    totalPossibleScore: Int = 100,
    category: String? = null,
    timeSpent: Int = 0, // Time spent in seconds
    onBackToMain: () -> Unit = {}
) {
    // Animation states
    var startAnimation by remember { mutableStateOf(false) }
    val trophyScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "trophy_scale"
    )

    val trophyRotation by animateFloatAsState(
        targetValue = if (startAnimation) 0f else -180f,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "trophy_rotation"
    )

    val contentAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(800, delayMillis = 500),
        label = "content_alpha"
    )

    val buttonScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.8f,
        animationSpec = tween(600, delayMillis = 1200),
        label = "button_scale"
    )

    // Score counting animation
    val animatedScore = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(800)
        animatedScore.animateTo(
            targetValue = score.toFloat(),
            animationSpec = tween(1500, easing = FastOutSlowInEasing)
        )
    }

    // Calculate percentage based on actual score vs total possible score
    val percentage = if (totalPossibleScore > 0) {
        (score.toFloat() / totalPossibleScore.toFloat()) * 100
    } else {
        0f
    }

    // Format time display
    val minutes = timeSpent / 60
    val seconds = timeSpent % 60
    val timeDisplay = String.format("%02d:%02d", minutes, seconds)

    // Time bonus message based on time spent
    val timeBonus = when {
        timeSpent <= 120 -> "⚡ Speed Bonus!" // Under 2 minutes
        timeSpent <= 180 -> "⏱️ Good Timing!" // Under 3 minutes
        timeSpent <= 240 -> "⏰ Fair Time" // Under 4 minutes
        else -> "🐢 Completed" // Over 4 minutes
    }

    val performanceMessage = when {
        percentage >= 90 -> "🎉 EXCELLENT!"
        percentage >= 70 -> "🌟 GREAT JOB!"
        percentage >= 50 -> "👍 GOOD WORK!"
        else -> "💪 KEEP TRYING!"
    }

    val performanceColor = when {
        percentage >= 90 -> Color(0xFF4CAF50) // Green
        percentage >= 70 -> Color(0xFF2196F3) // Blue
        percentage >= 50 -> Color(0xFFFF9800) // Orange
        else -> Color(0xFFF44336) // Red
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colorResource(id = R.color.white),
                        Color(0xFFF8F9FA)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            // Performance message
            Text(
                text = performanceMessage,
                color = performanceColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .alpha(contentAlpha)
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )

            // Time bonus message
            Text(
                text = timeBonus,
                color = colorResource(id = R.color.purple),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .alpha(contentAlpha)
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            // Trophy with animations
            Box(
                contentAlignment = Alignment.Center
            ) {
                // Glow effect background
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .scale(trophyScale * 0.9f)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    performanceColor.copy(alpha = 0.2f),
                                    Color.Transparent
                                ),
                                radius = 200f
                            ),
                            shape = CircleShape
                        )
                )

                Image(
                    painter = painterResource(id = R.drawable.trophy),
                    contentDescription = "Trophy",
                    modifier = Modifier
                        .size(200.dp)
                        .scale(trophyScale)
                        .rotate(trophyRotation),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Score card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(contentAlpha),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    // Category and Time row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        if (category != null) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "📚",
                                    fontSize = 20.sp
                                )
                                Text(
                                    text = category.uppercase(),
                                    color = colorResource(id = R.color.purple),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "⏱️",
                                fontSize = 20.sp
                            )
                            Text(
                                text = timeDisplay,
                                color = colorResource(id = R.color.navy_blue),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "YOUR SCORE",
                        color = colorResource(id = R.color.navy_blue),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 2.sp
                    )

                    Text(
                        text = animatedScore.value.toInt().toString(),
                        color = performanceColor,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Text(
                        text = "out of $totalPossibleScore",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )

                    // Progress bar
                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .background(
                                Color.Gray.copy(alpha = 0.2f),
                                RoundedCornerShape(4.dp)
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(percentage / 100f)
                                .height(8.dp)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            performanceColor,
                                            performanceColor.copy(alpha = 0.8f)
                                        )
                                    ),
                                    RoundedCornerShape(4.dp)
                                )
                        )
                    }

                    Text(
                        text = "${percentage.toInt()}% Correct",
                        color = performanceColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    // Stats row
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Correct answers
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)
                                ),
                                shape = CircleShape,
                                modifier = Modifier.size(50.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$correctAnswers",
                                        color = Color(0xFF4CAF50),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Correct",
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
                        }

                        // Wrong answers
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Red.copy(alpha = 0.1f)
                                ),
                                shape = CircleShape,
                                modifier = Modifier.size(50.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${totalQuestions - correctAnswers}",
                                        color = Color.Red,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Wrong",
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
                        }

                        // Total questions
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = colorResource(id = R.color.purple).copy(alpha = 0.1f)
                                ),
                                shape = CircleShape,
                                modifier = Modifier.size(50.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$totalQuestions",
                                        color = colorResource(id = R.color.purple),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Total",
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
                        }

                        // Average time per question
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = colorResource(id = R.color.orange).copy(alpha = 0.1f)
                                ),
                                shape = CircleShape,
                                modifier = Modifier.size(50.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val avgTime = if (totalQuestions > 0) timeSpent / totalQuestions else 0
                                    Text(
                                        text = "${avgTime}s",
                                        color = colorResource(id = R.color.orange),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Avg/Q",
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action button
            Button(
                onClick = onBackToMain,
                modifier = Modifier
                    .scale(buttonScale)
                    .padding(horizontal = 32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = performanceColor
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = "Back to Main",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun ScoreScreenPreview() {
    ScoreScreen(
        score = 45,
        totalQuestions = 10,
        correctAnswers = 4,
        totalPossibleScore = 100,
        category = "Science",
        timeSpent = 187, // 3 minutes 7 seconds
        onBackToMain = {}
    )
}