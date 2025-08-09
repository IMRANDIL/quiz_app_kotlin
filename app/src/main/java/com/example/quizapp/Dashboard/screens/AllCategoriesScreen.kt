package com.example.quizapp.Dashboard.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
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
    val categories by viewModel.categories.collectAsState()
    var isLoaded by remember { mutableStateOf(false) }

    // Extended categories list
    val allCategories = remember {
        listOf(
            Category("Science", R.drawable.cat1),
            Category("History", R.drawable.cat2),
            Category("Sport", R.drawable.cat3),
            Category("Art", R.drawable.cat4),
            Category("Geography", R.drawable.ic_launcher_background),
            Category("Music", R.drawable.ic_launcher_background),
            Category("Technology", R.drawable.ic_launcher_background),
            Category("Movies", R.drawable.ic_launcher_background),
            Category("Literature", R.drawable.ic_launcher_background),
            Category("Mathematics", R.drawable.ic_launcher_background),
            Category("Biology", R.drawable.ic_launcher_background),
            Category("Chemistry", R.drawable.ic_launcher_background)
        )
    }

    LaunchedEffect(Unit) {
        delay(100)
        isLoaded = true
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

            // Use the AllCategoriesGrid component
            AllCategoriesGrid(
                categories = allCategories,
                onCategoryClick = onCategoryClick
            )
        }
    }
}