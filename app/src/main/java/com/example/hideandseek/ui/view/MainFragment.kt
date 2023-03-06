package com.example.hideandseek.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
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

                    Log.d("UseCaseTest", mainUiState.latestUser.toString())

                    ivMap.setImageBitmap(mainUiState.map)

                    setFragmentResult("MainFragmentIsOverSkillTime", bundleOf("isOverSkillTime" to mainUiState.isOverSkillTime))
                    changeBtSkillVisible(mainUiState.isOverSkillTime)

                    if (mainUiState.isOverLimitTime) {
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
                    val allTraps    = mainUiState.allTrap
                    val latestUser    = mainUiState.latestUser

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
}
