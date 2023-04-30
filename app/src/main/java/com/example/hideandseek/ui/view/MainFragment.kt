package com.example.hideandseek.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Visibility
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.hideandseek.R
import com.example.hideandseek.data.datasource.local.TrapData
import com.example.hideandseek.ui.viewmodel.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//@AndroidEntryPoint
//class MainFragment(
//    mainDispatcher: CoroutineDispatcher = Dispatchers.Main
//) : Fragment() {
//    private val viewModel: MainFragmentViewModel by viewModels()
//
//    private val coroutineScope = CoroutineScope(mainDispatcher)
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?,
//    ): View {
//        // BeTrappedFragmentから戻ってきた時
//        setFragmentResultListener("BeTrappedFragmentSkillTime") { _, bundle ->
//            val result = bundle.getString("skillTime")
//            Log.d("skillTimeResultFragment", result.toString())
//            if (result != null) {
//                viewModel.setSkillTImeString(result)
//            }
//        }
//
//        setFragmentResultListener("BeTrappedFragmentIsOverSkillTime") { _, bundle ->
//            val result = bundle.getBoolean("isOverSkillTime")
//            Log.d("isOverSkillTimeResultFragment", result.toString())
//            viewModel.setIsOverSkillTime(result)
//        }
//
//        lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.uiState.collect { mainUiState ->
//                    Log.d("UseCaseTest", mainUiState.latestUser.toString())
//
//                    setFragmentResult("MainFragmentIsOverSkillTime", bundleOf("isOverSkillTime" to mainUiState.isOverSkillTime))
//
//                    if (mainUiState.isOverLimitTime) {
//                        // statusをクリアにする
//                        viewModel.updatePlayerStatus(4)
//                        // クリアダイアログを表示
//                        val successEscapeDialogFragment = SuccessEscapeDialogFragment()
//                        val supportFragmentManager = childFragmentManager
//                        successEscapeDialogFragment.show(supportFragmentManager, "clear")
//                    }
//
//                    val limitTime = mainUiState.limitTime
//                    val skillTime = mainUiState.skillTime
//
//                    Log.d("UiState", "stateを更新しました")
//                    val allLocation = mainUiState.allLocation
//                    val allTraps = mainUiState.allTrap
//                    val latestUser = mainUiState.latestUser
//
//                    // 自分の情報の表示
//                    Log.d("UserLive", latestUser.toString())
//                    if (limitTime == "" && latestUser.relativeTime != "") {
//                        viewModel.setLimitTime(latestUser.relativeTime)
//                    }
//
//                    if (latestUser.relativeTime != "") {
//
//                        // 制限時間になったかどうかの判定
//                        if (limitTime != "") {
//                            viewModel.compareTime(latestUser.relativeTime, limitTime)
//                            setFragmentResult("MainFragmentLimitTime", bundleOf("limitTime" to limitTime))
//                        }
//
//                        // 他人の位置を追加
//                        Log.d("ALL_Location", allLocation.toString())
//                        if (allLocation.isNotEmpty()) {
//                            // ユーザーの位置情報
//                            for (i in allLocation.indices) {
//                                when (allLocation[i].status) {
//                                    1 -> {
//                                        // 他人の罠をRoomにinsert
//                                        val trap = TrapData(0, allLocation[i].latitude, allLocation[i].longitude, 1)
//                                        viewModel.postOthersTrap(trap)
//                                        viewModel.deleteLocation(allLocation[i])
//                                    }
//                                    -1 -> {
//                                        // 罠を削除
//                                        if (allTraps.isNotEmpty()) {
//                                            for (j in allTraps.indices) {
//                                                if (allTraps[j].latitude == allLocation[i].latitude) {
//                                                    viewModel.deleteTrap(allTraps[j])
//                                                }
//                                            }
//                                        }
//                                        viewModel.deleteLocation(allLocation[i])
//                                    }
//                                }
//                            }
//                        }
//                        Log.d("All_trap", allTraps.toString())
//
//                        // trapの位置情報
//                        if (allTraps.isNotEmpty()) {
//                            for (i in allTraps.indices) {
//                                if (viewModel.checkCaughtTrap(latestUser, allTraps[i])) {
//                                    // かかったTrapの削除(local)
//                                    viewModel.deleteTrap(allTraps[i])
//
//                                    // かかったTrapの削除(remote)
//                                    viewModel.postTrapSpacetime("delete", latestUser)
//
//                                    // トラップにかかったステータスに変更
//                                    viewModel.updatePlayerStatus(2)
//
//                                    // TrapにかかったらFragmentを移動
//                                    setFragmentResult("MainFragmentTrapTime", bundleOf("trapTime" to latestUser.relativeTime))
//
//                                    findNavController().navigate(R.id.navigation_be_trapped)
//                                }
//                            }
//                        }
//
//                        // Skill Buttonの Progress Bar
//                        // スキルボタンを複数回押したとき、relativeが一旦最初の(skillTime+1)秒になって、本来のrelativeまで1秒ずつ足される
//                        // observeを二重にしてるせいで変な挙動していると思われる（放置するとメモリやばそう）
//                        // この辺ちゃんと仕様わかってないので、リファクタリング時に修正する
//                        if (skillTime != "") {
//                            viewModel.compareSkillTime(latestUser.relativeTime, skillTime)
//                            setFragmentResult("MainFragmentSkillTime", bundleOf("skillTime" to skillTime))
//                        }
//
//                        // URLから画像を取得
//                        // 相対時間10秒おきに行う
//                        if (latestUser.relativeTime.substring(7, 8) == "0") {
//                            Log.d("fetchMAP", "Mapが更新されました")
//                            coroutineScope.launch {
//                                viewModel.fetchMap(latestUser, allLocation, allTraps)
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        return ComposeView(requireContext()).apply {
//            setContent {
//                MainFragmentScreen()                )
//            }
//        }
//    }
//}

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
fun MainFragmentScreen(viewModel: MainFragmentViewModel = androidx.lifecycle.viewmodel.compose.viewModel(), navController: NavController) {
    val mainFragmentUiState by viewModel.uiState.collectAsState()

    val latestUser = mainFragmentUiState.latestUser
    val skillTime = mainFragmentUiState.skillTime
    val allPlayer = mainFragmentUiState.allPlayer

    val howProgressSkill =
        if (skillTime != "") {
            viewModel.howProgressSkillTime(
                latestUser.relativeTime,
                skillTime,
            ) / 60f
        } else {
            0f
        }

    Surface(modifier = Modifier.fillMaxSize()) {
        mainFragmentUiState.map?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "map",
                contentScale = ContentScale.Crop
            )
        }
        ConstraintLayout {
            // Create references for the composable to constrain
            val (ivTime, tvNow, tvRelative, tvLimit, tvLimitTime, btCaptureOn, btSkillOn, btSkillOff, progressSkill, user1, user2, user3, user4) = createRefs()

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
            if (latestUser.relativeTime != "") {
                Text(
                    text = latestUser.relativeTime,
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
                text = mainFragmentUiState.limitTime,
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
            if (mainFragmentUiState.isOverSkillTime) {
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
                            // 自分の罠をRoomにinsert
                            viewModel.postTrapRoom(0, latestUser)
                            viewModel.postTrapSpacetime("post", latestUser)
                            viewModel.setSkillTime(latestUser)
                            viewModel.setIsOverSkillTime(false)
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
            if (allPlayer.isNotEmpty()) {
                Image(
                    painter = painterResource(selectDrawable(allPlayer[0].icon)),
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
            }
            if (allPlayer.size > 1) {
                Image(
                    painter = painterResource(selectDrawable(allPlayer[1].icon)),
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
            }
            if (allPlayer.size > 2) {
                Image(
                    painter = painterResource(selectDrawable(allPlayer[2].icon)),
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
            }
            if (allPlayer.size > 3) {
                Image(
                    painter = painterResource(selectDrawable(allPlayer[3].icon)),
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
}

@Composable
@Preview
fun MainFragmentPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.title_background_responsive_nontitlever),
            contentDescription = "background",
            contentScale = ContentScale.Crop
        )
        ConstraintLayout {
            // Create references for the composable to constrain
            val (ivTime, tvNow, tvRelative, tvLimit, tvLimitTime, btCaptureOn, btSkillOn, btSkillOff, progressSkill, user1, user2, user3, user4) = createRefs()

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
            Text(
                text = "relative_time",
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier
                    .constrainAs(tvRelative) {
                        bottom.linkTo(tvNow.bottom)
                        start.linkTo(tvNow.end)
                    }
                    .padding(start = 12.dp)
            )
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
                text = "limit_time",
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
            )
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
            )
            Image(
                painter = painterResource(R.drawable.button_skill_off),
                contentDescription = "skill button off",
                modifier = Modifier
                    .constrainAs(btSkillOff) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        visibility = Visibility.Invisible
                    }
                    .height(100.dp)
                    .width(180.dp)
            )
            LinearProgressIndicator(
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
