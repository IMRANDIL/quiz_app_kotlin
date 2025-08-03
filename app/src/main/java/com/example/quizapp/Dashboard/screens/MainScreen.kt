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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizapp.Dashboard.components.Banner
import com.example.quizapp.Dashboard.components.BottomNavigationBar
import com.example.quizapp.Dashboard.components.CategoryGrid
import com.example.quizapp.Dashboard.components.CategoryHeader
import com.example.quizapp.Dashboard.components.GameMadeButtons
import com.example.quizapp.Dashboard.components.TopUserSection
import com.example.quizapp.R

@Composable
@Preview
fun MainScreen(
    onCreateQuizClick: () -> Unit = {},
    onSinglePlayerClick: () -> Unit = {},
    onMultiPlayerClick: () -> Unit = {},
    onBoardClick: () -> Unit = {},
){
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.grey))
    ) {
        Column(
            Modifier.fillMaxSize()
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
            CategoryHeader()
            CategoryGrid()
            Banner()
        }
        BottomNavigationBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            onItemSelected = { itemId ->
                if(itemId == R.id.Board) onBoardClick()
            }
        )
    }
}