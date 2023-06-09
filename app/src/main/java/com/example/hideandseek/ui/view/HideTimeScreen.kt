package com.example.hideandseek.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hideandseek.R
import com.example.hideandseek.ui.viewmodel.HideTimeViewModel

private fun selectDrawable(icon: Int): Int {
    val list = listOf(R.drawable.user01_oni, R.drawable.user02_oni, R.drawable.user03_oni, R.drawable.user04_oni)
    if (icon in 1..4) {
        return list[icon - 1]
    }
    return list[0]
}

@Composable
fun HideTimeScreen(
    viewModel: HideTimeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navController: NavController,
) {
    val hideTimeUiState by viewModel.uiState.collectAsState()
    val allPlayer = hideTimeUiState.allPlayer
    val demon = hideTimeUiState.demon

    if (allPlayer.isNotEmpty()) {
        HideTimeLayout(
            demonName = allPlayer[demon].name,
            demonIcon = allPlayer[demon].icon,
            navController = navController,
        )
    }
}

@Composable
private fun HideTimeLayout(
    demonName: String,
    demonIcon: Int,
    navController: NavController,
) {
    Surface(
        color = Color.Black,
        modifier = Modifier
            .fillMaxSize()
            .clickable { navController.navigate("main") }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "鬼は $demonName です",
                fontSize = 24.sp,
                color = Color.White,
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = selectDrawable(demonIcon)),
                contentDescription = null,
                modifier = Modifier
                    .size(width = 144.dp, height = 144.dp)
            )
            Spacer(modifier = Modifier.weight(2f))
        }
    }
}

@Preview
@Composable
private fun HideTimePreview() {
    HideTimeLayout(
        demonName = "name",
        demonIcon = 1,
        navController = rememberNavController(),
    )
}