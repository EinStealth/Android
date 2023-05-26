package com.example.hideandseek.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hideandseek.R
import com.example.hideandseek.ui.viewmodel.CaptureDialogViewModel

@Composable
fun CaptureDialogScreen(viewModel: CaptureDialogViewModel = viewModel(), navController: NavController) {
    CaptureDialogLayout(
        navController = navController,
        updateStatus = { viewModel.updatePlayerStatus(1) }
    )
}

@Composable
private fun CaptureDialogLayout(
    navController: NavController,
    updateStatus: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
        ) {
            Image(
                painter = painterResource(R.drawable.text_captureboolean),
                contentDescription = null,
                modifier = Modifier
                    .width(380.dp)
                    .height(332.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.user04_oni),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .width(160.dp)
                        .height(160.dp)
                )
                Row {
                    Image(
                        painter = painterResource(R.drawable.button_yes),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 20.dp, bottom = 20.dp)
                            .width(160.dp)
                            .height(80.dp)
                            .clickable {
                                // ステータスを捕まったに変更
                                updateStatus()
                                navController.navigate("notifyCapture")
                            }
                    )
                    Image(
                        painter = painterResource(R.drawable.button_no),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 20.dp, bottom = 20.dp)
                            .width(160.dp)
                            .height(80.dp)
                            .clickable {
                                if (isOverTrapInBeTrapped) {
                                    navController.navigate("main")
                                } else {
                                    navController.navigate("beTrapped")
                                }
                            }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CaptureDialogPreview() {
    val navController = rememberNavController()
    CaptureDialogLayout(
        navController = navController,
        updateStatus = {}
    )
}
