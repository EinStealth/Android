package com.example.hideandseek.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hideandseek.R
import com.example.hideandseek.ui.viewmodel.RegisterUserIconFragmentViewModel

@Composable
fun RegisterUserIconScreen(viewModel: RegisterUserIconFragmentViewModel = viewModel(), navController: NavController) {
    Surface(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.title_background_responsive_nontitlever),
            contentDescription = "background",
            contentScale = ContentScale.Crop
        )
        ConstraintLayout {
            // Create references for the composable to constrain
            val (dialog, iconList) = createRefs()
            Image(
                painter = painterResource(R.drawable.text_null),
                contentDescription = "dialog",
                modifier = Modifier.constrainAs(dialog) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
            )
            Row(
                modifier = Modifier.constrainAs(iconList) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                    .fillMaxWidth(1f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    painter = painterResource(R.drawable.user01_normal),
                    contentDescription = "icon1",
                    modifier = Modifier.width(92.dp)
                        .height(92.dp)
                        .clickable {
                            // アイコンの保存
                            viewModel.writeUserIcon(1)
                            navController.navigate("roomTypeSelect")
                        }
                )
                Image(
                    painter = painterResource(R.drawable.user02_normal),
                    contentDescription = "icon2",
                    modifier = Modifier.width(92.dp)
                        .height(92.dp)
                        .clickable {
                            // アイコンの保存
                            viewModel.writeUserIcon(2)
                            navController.navigate("roomTypeSelect")
                        }
                )
                Image(
                    painter = painterResource(R.drawable.user03_normal),
                    contentDescription = "icon3",
                    modifier = Modifier.width(92.dp)
                        .height(92.dp)
                        .clickable {
                            // アイコンの保存
                            viewModel.writeUserIcon(3)
                            navController.navigate("roomTypeSelect")
                        }
                )
                Image(
                    painter = painterResource(R.drawable.user04_normal),
                    contentDescription = "icon4",
                    modifier = Modifier.width(92.dp)
                        .height(92.dp)
                        .clickable {
                            // アイコンの保存
                            viewModel.writeUserIcon(4)
                            navController.navigate("roomTypeSelect")
                        }
                )
            }
        }
    }
}

@Preview
@Composable
fun RegisterUserIconPreview() {
    ConstraintLayout {
        // Create references for the composable to constrain
        val (background, dialog, iconList) = createRefs()

        Image(
            painter = painterResource(R.drawable.title_background_responsive_nontitlever),
            contentDescription = "background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.constrainAs(background) {
                top.linkTo(parent.top)
            }
        )
        Image(
            painter = painterResource(R.drawable.text_null),
            contentDescription = "dialog",
            modifier = Modifier.constrainAs(dialog) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        )
        Row(
            modifier = Modifier.constrainAs(iconList) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
                .fillMaxWidth(1f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(R.drawable.user01_normal),
                contentDescription = "icon1",
                modifier = Modifier.width(92.dp)
                    .height(92.dp)
            )
            Image(
                painter = painterResource(R.drawable.user02_normal),
                contentDescription = "icon2",
                modifier = Modifier.width(92.dp)
                    .height(92.dp)
            )
            Image(
                painter = painterResource(R.drawable.user03_normal),
                contentDescription = "icon3",
                modifier = Modifier.width(92.dp)
                    .height(92.dp)
            )
            Image(
                painter = painterResource(R.drawable.user04_normal),
                contentDescription = "icon4",
                modifier = Modifier.width(92.dp)
                    .height(92.dp)
            )
        }
    }
}
