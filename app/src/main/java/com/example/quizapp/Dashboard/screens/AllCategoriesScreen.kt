package com.example.quizapp.Dashboard.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quizapp.Dashboard.components.AllCategoriesGrid
import com.example.quizapp.R
import com.example.quizapp.network.models.Category
import com.example.quizapp.network.models.CategoryViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllCategoriesScreen(
    onBackClick: () -> Unit = {},
    onCategoryClick: (Category) -> Unit = {},
    viewModel: CategoryViewModel = viewModel()
) {
    // Get categories from the ViewModel (from API/DB)
    val categories by viewModel.categories.collectAsState()
    var isLoaded by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    // Use categories from ViewModel instead of hardcoded list
    val allCategories = remember(categories) {
        if (categories.isNotEmpty()) {
            categories
        } else {
            viewModel.getAllCategories() // Fallback to default if API fails
        }
    }

    LaunchedEffect(Unit) {
        // Refresh categories from API
        viewModel.fetchCategories()
        delay(100)
        isLoaded = true
        isLoading = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colorResource(id = R.color.grey),
                        Color.White,
                        colorResource(id = R.color.grey)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Animated Top Bar
            AnimatedVisibility(
                visible = isLoaded,
                enter = slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy
                    )
                ) + fadeIn()
            ) {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                "All Categories",
                                color = colorResource(id = R.color.navy_blue),
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )
                            // Category count badge
                            Box(
                                modifier = Modifier
                                    .background(
                                        colorResource(id = R.color.orange).copy(alpha = 0.2f),
                                        CircleShape
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${allCategories.size}",
                                    color = colorResource(id = R.color.orange),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .padding(8.dp)
                                .size(40.dp)
                                .background(
                                    colorResource(id = R.color.navy_blue).copy(alpha = 0.1f),
                                    CircleShape
                                )
                        ) {
                            Text(
                                "←",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.navy_blue)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }

            // Show loading or categories
            if (isLoading && allCategories.isEmpty()) {
                // Loading state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = colorResource(id = R.color.purple),
                        modifier = Modifier.size(50.dp)
                    )
                }
            } else if (allCategories.isEmpty()) {
                // Empty state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No categories available",
                            fontSize = 18.sp,
                            color = colorResource(id = R.color.navy_blue),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Please check your internet connection",
                            fontSize = 14.sp,
                            color = colorResource(id = R.color.navy_blue).copy(alpha = 0.6f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                // Show categories grid
                AllCategoriesGrid(
                    categories = allCategories,
                    onCategoryClick = onCategoryClick
                )
            }
        }
    }
}