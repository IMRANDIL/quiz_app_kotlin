package com.example.quizapp.Dashboard.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.quizapp.R
import com.example.quizapp.network.models.Category
import kotlinx.coroutines.delay

@Composable
fun AllCategoriesGrid(
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit
) {
    // Define colors for categories
    val categoryColors = remember {
        listOf(
            Color(0xFF45beb9), // Teal
            Color(0xFFf87441), // Orange
            Color(0xFF8c72ed), // Purple
            Color(0xFFf55150), // Red
            Color(0xFF34d287), // Green
            Color(0xFFFFD700), // Gold
            Color(0xFF4169E1), // Royal Blue
            Color(0xFFFF69B4), // Hot Pink
            Color(0xFF9370DB), // Medium Purple
            Color(0xFF20B2AA), // Light Sea Green
            Color(0xFFFF6347), // Tomato
            Color(0xFF48D1CC)  // Medium Turquoise
        )
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(
            items = categories,
            key = { _, category -> category.id }
        ) { index, category ->
            CategoryGridCard(
                category = category,
                backgroundColor = categoryColors[index % categoryColors.size],
                animationDelay = index * 50L,
                onClick = { onCategoryClick(category) }
            )
        }
    }
}

@Composable
fun CategoryGridCard(
    category: Category,
    backgroundColor: Color,
    animationDelay: Long,
    onClick: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    // Animations
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )

    val rotation by animateFloatAsState(
        targetValue = if (isPressed) 3f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy
        ),
        label = "rotation"
    )

    // Floating animation
    val infiniteTransition = rememberInfiniteTransition(label = "float")
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    LaunchedEffect(Unit) {
        delay(animationDelay)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(
            initialScale = 0.8f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + fadeIn(animationSpec = tween(300))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .scale(scale)
                .rotate(rotation)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    isPressed = true
                    onClick()
                },
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = if (isPressed) 4.dp else 8.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                // Gradient background overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    backgroundColor.copy(alpha = 0.3f),
                                    backgroundColor.copy(alpha = 0.1f)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Animated icon container
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .offset(y = floatOffset.dp)
                            .shadow(
                                elevation = 8.dp,
                                shape = CircleShape,
                                clip = false,
                                spotColor = backgroundColor.copy(alpha = 0.3f)
                            )
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        backgroundColor.copy(alpha = 0.2f),
                                        backgroundColor.copy(alpha = 0.05f)
                                    )
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        when (category.iconRes) {
                            is Int -> {
                                Image(
                                    painter = painterResource(id = category.iconRes),
                                    contentDescription = null,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            is String -> {
                                AsyncImage(
                                    model = category.iconRes,
                                    contentDescription = null,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Category name
                    Text(
                        text = category.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF352e4f),
                        textAlign = TextAlign.Center
                    )

                    // Question count (optional)
                    Text(
                        text = "${(20..50).random()} Questions",
                        fontSize = 12.sp,
                        color = backgroundColor,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Shimmer effect
                this@Card.AnimatedVisibility(
                    visible = isPressed,
                    enter = fadeIn(animationSpec = tween(200)),
                    exit = fadeOut(animationSpec = tween(200))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                backgroundColor.copy(alpha = 0.1f),
                                RoundedCornerShape(20.dp)
                            )
                    )
                }
            }
        }

        // Reset pressed state after animation
        LaunchedEffect(isPressed) {
            if (isPressed) {
                delay(200)
                isPressed = false
            }
        }
    }
}