package com.example.hideandseek.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.hideandseek.R

@Composable
fun StartScreen() {
    Column(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.title_background_responsive),
            contentDescription = "title",
            contentScale = ContentScale.Crop
        )
    }
}