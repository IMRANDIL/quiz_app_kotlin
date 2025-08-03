package com.example.quizapp.Utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun getDrawableId(name:String):Int{
    val context = LocalContext.current
    return context.resources.getIdentifier(name, "drawable", context.packageName)
}