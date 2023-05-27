package com.example.hideandseek.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hideandseek.R
import com.example.hideandseek.ui.viewmodel.RegisterUserIconFragmentViewModel

@Composable
fun RegisterUserIconScreen(viewModel: RegisterUserIconFragmentViewModel = viewModel(), navController: NavController) {
    RegisterUserIconLayout(navController = navController, writeUserIcon = { viewModel.writeUserIcon(it) })
}

@Composable
private fun RegisterUserIconLayout(
    navController: NavController,
    writeUserIcon: (Int) -> Unit,
) {
    Surface(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.title_background_responsive_nontitlever),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(R.drawable.text_null),
                contentDescription = null,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val painterList = listOf(
                    R.drawable.user01_normal,
                    R.drawable.user02_normal,
                    R.drawable.user03_normal,
                    R.drawable.user04_normal,
                )
                for (i in 0..3) {
                    Image(
                        painter = painterResource(painterList[i]),
                        contentDescription = null,
                        modifier = Modifier
                            .size(width = 92.dp, height = 92.dp)
                            .clickable {
                                // アイコンの保存
                                writeUserIcon(i + 1)
                                navController.navigate("roomTypeSelect")
                            }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun RegisterUserIconPreview() {
    RegisterUserIconLayout(navController = rememberNavController(), writeUserIcon = {})
}
