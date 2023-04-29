package com.example.hideandseek.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
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
import androidx.compose.ui.layout.ContentScale
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
import androidx.navigation.fragment.findNavController
import com.example.hideandseek.R
import com.example.hideandseek.data.datasource.local.TrapData
import com.example.hideandseek.databinding.FragmentMainBinding
import com.example.hideandseek.ui.viewmodel.BeTrappedFragmentViewModel
import com.example.hideandseek.ui.viewmodel.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment(
    mainDispatcher: CoroutineDispatcher = Dispatchers.Main
) : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val viewModel: MainFragmentViewModel by viewModels()

    private val coroutineScope = CoroutineScope(mainDispatcher)

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Viewの取得
        // 時間表示の場所
        val tvRelativeTime: TextView = binding.tvRelativeTime
        val tvLimitTime: TextView = binding.tvLimitTime
        // Map
        val ivMap: ImageView = binding.ivMap
        // 捕まったボタン
        val btCaptureOn: ImageView = binding.btCaptureOn

        // スキルボタン
        val btSkillOn: ImageView = binding.btSkillOn
        val btSkillOff: ImageView = binding.btSkillOff
        val progressSkill: ProgressBar = binding.progressSkill

        fun changeBtSkillVisible(isOn: Boolean) {
            if (isOn) {
                btSkillOn.visibility = View.VISIBLE
                btSkillOff.visibility = View.INVISIBLE
                progressSkill.visibility = View.INVISIBLE
            } else {
                btSkillOn.visibility = View.INVISIBLE
                btSkillOff.visibility = View.VISIBLE
                progressSkill.visibility = View.VISIBLE

                progressSkill.max = 60
            }
        }

        // User normal
        // TODO: Statusを受け取って表示が切り替わるようにする
        val user1Normal: ImageView = binding.user1Normal
        val user2Normal: ImageView = binding.user2Normal
        val user3Normal: ImageView = binding.user3Normal
        // User demon
        val user4Demon: ImageView = binding.user4Demon
        // User captured
        val user1Captured: ImageView = binding.user1Captured

        // BeTrappedFragmentから戻ってきた時
        setFragmentResultListener("BeTrappedFragmentSkillTime") { _, bundle ->
            val result = bundle.getString("skillTime")
            Log.d("skillTimeResultFragment", result.toString())
            if (result != null) {
                viewModel.setSkillTImeString(result)
            }
        }

        setFragmentResultListener("BeTrappedFragmentIsOverSkillTime") { _, bundle ->
            val result = bundle.getBoolean("isOverSkillTime")
            Log.d("isOverSkillTimeResultFragment", result.toString())
            viewModel.setIsOverSkillTime(result)
        }

        setFragmentResultListener("UserRegisterFragmentName") { _, bundle ->
            val result = bundle.getString("name")
            Log.d("nameRegisterTest", result.toString())
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { mainUiState ->

                    val allPlayer = mainUiState.allPlayer
                    if (allPlayer.isNotEmpty()) {
                        for (i in allPlayer.indices) {
                            if (i == 0) {
                                user1Normal.setImageResource(selectDrawable(allPlayer[i].icon))
                            }
                            if (i == 1) {
                                user2Normal.setImageResource(selectDrawable(allPlayer[i].icon))
                            }
                            if (i == 2) {
                                user3Normal.setImageResource(selectDrawable(allPlayer[i].icon))
                            }
                            if (i == 3) {
                                user4Demon.setImageResource(selectDrawable(allPlayer[i].icon))
                            }
                        }
                    }

                    Log.d("UseCaseTest", mainUiState.latestUser.toString())

                    ivMap.setImageBitmap(mainUiState.map)

                    setFragmentResult("MainFragmentIsOverSkillTime", bundleOf("isOverSkillTime" to mainUiState.isOverSkillTime))
                    changeBtSkillVisible(mainUiState.isOverSkillTime)

                    if (mainUiState.isOverLimitTime) {
                        // statusをクリアにする
                        viewModel.updatePlayerStatus(4)
                        // クリアダイアログを表示
                        val successEscapeDialogFragment = SuccessEscapeDialogFragment()
                        val supportFragmentManager = childFragmentManager
                        successEscapeDialogFragment.show(supportFragmentManager, "clear")
                    }

                    tvLimitTime.text = mainUiState.limitTime
                    val limitTime = mainUiState.limitTime
                    val skillTime = mainUiState.skillTime

                    Log.d("UiState", "stateを更新しました")
                    val allLocation = mainUiState.allLocation
                    val allTraps = mainUiState.allTrap
                    val latestUser = mainUiState.latestUser

                    // 自分の情報の表示
                    Log.d("UserLive", latestUser.toString())
                    if (limitTime == "" && latestUser.relativeTime != "") {
                        viewModel.setLimitTime(latestUser.relativeTime)
                    }
                    tvRelativeTime.text = latestUser.relativeTime

                    if (latestUser.relativeTime != "") {

                        // 制限時間になったかどうかの判定
                        if (limitTime != "") {
                            viewModel.compareTime(latestUser.relativeTime, limitTime)
                            setFragmentResult("MainFragmentLimitTime", bundleOf("limitTime" to limitTime))
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
                                    viewModel.updatePlayerStatus(2)

                                    // TrapにかかったらFragmentを移動
                                    setFragmentResult("MainFragmentTrapTime", bundleOf("trapTime" to latestUser.relativeTime))

                                    findNavController().navigate(R.id.navigation_be_trapped)
                                }
                            }
                        }

                        // Skill Buttonの Progress Bar
                        // スキルボタンを複数回押したとき、relativeが一旦最初の(skillTime+1)秒になって、本来のrelativeまで1秒ずつ足される
                        // observeを二重にしてるせいで変な挙動していると思われる（放置するとメモリやばそう）
                        // この辺ちゃんと仕様わかってないので、リファクタリング時に修正する
                        if (skillTime != "") {
                            viewModel.compareSkillTime(latestUser.relativeTime, skillTime)
                            progressSkill.progress = viewModel.howProgressSkillTime(latestUser.relativeTime, skillTime)
                            setFragmentResult("MainFragmentSkillTime", bundleOf("skillTime" to skillTime))
                        }

                        // URLから画像を取得
                        // 相対時間10秒おきに行う
                        if (latestUser.relativeTime.substring(7, 8) == "0") {
                            Log.d("fetchMAP", "Mapが更新されました")
                            coroutineScope.launch {
                                viewModel.fetchMap(latestUser, allLocation, allTraps)
                            }
                        }
                    }

                    // skillボタンが押された時の処理
                    btSkillOn.setOnClickListener {
                        Log.d("skill button", "skill buttonが呼ばれました")
                        setFragmentResult("MainFragmentSkillTime", bundleOf("skillTime" to mainUiState.latestUser.relativeTime))
                        // 自分の罠をRoomにinsert
                        viewModel.postTrapRoom(0, latestUser)
                        viewModel.postTrapSpacetime("post", latestUser)
                        viewModel.setSkillTime(latestUser)
                        viewModel.setIsOverSkillTime(false)
                    }
                }
            }
        }

        // 捕まったボタンが押された時の処理
        btCaptureOn.setOnClickListener {
            val captureDialogFragment = CaptureDialogFragment()
            val supportFragmentManager = childFragmentManager
            captureDialogFragment.show(supportFragmentManager, "capture")
        }

        return root
    }

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
}

@Composable
fun MainFragmentScreen(onNavigate: (Int) -> (Unit), viewModel: BeTrappedFragmentViewModel = androidx.lifecycle.viewmodel.compose.viewModel(), childFragmentManager: FragmentManager) {
    val beTrappedUiState by viewModel.uiState.collectAsState()

    val latestUser = beTrappedUiState.latestUser
    val skillTime = beTrappedUiState.skillTime

    val howProgressTrap =
        if (latestUser.relativeTime != "") {
            viewModel.howProgressTrapTime(latestUser.relativeTime, "trapTime") / 60f
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
                text = "limitTime",
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
                        val captureDialogFragment = CaptureDialogFragment()
                        captureDialogFragment.show(childFragmentManager, "capture")
                    }
            )
            if (beTrappedUiState.isOverSkillTime) {
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
                            viewModel.postTrapRoom(0, latestUser)
                            viewModel.postTrapSpacetime(latestUser)
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
