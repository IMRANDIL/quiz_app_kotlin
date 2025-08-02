package com.example.quizapp.Leader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.quizapp.Leader.Model.UserModel
import com.example.quizapp.Leader.components.OnBackRow
import com.example.quizapp.R


@Composable
fun LeaderScreen(
    topUsers: List<UserModel>,
    otherUsers: List<UserModel>,
    onBack: () -> Unit

    ) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.grey)),
        verticalArrangement = Arrangement.Top

    ) {
        item {
            OnBackRow(onBack = onBack)
        }
    }
}

@Preview
@Composable
fun LeaderScreenPreview() {
    val topUsers = listOf(
        UserModel(id = 1, name = "John Doe", pic = "person1", score = 100),
        UserModel(id = 2, name = "Jane Smith", pic = "person3", score = 90)
    )
    val otherUsers = listOf(
        UserModel(id = 3, name = "Alice Johnson", pic = "person4", score = 80),
        UserModel(id = 4, name = "Bob Williams", pic = "person5", score = 70)
    )
    LeaderScreen(
        topUsers = topUsers,
        otherUsers = otherUsers,
        onBack = {}
    )
}