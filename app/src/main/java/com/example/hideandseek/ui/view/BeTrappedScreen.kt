package com.example.hideandseek.ui.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
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
        ConstraintLayout {
            // Create references for the composable to constrain
            val (eye, tvTrap, progressTrap, ivTime, tvNow, tvRelative, tvLimit, tvLimitTime, btCaptureOn, btSkillOn, btSkillOff, progressSkill, user1, user2, user3, user4) = createRefs()

            Image(
                painter = painterResource(R.drawable.eye),
                contentDescription = "text_watching",
                modifier = Modifier
                    .constrainAs(eye) {
                        end.linkTo(parent.end)
                        bottom.linkTo(tvTrap.top)
                        start.linkTo(parent.start)
                    }
                    .height(160.dp)
                    .width(160.dp)
            )
            Text(
                text = "罠が解除されるまで",
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier
                    .constrainAs(tvTrap) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
            )
            LinearProgressIndicator(
                progress = howProgressTrap,
                modifier = Modifier
                    .constrainAs(progressTrap) {
                        top.linkTo(tvTrap.bottom)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
                    .padding(top = 20.dp)
                    .height(40.dp)
                    .width(280.dp)
            )
            Image(
                painter = painterResource(R.drawable.text_times),
                contentDescription = "時間が表示されています",
                modifier = Modifier
                    .constrainAs(ivTime) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
                    .height(100.dp)
            )
            Text(
                text = "NOW",
                fontSize = 12.sp,
                color = Color.Black,
                modifier = Modifier
                    .constrainAs(tvNow) {
                        top.linkTo(ivTime.top)
                        bottom.linkTo(ivTime.bottom)
                        start.linkTo(ivTime.start)
                    }
                    .padding(start = 40.dp)
            )
            if (relativeTime != "") {
                Text(
                    text = relativeTime,
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .constrainAs(tvRelative) {
                            bottom.linkTo(tvNow.bottom)
                            start.linkTo(tvNow.end)
                        }
                        .padding(start = 12.dp)
                )
            }
            Text(
                text = "LIMIT",
                fontSize = 12.sp,
                color = Color.Red,
                modifier = Modifier
                    .constrainAs(tvLimit) {
                        end.linkTo(tvLimitTime.start)
                        bottom.linkTo(tvNow.bottom)
                    }
                    .padding(end = 12.dp)
            )
            Text(
                text = limitTime,
                fontSize = 20.sp,
                color = Color.Red,
                modifier = Modifier
                    .constrainAs(tvLimitTime) {
                        end.linkTo(ivTime.end)
                        bottom.linkTo(tvNow.bottom)
                    }
                    .padding(end = 40.dp)
            )
            Image(
                painter = painterResource(R.drawable.button_captured_on),
                contentDescription = "鬼に捕まったときに押すボタン",
                modifier = Modifier
                    .constrainAs(btCaptureOn) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
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
                        .constrainAs(btSkillOn) {
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                        .height(100.dp)
                        .width(180.dp)
                        .clickable {
                            onClickSkillButton()
                        }
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.button_skill_off),
                    contentDescription = "skill button off",
                    modifier = Modifier
                        .constrainAs(btSkillOff) {
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                        .height(100.dp)
                        .width(180.dp)
                )
                LinearProgressIndicator(
                    progress = howProgressSkill,
                    modifier = Modifier
                        .constrainAs(progressSkill) {
                            end.linkTo(btSkillOff.end)
                            bottom.linkTo(btSkillOff.bottom)
                            start.linkTo(btSkillOff.start)
                        }
                        .padding(bottom = 24.dp, start = 8.dp)
                        .height(10.dp)
                        .width(80.dp)
                )
            }
            Image(
                painter = painterResource(R.drawable.user01_caputure),
                contentDescription = "user1",
                modifier = Modifier
                    .constrainAs(user1) {
                        top.linkTo(ivTime.bottom)
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

@Preview
@Composable
private fun BeTrappedPreview() {
    val navController = rememberNavController()
    BeTrappedLayout(
        howProgressTrap = 0f,
        howProgressSkill = 0f,
        relativeTime = "",
        navController = navController,
        isOverSkillTime = true,
        onClickSkillButton = {}
    )
}
