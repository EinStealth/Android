package com.example.hideandseek.ui.view

import android.annotation.SuppressLint
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hideandseek.R
import com.example.hideandseek.ui.viewmodel.RegisterUserNameFragmentViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun RegisterUserNameScreen(viewModel: RegisterUserNameFragmentViewModel, navController: NavController) {
    // 既に名前が保存されているか確認する
    viewModel.readUserInfo()

    RegisterUserNameLayout(
        navController = navController,
        isEdit = viewModel.uiState.value.isEdit,
        writeUserName = { viewModel.writeUserName(it) },
    )
}

@Composable
private fun RegisterUserNameLayout(
    navController: NavController,
    isEdit: Boolean,
    writeUserName: (String) -> Unit,
) {
    Surface(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.title_background_responsive_nontitlever),
            contentDescription = "background",
            contentScale = ContentScale.Crop
        )
        ConstraintLayout {
            // Create references for the composable to constrain
            val (dialog, btDecide, textField) = createRefs()

            Image(
                painter = painterResource(R.drawable.text_null),
                contentDescription = "dialog",
                modifier = Modifier.constrainAs(dialog) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
            )
            var text by remember { mutableStateOf("") }

            TextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                ),
                placeholder = { Text(text = "名前を入力して下さい") },
                modifier = Modifier.constrainAs(textField) {
                    top.linkTo(dialog.top)
                    end.linkTo(dialog.end)
                    bottom.linkTo(dialog.bottom)
                    start.linkTo(dialog.start)
                }
            )

            Image(
                painter = painterResource(R.drawable.button_decide),
                contentDescription = "decide_button",
                modifier = Modifier
                    .constrainAs(btDecide) {
                        top.linkTo(textField.bottom)
                        end.linkTo(dialog.end)
                        bottom.linkTo(dialog.bottom)
                        start.linkTo(dialog.start)
                    }
                    .width(142.dp)
                    .height(72.dp)
                    .clickable {
                        writeUserName(text)
                        if (isEdit) {
                            navController.navigate("roomTypeSelect")
                        } else {
                            navController.navigate("registerIcon")
                        }
                    }
            )
        }
    }
}

@Preview
@Composable
private fun RegisterUserNamePreview() {
    RegisterUserNameLayout(
        navController = rememberNavController(),
        isEdit = true,
        writeUserName = {})
}
