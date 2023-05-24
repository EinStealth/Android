package com.example.hideandseek.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.hideandseek.ui.viewmodel.RegisterUserNameFragmentViewModel
import com.example.hideandseek.ui.viewmodel.RoomTypeSelectFragmentViewModel

@Composable
fun RoomTypeSelectScreen(viewModel: RoomTypeSelectFragmentViewModel = viewModel(), navController: NavController) {
    val roomTypeSelectUiState by viewModel.uiState.collectAsState()

    // 名前・アイコンの読み込み
    viewModel.readUserInfo()

    RoomTypeSelectLayout(
        navController = navController,
        userIcon = roomTypeSelectUiState.userIcon,
        userName = roomTypeSelectUiState.userName,
    )
}

@Composable
private fun RoomTypeSelectLayout(
    navController: NavController,
    userIcon: Int,
    userName: String,
) {
    Surface(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.title_background_responsive_nontitlever),
            contentDescription = "background",
            contentScale = ContentScale.Crop
        )
        Column(
            Modifier.fillMaxSize(10f)
        ) {
            Box {
                Image(
                    painter = painterResource(R.drawable.user_info_background),
                    contentDescription = "user_info_background",
                    modifier = Modifier
                        .width(300.dp)
                        .height(80.dp)
                        .clickable {
                            navController.navigate("registerIcon")
                        }
                )
                Row {
                    Image(
                        painter = when (userIcon) {
                            1 -> {
                                painterResource(R.drawable.user01_normal)
                            }
                            2 -> {
                                painterResource(R.drawable.user02_normal)
                            }
                            3 -> {
                                painterResource(R.drawable.user03_normal)
                            }
                            else -> {
                                painterResource(R.drawable.user04_normal)
                            }
                        },
                        contentDescription = "user_icon",
                        modifier = Modifier
                            .width(60.dp)
                            .height(60.dp)
                            .padding(start = 0.dp, top = 10.dp, end = 0.dp, bottom = 0.dp)
                    )
                    Text(
                        text = userName,
                        modifier = Modifier
                            .padding(start = 0.dp, top = 25.dp, end = 0.dp, bottom = 0.dp)
                            .clickable {
                                navController.navigate("registerName")
                            }
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.button_room_create),
                contentDescription = "button_room_create",
                modifier = Modifier
                    .width(198.dp)
                    .height(98.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        navController.navigate("roomCreate")
                    }
            )
            Spacer(Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.button_room_search),
                contentDescription = "button_room_search",
                modifier = Modifier
                    .width(198.dp)
                    .height(98.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        navController.navigate("roomSearch")
                    }
            )
            Spacer(Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
private fun RoomTypeSelectPreview() {
    RoomTypeSelectLayout(
        navController = rememberNavController(),
        userIcon = 1,
        userName = "なまえ",
    )
}
