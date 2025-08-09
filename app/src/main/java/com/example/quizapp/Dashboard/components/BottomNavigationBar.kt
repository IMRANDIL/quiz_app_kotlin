package com.example.quizapp.Dashboard.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizapp.R


@Composable
fun BottomNavigationBar(
    selectedItemId: Int, // ✅ New param
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        Triple("Home", R.drawable.bottom_btn1, R.id.home),
        Triple("Board", R.drawable.bottom_btn2, R.id.Board),
        Triple("Favorites", R.drawable.bottom_btn3, R.id.favorites),
        Triple("Profile", R.drawable.bottom_btn4, R.id.profile)
    )

    NavigationBar(
        containerColor = colorResource(id = R.color.white),
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = selectedItemId == item.third, // ✅ highlight
                onClick = { onItemSelected(item.third) },
                icon = { Icon(painter = painterResource(id = item.second), contentDescription = item.first) },
                label = { Text(text = item.first) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colorResource(id = R.color.purple),
                    selectedTextColor = colorResource(id = R.color.purple),
                    indicatorColor = colorResource(id = R.color.black), // pill background
                    unselectedIconColor = colorResource(id = R.color.navy_blue),
                    unselectedTextColor = colorResource(id = R.color.navy_blue)
                )
            )
        }
    }
}
