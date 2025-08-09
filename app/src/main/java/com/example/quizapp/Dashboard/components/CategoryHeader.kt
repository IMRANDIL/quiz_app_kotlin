package com.example.quizapp.Dashboard.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.R

@Composable
fun CategoryHeader(
    onSeeAllClick: () -> Unit = {}
) {
    // Animation for "See all" button
    val infiniteTransition = rememberInfiniteTransition(label = "seeAll")
    val arrowOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "arrow"
    )

    var isPressed by remember { mutableStateOf(false) }
    val buttonScale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy
        ),
        label = "scale"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Quiz Categories",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.navy_blue)
            )
            Text(
                text = "Choose your favorite topic",
                fontSize = 14.sp,
                color = colorResource(id = R.color.navy_blue).copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        Row(
            modifier = Modifier
                .scale(buttonScale)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    colorResource(id = R.color.orange).copy(alpha = 0.1f)
                )
                .clickable {
                    isPressed = true
                    onSeeAllClick()
                }
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "See all",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(id = R.color.orange)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "→",
                fontSize = 16.sp,
                color = colorResource(id = R.color.orange),
                modifier = Modifier.offset(x = arrowOffset.dp)
            )
        }
    }

    // Reset pressed state
    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}

@Preview
@Composable
fun CategoryHeaderPreview() {
    CategoryHeader()
}