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
import com.example.hideandseek.R

@Composable
fun StartScreen() {
    val activity = LocalContext.current as Activity

    // 名前の読み込み
    val sharedPref = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE)
    val name = sharedPref?.getString("name", "")

    Surface(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.title_background_responsive),
            contentDescription = "title",
            contentScale = ContentScale.Crop,
            modifier = Modifier.clickable {
//                if (name != "") {
//                    onNavigate(R.id.navigation_room_type_select)
//                } else {
//                    onNavigate(R.id.navigation_register_user_name)
//                }
            }
        )
    }
}

@Composable
@Preview
fun StartPreview() {
    Surface(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.title_background_responsive),
            contentDescription = "title",
            contentScale = ContentScale.Crop
        )
    }
}
