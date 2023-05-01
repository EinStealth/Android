package com.example.hideandseek.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.hideandseek.R
import com.example.hideandseek.ui.viewmodel.StandByRoomFragmentViewModel

private fun selectDrawable(icon: Int): Int {
    return when (icon) {
        1 -> {
            R.drawable.user01_normal
        }
        2 -> {
            R.drawable.user02_normal
        }
        3 -> {
            R.drawable.user03_normal
        }
        else -> {
            R.drawable.user04_normal
        }
    }
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
    Surface(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.title_background_responsive_nontitlever),
            contentDescription = "background",
            contentScale = ContentScale.Crop
        )
        ConstraintLayout {
            // Create references for the composable to constrain
            val (dialog, textSecret, icon1, icon2, icon3, icon4, name1, name2, name3, name4, btStart) = createRefs()
            Image(
                painter = painterResource(R.drawable.stand_by_room),
                contentDescription = "dialog",
                modifier = Modifier
                    .constrainAs(dialog) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
                    .width(302.dp)
                    .height(405.dp)
            )
            Text(
                text = standByRoomUiState.secretWords,
                fontSize = 24.sp,
                modifier = Modifier
                    .constrainAs(textSecret) {
                        top.linkTo(dialog.top)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
                    .padding(top = 44.dp)
            )
            if (standByRoomUiState.allPlayer.isNotEmpty()) {
                Image(
                    painter = painterResource(selectDrawable(allPlayer[0].icon)),
                    contentDescription = "icon1",
                    modifier = Modifier
                        .constrainAs(icon1) {
                            top.linkTo(dialog.top)
                            start.linkTo(dialog.start)
                        }
                        .padding(top = 80.dp, start = 12.dp)
                        .height(100.dp)
                        .width(100.dp)
                )
                Text(
                    text = allPlayer[0].name,
                    modifier = Modifier
                        .constrainAs(name1) {
                            top.linkTo(icon1.bottom)
                            start.linkTo(icon1.start)
                            end.linkTo(icon1.end)
                        }
                )
            }
            if (standByRoomUiState.allPlayer.size > 1) {
                Image(
                    painter = painterResource(selectDrawable(allPlayer[1].icon)),
                    contentDescription = "icon2",
                    modifier = Modifier
                        .constrainAs(icon2) {
                            top.linkTo(dialog.top)
                            start.linkTo(icon1.end)
                        }
                        .padding(top = 80.dp, start = 12.dp)
                        .height(100.dp)
                        .width(100.dp)
                )
                Text(
                    text = allPlayer[1].name,
                    modifier = Modifier
                        .constrainAs(name2) {
                            top.linkTo(icon1.bottom)
                            start.linkTo(icon2.start)
                            end.linkTo(icon2.end)
                        }
                )
            }
            if (standByRoomUiState.allPlayer.size > 2) {
                Image(
                    painter = painterResource(selectDrawable(allPlayer[2].icon)),
                    contentDescription = "icon3",
                    modifier = Modifier
                        .constrainAs(icon3) {
                            top.linkTo(name1.bottom)
                            start.linkTo(dialog.start)
                        }
                        .height(100.dp)
                        .width(100.dp)
                )
                Text(
                    text = allPlayer[2].name,
                    modifier = Modifier
                        .constrainAs(name3) {
                            top.linkTo(icon3.bottom)
                            start.linkTo(icon3.start)
                            end.linkTo(icon3.end)
                        }
                )
            }
            if (standByRoomUiState.allPlayer.size > 3) {
                Image(
                    painter = painterResource(selectDrawable(allPlayer[3].icon)),
                    contentDescription = "icon4",
                    modifier = Modifier
                        .constrainAs(icon4) {
                            top.linkTo(name2.bottom)
                            start.linkTo(icon3.end)
                        }
                        .padding(start = 12.dp)
                        .height(100.dp)
                        .width(100.dp)
                )
                Text(
                    text = allPlayer[3].name,
                    modifier = Modifier
                        .constrainAs(name4) {
                            top.linkTo(icon4.bottom)
                            start.linkTo(icon4.start)
                            end.linkTo(icon4.end)
                        }
                )
            }
            Image(
                painter = painterResource(R.drawable.bt_start),
                contentDescription = "button_start",
                modifier = Modifier
                    .constrainAs(btStart) {
                        end.linkTo(parent.end)
                        bottom.linkTo(dialog.bottom)
                        start.linkTo(parent.start)
                    }
                    .padding(bottom = 12.dp)
                    .width(142.dp)
                    .height(72.dp)
                    .clickable {
                        navController.navigate("main")
                        viewModel.updateIsStart()
                    }
            )
        }
    }
}

@Composable
@Preview
fun StandByRoomPreview() {
    ConstraintLayout {
        // Create references for the composable to constrain
        val (background, dialog, textSecret, icon1, icon2, icon3, icon4, name1, name2, name3, name4, btStart) = createRefs()

        Image(
            painter = painterResource(R.drawable.title_background_responsive_nontitlever),
            contentDescription = "background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .constrainAs(background) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )
        Image(
            painter = painterResource(R.drawable.stand_by_room),
            contentDescription = "dialog",
            modifier = Modifier
                .constrainAs(dialog) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
                .width(302.dp)
                .height(405.dp)
        )
        Text(
            text = "secretWords",
            fontSize = 24.sp,
            modifier = Modifier
                .constrainAs(textSecret) {
                    top.linkTo(dialog.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                .padding(top = 44.dp)
        )
        Image(
            painter = painterResource(R.drawable.user01_normal),
            contentDescription = "icon1",
            modifier = Modifier
                .constrainAs(icon1) {
                    top.linkTo(dialog.top)
                    start.linkTo(dialog.start)
                }
                .padding(top = 80.dp, start = 12.dp)
                .height(100.dp)
                .width(100.dp)
        )
        Text(
            text = "name1",
            modifier = Modifier
                .constrainAs(name1) {
                    top.linkTo(icon1.bottom)
                    start.linkTo(icon1.start)
                    end.linkTo(icon1.end)
                }
        )
        Image(
            painter = painterResource(R.drawable.user01_normal),
            contentDescription = "icon2",
            modifier = Modifier
                .constrainAs(icon2) {
                    top.linkTo(dialog.top)
                    start.linkTo(icon1.end)
                }
                .padding(top = 80.dp, start = 12.dp)
                .height(100.dp)
                .width(100.dp)
        )
        Text(
            text = "name2",
            modifier = Modifier
                .constrainAs(name2) {
                    top.linkTo(icon1.bottom)
                    start.linkTo(icon2.start)
                    end.linkTo(icon2.end)
                }
        )
        Image(
            painter = painterResource(R.drawable.user01_normal),
            contentDescription = "icon3",
            modifier = Modifier
                .constrainAs(icon3) {
                    top.linkTo(name1.bottom)
                    start.linkTo(dialog.start)
                }
                .height(100.dp)
                .width(100.dp)
        )
        Text(
            text = "name3",
            modifier = Modifier
                .constrainAs(name3) {
                    top.linkTo(icon3.bottom)
                    start.linkTo(icon3.start)
                    end.linkTo(icon3.end)
                }
        )
        Image(
            painter = painterResource(R.drawable.user01_normal),
            contentDescription = "icon4",
            modifier = Modifier
                .constrainAs(icon4) {
                    top.linkTo(name2.bottom)
                    start.linkTo(icon3.end)
                }
                .padding(start = 12.dp)
                .height(100.dp)
                .width(100.dp)
        )
        Text(
            text = "name4",
            modifier = Modifier
                .constrainAs(name4) {
                    top.linkTo(icon4.bottom)
                    start.linkTo(icon4.start)
                    end.linkTo(icon4.end)
                }
        )
        Image(
            painter = painterResource(R.drawable.bt_start),
            contentDescription = "button_start",
            modifier = Modifier
                .constrainAs(btStart) {
                    end.linkTo(parent.end)
                    bottom.linkTo(dialog.bottom)
                    start.linkTo(parent.start)
                }
                .padding(bottom = 12.dp)
                .width(142.dp)
                .height(72.dp)
        )
    }
}
