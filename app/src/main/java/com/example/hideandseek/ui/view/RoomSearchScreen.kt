package com.example.hideandseek.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hideandseek.R
import com.example.hideandseek.ui.viewmodel.RoomSearchFragmentViewModel

@Composable
fun RoomSearchScreen(viewModel: RoomSearchFragmentViewModel = viewModel(), navController: NavController) {
    // 名前・アイコンの読み込み
    viewModel.readUserInfo()

    fun onClickSearchButton(text: String) {
        // player情報のpost
        viewModel.postPlayer(text, viewModel.uiState.value.userName, viewModel.uiState.value.userIcon)
        // secret_wordsの保存
        viewModel.writeSecretWords(text)
    }

    RoomSearchLayout(
        navController = navController,
        onClickSearchButton = { onClickSearchButton(it) },
    )
}

@Composable
private fun RoomSearchLayout(
    navController: NavController,
    onClickSearchButton: (String) -> Unit,
) {
    Surface(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.title_background_responsive_nontitlever),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.secret_word),
                contentDescription = null,
                modifier = Modifier
                    .size(width = 300.dp, height = 258.dp)
            )
            var text by remember { mutableStateOf("") }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 40.dp)
            ) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    ),
                    singleLine = true,
                    placeholder = { Text(text = "\uD83D\uDDDD　合言葉") },
                )
                Image(
                    painter = painterResource(R.drawable.bt_search),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .size(width = 142.dp, height = 72.dp)
                        .clickable {
                            onClickSearchButton(text)
                            navController.navigate("standByRoom")
                        }
                )
            }
        }
    }
}

@Preview
@Composable
fun RoomSearchPreview() {
    RoomSearchLayout(
        navController = rememberNavController(),
        onClickSearchButton = {},
    )
}
