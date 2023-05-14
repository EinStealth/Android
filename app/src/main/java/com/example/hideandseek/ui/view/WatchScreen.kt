package com.example.hideandseek.ui.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.hideandseek.R
import com.example.hideandseek.ui.viewmodel.WatchFragmentViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun WatchScreen(viewModel: WatchFragmentViewModel = androidx.lifecycle.viewmodel.compose.viewModel(), navController: NavController, mainDispatcher: CoroutineDispatcher = Dispatchers.Main) {
    val watchUiState by viewModel.uiState.collectAsState()

    val coroutineScope = CoroutineScope(mainDispatcher)

    val allLocation = watchUiState.allLocation
    val allTraps = watchUiState.allTrap
    val latestUser = watchUiState.latestUser

    // 自分の情報の表示
    Log.d("UserLive", latestUser.toString())
    if (latestUser.relativeTime != "") {
        if (watchUiState.map == null) {
            // URLから画像を取得
            // 相対時間10秒おきに行う
            Log.d("fetchMAP", "Mapが更新されました")
            coroutineScope.launch {
                viewModel.fetchMap(latestUser, allLocation, allTraps)
            }
        }

        // 他人の位置を追加
        Log.d("ALL_Location", allLocation.toString())
        if (allLocation.isNotEmpty()) {
            // ユーザーの位置情報
            for (i in allLocation.indices) {
                if (allLocation[i].status == 1) {
                    viewModel.postTrapRoom(1, latestUser)
                }
            }
        }

        // URLから画像を取得
        // 相対時間10秒おきに行う
        if (watchUiState.preRelativeTime == "") {
            viewModel.updatePreRelativeTime()
        }
        else {
            Log.d("fetchMap", "re: ${latestUser.relativeTime}, pre: ${watchUiState.preRelativeTime}")
            if (latestUser.relativeTime.substring(6, 7) != watchUiState.preRelativeTime.substring(6, 7)) {
                Log.d("fetchMAP", "Mapが更新されました")
                coroutineScope.launch {
                    viewModel.fetchMap(latestUser, allLocation, allTraps)
                }
                viewModel.updatePreRelativeTime()
            }
        }
    }

    Surface(Modifier.fillMaxSize()) {
        watchUiState.map?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "map",
                contentScale = ContentScale.Crop
            )
        }
        ConstraintLayout {
            // Create references for the composable to constrain
            val (ivWatching, btCaptureOff, btSkillOf, user1, user2, user3, user4) = createRefs()

            Image(
                painter = painterResource(R.drawable.text_watching),
                contentDescription = "text_watching",
                modifier = Modifier
                    .constrainAs(ivWatching) {
                        end.linkTo(parent.end)
                        bottom.linkTo(btCaptureOff.top)
                        start.linkTo(parent.start)
                    }
                    .height(80.dp)
            )
            Image(
                painter = painterResource(R.drawable.button_captured_off),
                contentDescription = "鬼に捕まったときに押すボタン",
                modifier = Modifier
                    .constrainAs(btCaptureOff) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
                    .height(100.dp)
                    .width(200.dp)
            )
            Image(
                painter = painterResource(R.drawable.button_skill_off),
                contentDescription = "skill button",
                modifier = Modifier
                    .constrainAs(btSkillOf) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .height(100.dp)
                    .width(180.dp)
            )
            Image(
                painter = painterResource(R.drawable.user01_caputure),
                contentDescription = "user1",
                modifier = Modifier
                    .constrainAs(user1) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .padding(start = 40.dp)
                    .height(72.dp)
                    .width(72.dp)
            )
            Image(
                painter = painterResource(R.drawable.user02_runaway),
                contentDescription = "user2",
                modifier = Modifier
                    .constrainAs(user2) {
                        top.linkTo(user1.top)
                        end.linkTo(user3.start)
                        start.linkTo(user1.end)
                    }
                    .height(72.dp)
                    .width(72.dp)
            )
            Image(
                painter = painterResource(R.drawable.user03_runaway),
                contentDescription = "user3",
                modifier = Modifier
                    .constrainAs(user3) {
                        top.linkTo(user1.top)
                        end.linkTo(user4.start)
                        start.linkTo(user2.end)
                    }
                    .height(72.dp)
                    .width(72.dp)
            )
            Image(
                painter = painterResource(R.drawable.user04_oni),
                contentDescription = "user4",
                modifier = Modifier
                    .constrainAs(user4) {
                        top.linkTo(user1.top)
                        end.linkTo(parent.end)
                    }
                    .padding(end = 40.dp)
                    .height(72.dp)
                    .width(72.dp)
            )
        }
    }
}

@Composable
@Preview
fun WatchPreview() {
    Surface(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.title_background_responsive_nontitlever),
            contentDescription = "map",
            contentScale = ContentScale.Crop
        )
        ConstraintLayout {
            // Create references for the composable to constrain
            val (ivWatching, btCaptureOff, btSkillOf, user1, user2, user3, user4) = createRefs()

            Image(
                painter = painterResource(R.drawable.text_watching),
                contentDescription = "text_watching",
                modifier = Modifier
                    .constrainAs(ivWatching) {
                        end.linkTo(parent.end)
                        bottom.linkTo(btCaptureOff.top)
                        start.linkTo(parent.start)
                    }
                    .height(80.dp)
            )
            Image(
                painter = painterResource(R.drawable.button_captured_off),
                contentDescription = "鬼に捕まったときに押すボタン",
                modifier = Modifier
                    .constrainAs(btCaptureOff) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
                    .height(100.dp)
                    .width(200.dp)
            )
            Image(
                painter = painterResource(R.drawable.button_skill_off),
                contentDescription = "skill button",
                modifier = Modifier
                    .constrainAs(btSkillOf) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .height(100.dp)
                    .width(180.dp)
            )
            Image(
                painter = painterResource(R.drawable.user01_caputure),
                contentDescription = "user1",
                modifier = Modifier
                    .constrainAs(user1) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .padding(start = 40.dp)
                    .height(72.dp)
                    .width(72.dp)
            )
            Image(
                painter = painterResource(R.drawable.user02_runaway),
                contentDescription = "user2",
                modifier = Modifier
                    .constrainAs(user2) {
                        top.linkTo(user1.top)
                        end.linkTo(user3.start)
                        start.linkTo(user1.end)
                    }
                    .height(72.dp)
                    .width(72.dp)
            )
            Image(
                painter = painterResource(R.drawable.user03_runaway),
                contentDescription = "user3",
                modifier = Modifier
                    .constrainAs(user3) {
                        top.linkTo(user1.top)
                        end.linkTo(user4.start)
                        start.linkTo(user2.end)
                    }
                    .height(72.dp)
                    .width(72.dp)
            )
            Image(
                painter = painterResource(R.drawable.user04_oni),
                contentDescription = "user4",
                modifier = Modifier
                    .constrainAs(user4) {
                        top.linkTo(user1.top)
                        end.linkTo(parent.end)
                    }
                    .padding(end = 40.dp)
                    .height(72.dp)
                    .width(72.dp)
            )
        }
    }
}
