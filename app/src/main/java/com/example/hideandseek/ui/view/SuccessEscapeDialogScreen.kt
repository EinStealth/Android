package com.example.hideandseek.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.hideandseek.R

@Composable
fun SuccessEscapeDialogScreen(navController: NavController) {
    ConstraintLayout {
        // Create references for the composable to constrain
        val (dialog, icon, btClose) = createRefs()

        Image(
            painter = painterResource(R.drawable.text_clear),
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
            painter = painterResource(R.drawable.user01_clear),
            contentDescription = "demon",
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
            painter = painterResource(R.drawable.button_close),
            contentDescription = "button_close",
            modifier = Modifier
                .constrainAs(btClose) {
                    start.linkTo(dialog.start)
                    end.linkTo(dialog.end)
                    bottom.linkTo(dialog.bottom)
                }
                .padding(bottom = 20.dp)
                .width(160.dp)
                .height(80.dp)
                .clickable {
                    navController.navigate("result")
                }
        )
    }
}

@Preview
@Composable
fun SuccessEscapeDialogPreview() {
    ConstraintLayout {
        // Create references for the composable to constrain
        val (dialog, icon, btClose) = createRefs()

        Image(
            painter = painterResource(R.drawable.text_clear),
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
            painter = painterResource(R.drawable.user01_clear),
            contentDescription = "demon",
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
            painter = painterResource(R.drawable.button_close),
            contentDescription = "button_close",
            modifier = Modifier
                .constrainAs(btClose) {
                    start.linkTo(dialog.start)
                    end.linkTo(dialog.end)
                    bottom.linkTo(dialog.bottom)
                }
                .padding(bottom = 20.dp)
                .width(160.dp)
                .height(80.dp)
        )
    }
}