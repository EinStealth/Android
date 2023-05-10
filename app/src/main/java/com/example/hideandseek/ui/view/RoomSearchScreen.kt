package com.example.hideandseek.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hideandseek.R
import com.example.hideandseek.ui.viewmodel.RoomSearchFragmentViewModel

@Composable
fun RoomSearchScreen(viewModel: RoomSearchFragmentViewModel = viewModel(), navController: NavController) {
    // 名前・アイコンの読み込み
    viewModel.readUserInfo()

    Surface(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.title_background_responsive_nontitlever),
            contentDescription = "background",
            contentScale = ContentScale.Crop
        )
        ConstraintLayout {
            // Create references for the composable to constrain
            val (dialog, btSearch, textField) = createRefs()

            Image(
                painter = painterResource(R.drawable.secret_word),
                contentDescription = "dialog",
                modifier = Modifier.constrainAs(dialog) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
                    .width(300.dp)
                    .height(258.dp)
            )
            var text by remember { mutableStateOf("") }

            TextField(
                value = text,
                onValueChange = { text = it },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                ),
                singleLine = true,
                placeholder = { Text(text = "\uD83D\uDDDD　合言葉") },
                modifier = Modifier.constrainAs(textField) {
                    top.linkTo(dialog.top)
                    end.linkTo(dialog.end)
                    bottom.linkTo(dialog.bottom)
                    start.linkTo(dialog.start)
                }
            )
            Image(
                painter = painterResource(R.drawable.bt_search),
                contentDescription = "search_button",
                modifier = Modifier
                    .constrainAs(btSearch) {
                        top.linkTo(textField.bottom)
                        end.linkTo(dialog.end)
                        bottom.linkTo(dialog.bottom)
                        start.linkTo(dialog.start)
                    }
                    .width(142.dp)
                    .height(72.dp)
                    .clickable {
                        // TODO: 部屋が存在している確認
                        // player情報のpost
                        viewModel.postPlayer(text, viewModel.uiState.value.userName, viewModel.uiState.value.userIcon)
                        // secret_wordsの保存
                        viewModel.writeSecretWords(text)
                        navController.navigate("standByRoom")
                    }
            )
        }
    }
}

@Preview
@Composable
fun RoomSearchPreview() {
    ConstraintLayout {
        // Create references for the composable to constrain
        val (background, dialog, btSearch, textField) = createRefs()

        Image(
            painter = painterResource(R.drawable.title_background_responsive_nontitlever),
            contentDescription = "background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.constrainAs(background) {
                top.linkTo(parent.top)
            }
        )
        Image(
            painter = painterResource(R.drawable.secret_word),
            contentDescription = "dialog",
            modifier = Modifier.constrainAs(dialog) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
            }
                .width(300.dp)
                .height(258.dp)
        )
        var text by remember { mutableStateOf("") }

        TextField(
            value = text,
            onValueChange = { text = it },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent
            ),
            placeholder = { Text(text = "\uD83D\uDDDD　合言葉") },
            modifier = Modifier.constrainAs(textField) {
                top.linkTo(dialog.top)
                end.linkTo(dialog.end)
                bottom.linkTo(dialog.bottom)
                start.linkTo(dialog.start)
            }
        )
        Image(
            painter = painterResource(R.drawable.bt_search),
            contentDescription = "search_button",
            modifier = Modifier
                .constrainAs(btSearch) {
                    top.linkTo(textField.bottom)
                    end.linkTo(dialog.end)
                    bottom.linkTo(dialog.bottom)
                    start.linkTo(dialog.start)
                }
                .width(142.dp)
                .height(72.dp)
        )
    }
}
