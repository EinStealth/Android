package com.example.hideandseek.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hideandseek.R

@Composable
fun SuccessEscapeDialogScreen(navController: NavController) {
    SuccessEscapeLayout(
        navController = navController,
    )
}

@Composable
private fun SuccessEscapeLayout(
    navController: NavController,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.text_clear),
            contentDescription = null,
            modifier = Modifier
                .width(400.dp)
                .height(352.dp)
        )
        Column(
            modifier = Modifier.padding(top = 68.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.user01_clear),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 20.dp)
                    .width(160.dp)
                    .height(160.dp)
            )
            Image(
                painter = painterResource(R.drawable.button_close),
                contentDescription = null,
                modifier = Modifier
                    .width(160.dp)
                    .height(80.dp)
                    .clickable {
                        navController.navigate("result")
                    }
            )
        }
    }
}

@Preview
@Composable
private fun SuccessEscapeDialogPreview() {
    SuccessEscapeLayout(
        navController = rememberNavController(),
    )
}
