package com.example.quizapp.Score

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
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
    totalQuestions: Int = 10, // Default value, pass actual total
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
        delay(800) // Wait for trophy animation
        animatedScore.animateTo(
            targetValue = score.toFloat(),
            animationSpec = tween(1500, easing = FastOutSlowInEasing)
        )
    }

    // Calculate performance
    val percentage = (score.toFloat() / (totalQuestions * 5)) * 100 // Assuming 10 points per question
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
                        .size(320.dp)
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
                        .size(280.dp)
                        .scale(trophyScale)
                        .rotate(trophyRotation),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

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
                        text = "out of ${totalQuestions * 5}",
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
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Action button (centered)
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
        score = 85,
        totalQuestions = 10,
        onBackToMain = {}
    )
}