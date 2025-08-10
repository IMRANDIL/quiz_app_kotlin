package com.example.quizapp.Dashboard.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quizapp.Dashboard.components.Banner
import com.example.quizapp.Dashboard.components.BottomNavigationBar
import com.example.quizapp.Dashboard.components.CategoryGrid
import com.example.quizapp.Dashboard.components.CategoryHeader
import com.example.quizapp.Dashboard.components.GameMadeButtons
import com.example.quizapp.Dashboard.components.TopUserSection
import com.example.quizapp.network.models.CategoryViewModel
import com.example.quizapp.R

@Composable
fun MainScreen(
    onCreateQuizClick: () -> Unit = {},
    onSinglePlayerClick: () -> Unit = {},
    onMultiPlayerClick: () -> Unit = {},
    onBoardClick: () -> Unit = {},
    onSeeAllCategoriesClick: () -> Unit = {}, // New parameter for navigation
    viewModel: CategoryViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val selectedItemId by remember { mutableStateOf(R.id.home) }
    val categories by viewModel.categories.collectAsState()
    // Assuming `categories` is your list of all categories
    val randomCategories = categories.shuffled().take(2)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.grey))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            TopUserSection()
            Spacer(modifier = Modifier.height(16.dp))
            GameMadeButtons(
                onCreateQuizClick = onCreateQuizClick,
                onSinglePlayerClick = onSinglePlayerClick,
                onMultiPlayerClick = onMultiPlayerClick
            )
            Spacer(modifier = Modifier.height(32.dp))
            CategoryHeader(
                onSeeAllClick = onSeeAllCategoriesClick // Pass the navigation callback
            )
            CategoryGrid(randomCategories)
            Banner()
        }
        BottomNavigationBar(
            selectedItemId = selectedItemId,
            modifier = Modifier.align(Alignment.BottomCenter),
            onItemSelected = { itemId ->
                if (itemId == R.id.Board) onBoardClick()
            }
        )
    }
}