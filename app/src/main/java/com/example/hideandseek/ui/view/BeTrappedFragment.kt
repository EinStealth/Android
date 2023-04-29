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
import androidx.navigation.fragment.findNavController
import com.example.hideandseek.R
import com.example.hideandseek.databinding.FragmentBeTrappedBinding
import com.example.hideandseek.ui.viewmodel.BeTrappedFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// 2重LiveData解消のために変数定義
private var limitTime = ""
private var trapTime = ""

@AndroidEntryPoint
class BeTrappedFragment : Fragment() {
    private var _binding: FragmentBeTrappedBinding? = null
    private val viewModel: BeTrappedFragmentViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBeTrappedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Viewの取得
        // 時間表示の場所
        val tvRelativeTime: TextView = binding.tvRelativeTime
        val tvLimitTime: TextView = binding.tvLimitTime

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

        // Trapが解除されるまでのプログレスバー
        val progressTrap: ProgressBar = binding.progressTrap
        progressTrap.max = 60

        setFragmentResultListener("MainFragmentLimitTime") { _, bundle ->
            val result = bundle.getString("limitTime")
            Log.d("limitTimeResultListener", result.toString())
            if (result != null) {
                limitTime = result
            }
        }

        setFragmentResultListener("MainFragmentTrapTime") { _, bundle ->
            val result = bundle.getString("trapTime")
            Log.d("trapTimeResultListener", result.toString())
            if (result != null) {
                trapTime = result
            }
        }

        setFragmentResultListener("MainFragmentSkillTime") { _, bundle ->
            val result = bundle.getString("skillTime")
            Log.d("skillTimeResultListener", result.toString())
            if (result != null) {
                viewModel.setSkillTimeInit(result)
            }
        }

        setFragmentResultListener("MainFragmentIsOverSkillTime") { _, bundle ->
            val result = bundle.getBoolean("isOverSkillTime")
            Log.d("isOverSKillTimeResultListener", result.toString())
            viewModel.setIsOverSkillTime(result)
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { beTrappedUiState ->
                    val skillTime = beTrappedUiState.skillTime

                    // Skillを使ってからの時間を監視
                    setFragmentResult("BeTrappedFragmentIsOverSkillTime", bundleOf("isOverSkillTime" to beTrappedUiState.isOverSkillTime))
                    changeBtSkillVisible(beTrappedUiState.isOverSkillTime)

                    // 罠にかかっている時間の監視
                    if (beTrappedUiState.isOverTrapTime) {
                        findNavController().navigate(R.id.navigation_main)
                    }

                    // 制限時間と相対時間の監視
                    if (beTrappedUiState.isOverLimitTime) {
                        // クリアダイアログを表示
                        val successEscapeDialogFragment = SuccessEscapeDialogFragment()
                        val supportFragmentManager = childFragmentManager
                        successEscapeDialogFragment.show(supportFragmentManager, "clear")
                    }

                    val latestUser = beTrappedUiState.latestUser

                    Log.d("UserLive", latestUser.toString())
                    if (latestUser.relativeTime != "") {
                        tvRelativeTime.text = latestUser.relativeTime
                        // 制限時間になったかどうかの判定
                        viewModel.compareTime(latestUser.relativeTime, limitTime)

                        // trapにかかっている時間を計測
                        viewModel.compareTrapTime(latestUser.relativeTime, trapTime)
                        val howProgressTrap = viewModel.howProgressTrapTime(latestUser.relativeTime, trapTime)
                        progressTrap.progress = howProgressTrap

                        // Skill Buttonの Progress Bar
                        // スキルボタンを複数回押したとき、relativeが一旦最初の(skillTime+1)秒になって、本来のrelativeまで1秒ずつ足される
                        // observeを二重にしてるせいで変な挙動していると思われる（放置するとメモリやばそう）
                        // この辺ちゃんと仕様わかってないので、リファクタリング時に修正する
                        if (skillTime != "") {
                            viewModel.compareSkillTime(
                                latestUser.relativeTime,
                                skillTime,
                            )
                            progressSkill.progress = viewModel.howProgressSkillTime(
                                latestUser.relativeTime,
                                skillTime,
                            )
                            setFragmentResult("BeTrappedFragmentSkillTime", bundleOf("skillTime" to skillTime))
                        }

                        // skillボタンが押された時の処理
                        btSkillOn.setOnClickListener {
                            setFragmentResult("BeTrappedFragmentSkillTime", bundleOf("skillTime" to latestUser.relativeTime))
                            viewModel.postTrapRoom(0, latestUser)
                            viewModel.postTrapSpacetime(latestUser)
                            viewModel.setSkillTime(latestUser)
                            viewModel.setIsOverSkillTime(false)
                        }
                    }
                }
            }
        }

        return ComposeView(requireContext()).apply {
            setContent {
                BeTrappedScreen(
                    onNavigate = { dest -> findNavController().navigate(dest) },
                    childFragmentManager = childFragmentManager
                )
            }
        }
    }
}

@Composable
fun BeTrappedScreen(onNavigate: (Int) -> (Unit), viewModel: BeTrappedFragmentViewModel = androidx.lifecycle.viewmodel.compose.viewModel(), childFragmentManager: FragmentManager) {
    val beTrappedUiState by viewModel.uiState.collectAsState()

    val latestUser = beTrappedUiState.latestUser
    val howProgressTrap =
        if (latestUser.relativeTime != "" && trapTime != "") {
            viewModel.howProgressTrapTime(latestUser.relativeTime, trapTime)/60f
        } else {
            0f
        }


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
                        val captureDialogFragment = CaptureDialogFragment()
                        captureDialogFragment.show(childFragmentManager, "capture")
                    }
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
                progress = 0f,
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

@Composable
@Preview
fun BeTrappedPreview() {
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
