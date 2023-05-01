package com.example.hideandseek.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hideandseek.R
import com.example.hideandseek.ui.viewmodel.CaptureDialogViewModel

@Composable
fun CaptureDialogScreen(viewModel: CaptureDialogViewModel = viewModel(), navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ConstraintLayout (
            modifier = Modifier.align(Alignment.Center)
        ) {
            // Create references for the composable to constrain
            val (dialog, icon, btYes, btNo) = createRefs()

            Image(
                painter = painterResource(R.drawable.text_captureboolean),
                contentDescription = "dialog",
                modifier = Modifier
                    .constrainAs(dialog) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
                    .width(380.dp)
                    .height(332.dp)
            )
            Image(
                painter = painterResource(R.drawable.user04_oni),
                contentDescription = "icon",
                modifier = Modifier
                    .constrainAs(icon) {
                        top.linkTo(dialog.top)
                        end.linkTo(dialog.end)
                        bottom.linkTo(dialog.bottom)
                        start.linkTo(dialog.start)
                    }
                    .padding(end = 20.dp)
                    .width(160.dp)
                    .height(160.dp)
            )
            Image(
                painter = painterResource(R.drawable.button_yes),
                contentDescription = "button_yes",
                modifier = Modifier
                    .constrainAs(btYes) {
                        end.linkTo(dialog.end)
                        bottom.linkTo(dialog.bottom)
                    }
                    .padding(end = 20.dp, bottom = 20.dp)
                    .width(160.dp)
                    .height(80.dp)
                    .clickable {
                        // ステータスを捕まったに変更
                        viewModel.updatePlayerStatus(3)
                        navController.navigate("notifyCapture")
                    }
            )
            Image(
                painter = painterResource(R.drawable.button_no),
                contentDescription = "button_no",
                modifier = Modifier
                    .constrainAs(btNo) {
                        bottom.linkTo(dialog.bottom)
                        start.linkTo(dialog.start)
                    }
                    .padding(start = 20.dp, bottom = 20.dp)
                    .width(160.dp)
                    .height(80.dp)
                    .clickable {
                        navController.navigate("main")
                    }
            )
        }
    }
}

@Preview
@Composable
fun CaptureDialogPreview() {
    ConstraintLayout {
        // Create references for the composable to constrain
        val (dialog, icon, btYes, btNo) = createRefs()

        Image(
            painter = painterResource(R.drawable.text_captureboolean),
            contentDescription = "dialog",
            modifier = Modifier
                .constrainAs(dialog) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
                .width(400.dp)
                .height(352.dp)
        )
        Image(
            painter = painterResource(R.drawable.user04_oni),
            contentDescription = "icon",
            modifier = Modifier
                .constrainAs(icon) {
                    top.linkTo(dialog.top)
                    end.linkTo(dialog.end)
                    bottom.linkTo(dialog.bottom)
                    start.linkTo(dialog.start)
                }
                .width(160.dp)
                .height(160.dp)
                .padding(end = 20.dp)
        )
        Image(
            painter = painterResource(R.drawable.button_yes),
            contentDescription = "button_yes",
            modifier = Modifier
                .constrainAs(btYes) {
                    end.linkTo(dialog.end)
                    bottom.linkTo(dialog.bottom)
                }
                .width(160.dp)
                .height(80.dp)
                .padding(end = 20.dp, bottom = 20.dp)
        )
        Image(
            painter = painterResource(R.drawable.button_no),
            contentDescription = "button_no",
            modifier = Modifier
                .constrainAs(btNo) {
                    bottom.linkTo(dialog.bottom)
                    start.linkTo(dialog.start)
                }
                .width(160.dp)
                .height(80.dp)
                .padding(start = 20.dp, bottom = 20.dp)
        )
    }
}