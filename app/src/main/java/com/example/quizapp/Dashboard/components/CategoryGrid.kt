package com.example.quizapp.Dashboard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.quizapp.network.models.Category
import com.example.quizapp.R

@Composable
fun CategoryGrid(categories: List<Category>, onCategoryClick: (Category) -> Unit = {}) {
    Column {
        categories.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                rowItems.forEachIndexed { index, category ->
                    CategoryCard(
                        title = category.name,
                        iconRes = category.iconRes,
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                start = if (index == 0) 24.dp else 12.dp,
                                end = if (index == 0) 12.dp else 24.dp,
                                top = 16.dp
                            ),
                        onClick = { onCategoryClick(category) } // Click listener
                    )
                }
                // Fill empty space if row has only one item
                if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun CategoryCard(
    title: String,
    iconRes: Any? = R.drawable.ic_launcher_background, // Can be Int or String
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .height(55.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = colorResource(id = R.color.white))
            .clickable { onClick() } // Make clickable
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(colorResource(id = R.color.white))
            ) {
                when (iconRes) {
                    is Int -> {
                        Image(
                            painter = painterResource(id = iconRes),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    is String -> {
                        AsyncImage(
                            model = iconRes,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.weight(1f) // Allow text to take remaining space
            )
        }
    }
}

