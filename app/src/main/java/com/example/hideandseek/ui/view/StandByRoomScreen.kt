package com.example.hideandseek.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hideandseek.R
import com.example.hideandseek.data.datasource.remote.ResponseData
import com.example.hideandseek.ui.viewmodel.StandByRoomFragmentViewModel

private fun selectDrawable(icon: Int): Int {
    val list = listOf(R.drawable.user01_normal, R.drawable.user02_normal, R.drawable.user03_normal, R.drawable.user04_normal)
    if (icon in 1..4) {
        return list[icon - 1]
    }
    return list[0]
}

@Composable
fun StandByRoomScreen(viewModel: StandByRoomFragmentViewModel = androidx.lifecycle.viewmodel.compose.viewModel(), navController: NavController) {
    val standByRoomUiState by viewModel.uiState.collectAsState()

    // limit timeの初期化
    viewModel.resetData()

    if (standByRoomUiState.isStart == 1) {
        navController.navigate("main")
    }
    val allPlayer = standByRoomUiState.allPlayer

    StandByRoomLayout(
        navController = navController,
        secretWords = standByRoomUiState.secretWords,
        allPlayer = allPlayer,
        onClickStartButton = { viewModel.updateIsStart() },
        updateDemon = { viewModel.updateDemon(allPlayer.size - 1) }
    )
}

@Composable
private fun StandByRoomLayout(
    navController: NavController,
    secretWords: String,
    allPlayer: List<ResponseData.ResponseGetPlayer>,
    onClickStartButton: () -> Unit,
    updateDemon: () -> Unit,
) {
    Surface(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.title_background_responsive_nontitlever),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Box(
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.stand_by_room),
                contentDescription = null,
                modifier = Modifier
                    .size(width = 302.dp, height = 405.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = secretWords,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(top = 44.dp)
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .height(200.dp)
                ) {
                    items(allPlayer) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(selectDrawable(it.icon)),
                                contentDescription = null
                            )
                            Text(text = it.name)
                        }
                    }
                }
                Image(
                    painter = painterResource(R.drawable.bt_start),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .size(width = 142.dp, height = 72.dp)
                        .clickable {
                            navController.navigate("main")
                            onClickStartButton()
                            updateDemon()
                        }
                )
            }
        }
    }
}

@Preview
@Composable
private fun StandByRoomPreview() {
    StandByRoomLayout(
        navController = rememberNavController(),
        secretWords = "a",
        allPlayer = listOf(
            ResponseData.ResponseGetPlayer("", "user1", 1, 1),
            ResponseData.ResponseGetPlayer("", "user1", 1, 1),
            ResponseData.ResponseGetPlayer("", "user1", 1, 1),
            ResponseData.ResponseGetPlayer("", "user1", 1, 1),
        ),
        onClickStartButton = {},
        updateDemon = {}
    )
}
