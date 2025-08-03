package com.example.quizapp.Leader.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.quizapp.Leader.Model.UserModel
import com.example.quizapp.R
import com.example.quizapp.Utils.getDrawableId


@Composable
fun LeaderRow(
    user: UserModel, rank: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(height = 70.dp)
            .background(colorResource(id = R.color.white), shape = RoundedCornerShape(size = 10.dp)).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = rank.toString(),
            modifier = Modifier.padding(start = 16.dp),
            color = colorResource(id = R.color.navy_blue)
        )
        Spacer(modifier = Modifier.width(8.dp))
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(getDrawableId(user.pic))
                .crossfade(true)
                .build(),
            contentDescription = "User Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(57.dp)
                .clip(CircleShape)
                .border(width = 2.dp, color = colorResource(id = R.color.orange), CircleShape)

        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = user.name,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.navy_blue)
        )
        Icon(
            painter = painterResource(id = R.drawable.garnet),
            contentDescription = "Score",
            tint = Color.Unspecified

        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = user.score.toString(),
            color = colorResource(id = R.color.navy_blue)
        )



    }
}




@Preview
@Composable
fun LeaderRowPreview() {
    val user = UserModel(id = 1, name = "John Doe", pic = "person1", score = 100)
    LeaderRow(user = user, rank = 1)
}

