package com.example.quizapp.Dashboard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.R

@Composable
@Preview
fun GameMadeButtons(
    onCreateQuizClick: () -> Unit = {},
    onSinglePlayerClick: () -> Unit = {},
    onMultiPlayerClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .height(150.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        GameButton(
            colors = listOf(colorResource(id = R.color.blue), colorResource(id = R.color.purple)),
            iconRes = R.drawable.btn1,
            text = "Create Quiz",
            onClick = onCreateQuizClick,
            modifier = Modifier.weight(1f) // ✅ weight is here now
        )
        GameButton(
            colors = listOf(colorResource(id = R.color.purple), colorResource(id = R.color.blue)),
            iconRes = R.drawable.btn2,
            text = "Single Player",
            onClick = onSinglePlayerClick,
            modifier = Modifier.weight(1f)
        )
        GameButton(
            colors = listOf(colorResource(id = R.color.orange), Color(0xFFFFA726)),
            iconRes = R.drawable.btn3,
            text = "Multi Player",
            onClick = onMultiPlayerClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun GameButton(
    colors: List<Color>,
    iconRes: Int,
    text: String,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxHeight()
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick?.invoke() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(colors),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 10.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )

            Text(
                text = text,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}
