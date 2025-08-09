package com.example.quizapp.Dashboard.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.R

@Composable
@Preview
fun TopUserSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Profile with glowing border
        Box(
            modifier = Modifier
                .size(58.dp)
                .clip(CircleShape)
                .background(Color.Transparent)
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            colorResource(id = R.color.blue),
                            colorResource(id = R.color.purple)
                        )
                    ),
                    shape = CircleShape
                )
                .padding(3.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Profile",
                modifier = Modifier.clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Hi, Ali",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorResource(id = R.color.black)
            )
            Text(
                text = "Welcome back 👋",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }

        // Animated points card
        var isPressed by remember { mutableStateOf(false) }
        val bgColor by animateColorAsState(
            targetValue = if (isPressed) colorResource(id = R.color.blue) else colorResource(id = R.color.navy_blue),
            label = "bgColorAnim"
        )

        Row(
            modifier = Modifier
                .height(40.dp)
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            bgColor,
                            colorResource(id = R.color.purple)
                        )
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 10.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { isPressed = !isPressed },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.garnet),
                contentDescription = "Points",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "3000",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(6.dp))
            Image(
                painter = painterResource(id = R.drawable.plus),
                contentDescription = "Add Points",
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
