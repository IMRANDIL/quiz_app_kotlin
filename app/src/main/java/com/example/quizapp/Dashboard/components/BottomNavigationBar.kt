package com.example.quizapp.Dashboard.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizapp.R


@Composable
@Preview
fun BottomNavigationBar(
    onItemSelected:(Int)->Unit = {},
    modifier: Modifier = Modifier
) {
    NavigationBar(containerColor = colorResource(id = R.color.white),
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
        ) {

        NavigationBarItem(
            selected = false,
            onClick = { onItemSelected(R.id.home) },
            icon = {Icon(painter = painterResource(id = R.drawable.bottom_btn1), contentDescription = null)},
            label = {Text(text = "Home")}
        )

        NavigationBarItem(
            selected = false,
            onClick = { onItemSelected(R.id.Board) },
            icon = {Icon(painter = painterResource(id = R.drawable.bottom_btn2), contentDescription = null)},
            label = {Text(text = "Board")}
        )

        NavigationBarItem(
            selected = false,
            onClick = { onItemSelected(R.id.favorites) },
            icon = {Icon(painter = painterResource(id = R.drawable.bottom_btn3), contentDescription = null)},
            label = {Text(text = "Favorites")}
        )

        NavigationBarItem(
            selected = false,
            onClick = { onItemSelected(R.id.profile) },
            icon = {Icon(painter = painterResource(id = R.drawable.bottom_btn4), contentDescription = null)},
            label = {Text(text = "Profile")}
        )

    }
}