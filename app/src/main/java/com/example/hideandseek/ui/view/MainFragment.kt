package com.example.hideandseek.ui.view

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
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

        // 画面サイズの取得
        var width: Int? = 100
        var height: Int? = 100
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            width = activity?.windowManager?.currentWindowMetrics?.bounds?.width()?.div(4)?.plus(10)
            height = activity?.windowManager?.currentWindowMetrics?.bounds?.height()?.div(4)
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

                        // 自分の位置情報のurl
                        val iconUrlHide = "https://lh3.googleusercontent.com/fTB0kRIlKDrQjTC1tiQC4VNIxcfRCKkuWLQec-sqIvtDrZhQ_kLS5PC5MILszDwMED_-kHW4vuuRQM71SLQnAqHnkboIeiFA69ws5xzuEMmOdy3NcsIFxSpIIBYYe7-9MkXX9Zpful9GfZ-_Ufvda5hrEMjTRKOdhg9fHtlRfdA5-SxXeUcl_bw1xOPj5Jg7pI-A0f1soN1G2wsbXmWlIk2F8zQ6_6_zaQ0WQPkaXShsnRXZJNPB078BZNOpzETx1KdvwCbH2uqnHPLLkiQBCvAnSbt-FHc_Y7UTZZ8KHCG6KIdnnwxfQlZn4joJaO5dJpC9kQMZoUn1cs_sJ-PF5h5PDOA26s-CKwgxifSaqVlmctqPupXmXoXfduLNaJm_z9CILnWAqCV6U5NOf8NZV0waqPQmhenk_8ANc2fNtgltz4--JxolPTz27PhRrXk4rADQEVLEJ1AWfpmWF93R94rkeDhKo39RCpndm52cWA0oqC6x_aWQj6F9KTukrb_nYyNZqUt14nWYUc8dzU22GYirIJcQBDHG4yLy8pv7DUuzd140emlx4SMBv4p919ubZi6qUc9gNPzRPql0BixKgGrwLg0SXCwyKUhM6a5WS-vBBAyCTSJCybfQ2QfsMGmCNuB437B8G1e9i581VBGymHLPBxq0vHPTrilYrag3EZWhjrLnIShW4h7coRf6DUdgZb-UyZsrQzhtV27R_dtXjfiNFggmRsDLh5jJgLQ1GHb_qZBistDosQ8da7wsLi0pQiKwfr68G_MTncpARwGREhjLeAuh1vPC2JFm7Oc7KIUtGOgCErB6u44Skbf-_w9tapn1uyFRqUlnMk0Rqww6sT5_dndLgXMo17KI893xKbA3VDXMmO4NtfCOH4mjdMPVBs8rtoE9yVnjRi0JhLoFAymituMyycwFC3aQq-Zejg6c16EkxUGS9oB0nKbeEcA-AqET8eaVJeKKx_hxmHqEagXA1a2bT6TokgE6WRyBUXjqu1eZaksGww6gG4UfaoSX9vYIGWG5PrugqMlzMV-XE27ddRh9Be6kf3X7ToEe7nF7CS8p7vV-BfaX3ms=s32-no?authuser=0"
                        var url = "https://maps.googleapis.com/maps/api/staticmap" +
                                "?center=${latestUser.latitude},${latestUser.longitude}" +
                                "&size=${width}x${height}&scale=1" +
                                "&zoom=18" +
                                "&key=AIzaSyA-cfLegBoleKaT2TbU5R4K1uRkzBR6vUQ" +
                                "&markers=icon:" + iconUrlHide + "|${latestUser.latitude},${latestUser.longitude}"

                        // 他人の位置を追加
                        Log.d("ALL_Location", allLocation.toString())
                        if (allLocation.isNotEmpty()) {
                            // ユーザーの位置情報
                            for (i in allLocation.indices) {
                                when (allLocation[i].objId) {
                                    1 -> {
                                        // 他人の罠をRoomにinsert
                                        val trap = TrapData(0, allLocation[i].latitude, allLocation[i].longitude, allLocation[i].altitude, 1)
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
                                    else -> {
                                        url += "&markers=icon:" + iconUrlHide + "|${allLocation[i].latitude},${allLocation[i].longitude}"
                                    }
                                }
                            }
                        }
                        Log.d("All_trap", allTraps.toString())

                        // trapの位置情報
                        if (allTraps.isNotEmpty()) {
                            for (i in allTraps.indices) {
                                if (allTraps[i].objId == 0) {
                                    val iconUrlTrap = "https://lh3.googleusercontent.com/6B1VLkjCpGk8BtAZfVfpSHxLlfHi1UI0Pry1DdgCi7PPrNr1846g818b2Qq1SjT6_-ntECnAFgQEsmEDT7x4LDC5Tgxhk9P9KwoyAFWnI2G6J2FIFRNfjDgMZe83SXZr4FK0lzsOO4VpcQD-bWRiLJ9NFx_Xk4qR68NihN7ZU9zm8c4Kopy7KrsZZLTOlasbLauicoLB3Et98RDiI23q79h6i1Ui-l8mqieUH29mTfOistvaLbVx8S0RuNW1qKlm_LJdW8btMli0SN9BUao_IwuHXf0CbPv2gCxJDK7pz4RoKEz3WYLnEZG-tC12UQrWIE8b4YA7EwHqOCXiePmRITZqAU-4_8HjNrN-ZZJd12nov_FjMWRLtaDk6lU3ZMctlBlSz-_0ep63SZJ9Gbo59mwV1VhM7ZrWj648fMX_l_15W4Si7W7t1BdcjkIOKAL1ORdQ1A-51OgVNjdBoucUip7U2I20vJQ1SlWjfNs2bb0fxO3ue8kEMko1RlbVgYyYBTWf2CkUJXPA05MSCSRYZ8-7goG01K2dsL-az12K664_zQF0_E5X2zR5X7BqiYC_bV2uYIVYyYn4geLDa-cxVmPFVA6AjyTfG8SpEoPoZwRTZ648hdkbYi7dnaYHEzDNyTxdCnzfi620RfQKMuLsKF5vhIIaSfHhmWPpy_ADHNezNH2oHHWgYmT-kuJ2HIR99MHvOlEsU30NGh-bNfkfWqmdoGFivR8IoQqzaaA-wZu23SyaFsXZ9Z4WTRWqhXRwrrGNqCByDiZgqiFUDpnvsaQ-dqe33HGduap7vYnbikhyMsBql-gShf53hr78-bTouQeVweWXc5xNPBXRl4FXsxyt-L7VlJPSwSu6eJzuQBzrAjLGLZdmwF-U03Sx1k-dl5_X2EE0b3CXkvywms9v0sCb9m4gDbIUQXZ9g3stn31ftdsF4SHaAgmoLIo7PCDosnRwf5AkcrkK-VSiByb8dyjVCzvZnfkWLN5eMDPHO2P36nLWMO81lA2xwUlYbTtodPA3E9IDd2oDQQLZJJxMsURqZTltaSDJK4dmcj-RGmzscfXISTTRmV8VIsY=w24-h33-no?authuser=0"
                                    url += "&markers=icon:$iconUrlTrap|${allTraps[i].latitude},${allTraps[i].longitude}"
                                }
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
                                viewModel.fetchMap(url)
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
