package com.example.hideandseek.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hideandseek.R

@Composable
fun ResultScreen() {
    ResultLayout()
}

@Composable
private fun ResultLayout() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.text_null),
            contentDescription = null,
            modifier = Modifier
                .scale(scaleX = 1f, scaleY = 2f)
                .fillMaxHeight()
                .padding(10.dp)
        )
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "RESULT",
                fontSize = 32.sp,
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = "WINNER",
                fontSize = 32.sp,
                modifier = Modifier
                    .padding(start = 32.dp)
            )
            Row {
                Image(
                    painter = painterResource(R.drawable.user01_normal),
                    contentDescription = null,
                    modifier = Modifier
                        .size(width = 120.dp, height = 120.dp)
                )
                Image(
                    painter = painterResource(R.drawable.user02_normal),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .size(width = 120.dp, height = 120.dp)
                )
            }
            Text(
                text = "LOSER",
                fontSize = 32.sp,
                modifier = Modifier
                    .padding(start = 32.dp)
            )
            Row {
                Image(
                    painter = painterResource(R.drawable.user03_normal),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .size(width = 120.dp, height = 120.dp)
                )
                Image(
                    painter = painterResource(R.drawable.user02_normal),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 24.dp, start = 12.dp)
                        .size(width = 120.dp, height = 120.dp)
                )
            }
            Image(
                painter = painterResource(R.drawable.button_close),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 52.dp)
                    .size(width = 160.dp, height = 80.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ResultPreview() {
    ResultLayout()
}
