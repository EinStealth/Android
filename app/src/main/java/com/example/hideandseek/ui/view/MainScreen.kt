package com.example.hideandseek.ui.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hideandseek.R
import com.example.hideandseek.data.datasource.local.TrapData
import com.example.hideandseek.data.datasource.remote.ResponseData
import com.example.hideandseek.ui.viewmodel.MainFragmentViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private fun selectDrawable(icon: Int, status: Int): Int {
    val list: List<List<Int>> = listOf(
        listOf(R.drawable.user01_runaway, R.drawable.user01_caputure, R.drawable.user01_clear, R.drawable.user01_oni),
        listOf(R.drawable.user02_runaway, R.drawable.user02_caputure, R.drawable.user02_clear, R.drawable.user02_oni),
        listOf(R.drawable.user03_runaway, R.drawable.user03_capture, R.drawable.user03_clear, R.drawable.user03_oni),
        listOf(R.drawable.user04_runaway, R.drawable.user04_capture, R.drawable.user04_clear, R.drawable.user04_oni),
    )
    return list[icon - 1][status % 10]
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MainScreen(viewModel: MainFragmentViewModel = androidx.lifecycle.viewmodel.compose.viewModel(), navController: NavController, mainDispatcher: CoroutineDispatcher = Dispatchers.Main) {
    // 別composeから戻ってきたとき
    val returnSkillTime = viewModel.readSkillTime()
    val returnIsOverSkillTime = viewModel.readIsOverSkillTime()
    val returnLimitTime = viewModel.readLimitTime()
    Log.d("SavedSkillTime and IsOver", "skillTime: " + returnSkillTime + "isOver: $returnIsOverSkillTime")
    if (returnSkillTime != "") {
        viewModel.setSkillTImeString(returnSkillTime)
        if (!returnIsOverSkillTime) {
            viewModel.setIsOverSkillTime(false)
        }
    }
    if (returnLimitTime != "") {
        viewModel.setLimitTimeString(returnLimitTime)
    }

    val mainUiState by viewModel.uiState.collectAsState()

    val coroutineScope = CoroutineScope(mainDispatcher)

    val latestUser = mainUiState.latestUser
    val skillTime = mainUiState.skillTime
    val allPlayer = mainUiState.allPlayer

    Log.d("SkillTime and IsOver", "skillTime: " + mainUiState.skillTime + "isOver: ${mainUiState.isOverSkillTime}")

    Log.d("UseCaseTest", mainUiState.latestUser.toString())

    viewModel.saveIsOverSkillTime(mainUiState.isOverSkillTime)

    if (mainUiState.isOverLimitTime && clearCallCount == 0) {
        // statusをクリアにする
        viewModel.updatePlayerStatus(2)
        // クリアダイアログを表示
        navController.navigate("clear")
        clearCallCount += 1
    }

    val limitTime = mainUiState.limitTime

    Log.d("UiState", "stateを更新しました")
    val allLocation = mainUiState.allLocation
    val allTraps = mainUiState.allTrap

    // 自分の情報の表示
    Log.d("UserLive", latestUser.toString())
    if (limitTime == "" && latestUser.relativeTime != "") {
        viewModel.setLimitTime(latestUser.relativeTime)
    }

    if (latestUser.relativeTime != "") {
        if (mainUiState.map == null) {
            // URLから画像を取得
            Log.d("fetchMAP", "Mapが更新されました")
            coroutineScope.launch {
                viewModel.fetchMap(latestUser, allLocation, allTraps)
            }
        }

        // 制限時間になったかどうかの判定
        if (limitTime != "") {
            viewModel.compareTime(latestUser.relativeTime, limitTime)
            viewModel.saveLimitTime(limitTime)
        }

        // 他人の位置を追加
        Log.d("ALL_Location", allLocation.toString())
        if (allLocation.isNotEmpty()) {
            // ユーザーの位置情報
            for (i in allLocation.indices) {
                when (allLocation[i].status) {
                    1 -> {
                        // 他人の罠をRoomにinsert
                        val trap = TrapData(0, allLocation[i].latitude, allLocation[i].longitude, 1)
                        viewModel.postOthersTrap(trap)
                        viewModel.deleteLocation(allLocation[i])
                    }
                    -1 -> {
                        // 罠を削除
                        if (allTraps.isNotEmpty()) {
                            for (j in allTraps.indices) {
                                if (allTraps[j].latitude == allLocation[i].latitude) {
                                    viewModel.deleteTrap(allTraps[j])
                                }
                            }
                        }
                        viewModel.deleteLocation(allLocation[i])
                    }
                }
            }
        }
        Log.d("All_trap", allTraps.toString())

        // trapの位置情報
        if (allTraps.isNotEmpty()) {
            for (i in allTraps.indices) {
                if (viewModel.checkCaughtTrap(latestUser, allTraps[i])) {
                    // かかったTrapの削除(local)
                    viewModel.deleteTrap(allTraps[i])

                    // かかったTrapの削除(remote)
                    viewModel.postTrapSpacetime("delete", latestUser)

                    // トラップにかかったステータスに変更
                    viewModel.updatePlayerStatus(1)

                    // TrapにかかったらFragmentを移動
                    viewModel.saveTrapTime(latestUser.relativeTime)

                    navController.navigate("beTrapped")
                    mainCallCount = 0
                }
            }
        }

        // Skill Buttonの Progress Bar
        // スキルボタンを複数回押したとき、relativeが一旦最初の(skillTime+1)秒になって、本来のrelativeまで1秒ずつ足される
        // observeを二重にしてるせいで変な挙動していると思われる（放置するとメモリやばそう）
        // この辺ちゃんと仕様わかってないので、リファクタリング時に修正する
        if (skillTime != "") {
            viewModel.compareSkillTime(latestUser.relativeTime, skillTime)
            viewModel.saveSkillTime(skillTime)
        }

        // URLから画像を取得
        // 相対時間10秒おきに行う
        if (mainUiState.preRelativeTime == "") {
            viewModel.updatePreRelativeTime()
        } else {
            Log.d("fetchMap", "re: ${latestUser.relativeTime}, pre: ${mainUiState.preRelativeTime}")
            if (latestUser.relativeTime.substring(6, 7) != mainUiState.preRelativeTime.substring(6, 7)) {
                Log.d("fetchMAP", "Mapが更新されました")
                coroutineScope.launch {
                    viewModel.fetchMap(latestUser, allLocation, allTraps)
                }
                viewModel.updatePreRelativeTime()
            }
        }
    }

    val howProgressSkill =
        if (skillTime != "") {
            viewModel.howProgressSkillTime(
                latestUser.relativeTime,
                skillTime,
            ) / 60f
        } else {
            0f
        }

    val onClickSkillButton = {
        viewModel.postTrapRoom(0, latestUser)
        viewModel.postTrapSpacetime("post", latestUser)
        viewModel.setSkillTime(latestUser)
        viewModel.saveSkillTime(latestUser.relativeTime)
        viewModel.setIsOverSkillTime(false)
        viewModel.saveIsOverSkillTime(false)
    }

    MainLayout(
        map = mainUiState.map,
        relativeTime = latestUser.relativeTime,
        limitTime = limitTime,
        navController = navController,
        isOverSkillTime = mainUiState.isOverSkillTime,
        onClickSkillButton = onClickSkillButton,
        howProgressSkill = howProgressSkill,
        allPlayer = allPlayer
    )
}

@Composable
private fun MainLayout(
    map: Bitmap?,
    relativeTime: String,
    limitTime: String,
    navController: NavController,
    isOverSkillTime: Boolean,
    onClickSkillButton: () -> Unit,
    howProgressSkill: Float,
    allPlayer: List<ResponseData.ResponseGetPlayer>

) {
    Surface(modifier = Modifier.fillMaxSize()) {
        map?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(R.drawable.text_times),
                    contentDescription = null,
                    modifier = Modifier
                        .height(100.dp)
                )
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "NOW",
                        fontSize = 12.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(start = 40.dp)
                    )
                    if (relativeTime != "") {
                        Text(
                            text = relativeTime,
                            fontSize = 20.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .padding(start = 12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "LIMIT",
                        fontSize = 12.sp,
                        color = Color.Red,
                        modifier = Modifier
                            .padding(end = 12.dp)
                    )
                    Text(
                        text = limitTime,
                        fontSize = 20.sp,
                        color = Color.Red,
                        modifier = Modifier
                            .padding(end = 40.dp)
                    )
                }
            }
            LazyRow {
                items(allPlayer) {
                    Image(
                        painter = painterResource(id = selectDrawable(it.icon, it.status)),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 28.dp)
                            .size(width = 72.dp, height = 72.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row {
                Image(
                    painter = painterResource(R.drawable.button_captured_on),
                    contentDescription = null,
                    modifier = Modifier
                        .size(width = 200.dp, height = 100.dp)
                        .clickable {
                            navController.navigate("capture")
                        }
                )
                if (isOverSkillTime) {
                    Image(
                        painter = painterResource(R.drawable.button_skill_on),
                        contentDescription = null,
                        modifier = Modifier
                            .size(width = 180.dp, height = 100.dp)
                            .clickable {
                                onClickSkillButton()
                            }
                    )
                } else {
                    Box(
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Image(
                            painter = painterResource(R.drawable.button_skill_off),
                            contentDescription = null,
                            modifier = Modifier
                                .size(width = 180.dp, height = 100.dp)
                        )
                        LinearProgressIndicator(
                            progress = howProgressSkill,
                            modifier = Modifier
                                .padding(bottom = 24.dp, start = 8.dp)
                                .size(width = 80.dp, height = 10.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MainLayout(
        map = null,
        relativeTime = "",
        limitTime = "",
        navController = rememberNavController(),
        isOverSkillTime = true,
        onClickSkillButton = {},
        howProgressSkill = 0f,
        allPlayer = listOf()
    )
}
