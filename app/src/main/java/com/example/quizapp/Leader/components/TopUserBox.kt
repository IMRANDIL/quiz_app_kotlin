package com.example.quizapp.Leader.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.quizapp.Leader.Model.UserModel
import com.example.quizapp.R


@Composable
fun TopUserBox(
    user: UserModel,
    rank: Int,
    color: String,
    sizeDp:Int,
    crown:Boolean=false
) {
    Column(
        modifier = Modifier
            .width(sizeDp.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        ConstraintLayout(
            modifier = Modifier.height(200.dp)
                .width(sizeDp.dp)
        ) {
            val (imgRef, badgeRef, cronRef) = createRefs()
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(data = getDrawableId(user.pic)

                    ).crossfade(true).build(),
                contentDescription = "User Image",
            )
        }
    }
}



@Composable
fun getDrawableId(name:String):Int{
    val context = LocalContext.current
    return context.resources.getIdentifier(name, "drawable", context.packageName)
}


@Preview
@Composable
fun TopUserBoxPreview() {
    val user = UserModel(id = 1, name = "IAA Doe", pic = "person8", score = 100)
    TopUserBox(
        user = user,
        rank = 1,
        color = "Gold",
        sizeDp = 120
    )
}