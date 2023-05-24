package com.example.hideandseek.ui.view

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hideandseek.R

@Composable
fun StartScreen(navController: NavController) {
    val activity = LocalContext.current as Activity

    // 名前の読み込み
    val sharedPref = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE)
    val name = sharedPref?.getString("name", "")

    StartLayout(
        name = name.toString(),
        navController = navController,
    )
}

@Composable
private fun StartLayout(
    name: String,
    navController: NavController
) {
    Surface(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.title_background_responsive),
            contentDescription = "title",
            contentScale = ContentScale.Crop,
            modifier = Modifier.clickable {
                if (name != "") {
                    navController.navigate("roomTypeSelect")
                } else {
                    navController.navigate("registerName")
                }
            }
        )
    }
}

@Preview
@Composable
private fun StartPreview() {
    StartLayout(
        name = "atomic",
        navController = rememberNavController(),
    )
}
