package com.example.hideandseek.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hideandseek.R

@Composable
fun NotifyCaptureDialogScreen(navController: NavController) {
    NotifyCaptureDialogLayout(
        navController = navController,
    )
}

@Composable
private fun NotifyCaptureDialogLayout(
    navController: NavController,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
        ) {
            Image(
                painter = painterResource(R.drawable.text_captured),
                contentDescription = "dialog",
                modifier = Modifier
                    .width(400.dp)
                    .height(352.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(R.drawable.user04_oni),
                        contentDescription = "demon",
                        modifier = Modifier
                            .width(160.dp)
                            .height(160.dp)
                    )
                    Box() {
                        Image(
                            painter = painterResource(R.drawable.user01_caputure),
                            contentDescription = "normal",
                            modifier = Modifier
                                .width(160.dp)
                                .height(160.dp)
                        )
                        Image(
                            painter = painterResource(R.drawable.kanabou),
                            contentDescription = "metal_rod",
                            modifier = Modifier
                                .width(120.dp)
                                .height(120.dp)
                        )
                    }
                }
                Image(
                    painter = painterResource(R.drawable.button_close),
                    contentDescription = "button_close",
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .width(160.dp)
                        .height(80.dp)
                        .clickable {
                            navController.navigate("watch")
                        }
                )
            }
        }
    }
}

@Preview
@Composable
private fun NotifyCaptureDialogPreview() {
    NotifyCaptureDialogLayout(
        navController = rememberNavController(),
    )
}
