package com.example.hideandseek.ui.view

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
import androidx.compose.foundation.layout.width
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hideandseek.R
import com.example.hideandseek.ui.viewmodel.BeTrappedFragmentViewModel

// 2重LiveData解消のために変数定義
private var limitTime = ""
private var trapTime = ""
var mainCallCount = 0
var clearCallCount = 0
var isOverTrapInBeTrapped = true

@Composable
fun BeTrappedScreen(viewModel: BeTrappedFragmentViewModel = androidx.lifecycle.viewmodel.compose.viewModel(), navController: NavController) {
    // 別composeから戻ってきたとき
    val returnSkillTime = viewModel.readSkillTime()
    val returnIsOverSkillTime = viewModel.readIsOverSkillTime()
    val returnTrapTime = viewModel.readTrapTime()
    val returnLimitTime = viewModel.readLimitTime()
    Log.d("SavedSkillTime and IsOver", "skillTime: " + returnSkillTime + "isOver: $returnIsOverSkillTime")
    if (returnSkillTime != "") {
        viewModel.setSkillTimeInit(returnSkillTime)
        if (!returnIsOverSkillTime) {
            viewModel.setIsOverSkillTime(false)
        }
    }
    if (returnTrapTime != "") {
        trapTime = returnTrapTime
    }
    if (returnLimitTime != "") {
        limitTime = returnLimitTime
    }

    val beTrappedUiState by viewModel.uiState.collectAsState()

    val skillTime = beTrappedUiState.skillTime

    isOverTrapInBeTrapped = beTrappedUiState.isOverTrapTime

    // Skillを使ってからの時間を監視
    viewModel.saveIsOverSkillTime(beTrappedUiState.isOverSkillTime)

    // 罠にかかっている時間の監視
    if (beTrappedUiState.isOverTrapTime && mainCallCount == 0) {
        navController.navigate("main")
        Log.d("MAIN_COUNT", "MainScreenが呼ばれました. count: $mainCallCount")
        mainCallCount += 1
    }

    // 制限時間と相対時間の監視
    if (beTrappedUiState.isOverLimitTime && clearCallCount == 0) {
        // クリアダイアログを表示
        navController.navigate("clear")
        clearCallCount += 1
    }

    val latestUser = beTrappedUiState.latestUser

    Log.d("UserLive", latestUser.toString())
    if (latestUser.relativeTime != "") {
        // 制限時間になったかどうかの判定
        viewModel.compareTime(latestUser.relativeTime, limitTime)

        // trapにかかっている時間を計測
        viewModel.compareTrapTime(latestUser.relativeTime, trapTime)

        // Skill Buttonの Progress Bar
        // スキルボタンを複数回押したとき、relativeが一旦最初の(skillTime+1)秒になって、本来のrelativeまで1秒ずつ足される
        // observeを二重にしてるせいで変な挙動していると思われる（放置するとメモリやばそう）
        // この辺ちゃんと仕様わかってないので、リファクタリング時に修正する
        if (skillTime != "") {
            viewModel.compareSkillTime(
                latestUser.relativeTime,
                skillTime,
            )
            viewModel.saveSkillTime(skillTime)
        }
    }

    val howProgressTrap =
        if (latestUser.relativeTime != "" && trapTime != "") {
            viewModel.howProgressTrapTime(latestUser.relativeTime, trapTime) / 60f
        } else {
            0f
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
        viewModel.postTrapSpacetime(latestUser)
        viewModel.setSkillTime(latestUser)
        viewModel.setIsOverSkillTime(false)
    }

    BeTrappedLayout(
        howProgressTrap = howProgressTrap,
        howProgressSkill = howProgressSkill,
        relativeTime = latestUser.relativeTime,
        navController = navController,
        isOverSkillTime = returnIsOverSkillTime,
        onClickSkillButton = onClickSkillButton
    )
}

@Composable
private fun BeTrappedLayout(
    howProgressTrap: Float,
    howProgressSkill: Float,
    relativeTime: String,
    navController: NavController,
    isOverSkillTime: Boolean,
    onClickSkillButton: () -> Unit,

) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
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
            Row(
                modifier = Modifier.height(72.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.user01_caputure),
                    contentDescription = "user1",
                    modifier = Modifier
                        .padding(start = 40.dp)
                        .height(72.dp)
                        .width(72.dp)
                )
                Image(
                    painter = painterResource(R.drawable.user02_runaway),
                    contentDescription = "user2",
                    modifier = Modifier
                        .height(72.dp)
                        .width(72.dp)
                )
                Image(
                    painter = painterResource(R.drawable.user03_runaway),
                    contentDescription = "user3",
                    modifier = Modifier
                        .height(72.dp)
                        .width(72.dp)
                )
                Image(
                    painter = painterResource(R.drawable.user04_oni),
                    contentDescription = "user4",
                    modifier = Modifier
                        .padding(end = 40.dp)
                        .height(72.dp)
                        .width(72.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.eye),
                contentDescription = "text_watching",
                modifier = Modifier
                    .height(160.dp)
                    .width(160.dp)
            )
            Text(
                text = "罠が解除されるまで",
                fontSize = 20.sp,
                color = Color.White,
            )
            LinearProgressIndicator(
                progress = howProgressTrap,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .height(40.dp)
                    .width(280.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.height(100.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.button_captured_on),
                    contentDescription = "鬼に捕まったときに押すボタン",
                    modifier = Modifier
                        .height(100.dp)
                        .width(200.dp)
                        .clickable {
                            navController.navigate("capture")
                        }
                )
                if (isOverSkillTime) {
                    Image(
                        painter = painterResource(R.drawable.button_skill_on),
                        contentDescription = "skill button on",
                        modifier = Modifier
                            .height(100.dp)
                            .width(180.dp)
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
                            contentDescription = "skill button off",
                            modifier = Modifier
                                .height(100.dp)
                                .width(180.dp)
                        )
                        LinearProgressIndicator(
                            progress = howProgressSkill,
                            modifier = Modifier
                                .padding(bottom = 24.dp, start = 8.dp)
                                .height(10.dp)
                                .width(80.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun BeTrappedPreview() {
    val navController = rememberNavController()
    BeTrappedLayout(
        howProgressTrap = 0f,
        howProgressSkill = 0f,
        relativeTime = "12:00:00",
        navController = navController,
        isOverSkillTime = false,
        onClickSkillButton = {}
    )
}
