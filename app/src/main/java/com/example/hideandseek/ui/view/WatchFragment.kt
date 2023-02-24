package com.example.hideandseek.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.hideandseek.databinding.FragmentWatchBinding
import com.example.hideandseek.ui.viewmodel.WatchFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WatchFragment(
    mainDispatcher: CoroutineDispatcher = Dispatchers.Main
) : Fragment() {
    private var _binding: FragmentWatchBinding? = null
    private val viewModel: WatchFragmentViewModel by viewModels()

    private val coroutineScope = CoroutineScope(mainDispatcher)

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWatchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Viewの取得
        // Map
        val ivMap: ImageView = binding.ivMap

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { watchUiState ->

                    // Mapに画像をセット
                    ivMap.setImageBitmap(watchUiState.map)

                    Log.d("UiState", "stateを更新しました")
                    val allLocation = watchUiState.allLocation
                    val allTraps    = watchUiState.allTrap
                    val latestUser  = watchUiState.latestUser

                    // 自分の情報の表示
                    Log.d("UserLive", latestUser.toString())
                    if (latestUser.relativeTime != "") {
                        // 自分の位置情報のurl
                        val iconUrlHide = "https://onl.bz/dcMZVEa"
                        var url = "https://maps.googleapis.com/maps/api/staticmap" +
                                "?center=${latestUser.latitude},${latestUser.longitude}" +
                                "&size=310x640&scale=1" +
                                "&zoom=18" +
                                "&key=AIzaSyA-cfLegBoleKaT2TbU5R4K1uRkzBR6vUQ" +
                                "&markers=icon:" + iconUrlHide + "|${latestUser.latitude},${latestUser.longitude}"

                        // 他人の位置を追加
                        Log.d("ALL_Location", allLocation.toString())
                        if (allLocation.isNotEmpty()) {
                            // ユーザーの位置情報
                            for (i in allLocation.indices) {
                                if (allLocation[i].objId == 1) {
                                    viewModel.postTrapRoom(1, latestUser)
                                } else {
                                    url += "&markers=icon:" + iconUrlHide + "|${allLocation[i].latitude},${allLocation[i].longitude}"
                                }
                            }
                        }

                        // trapの位置情報
                        if (allTraps.isNotEmpty()) {
                            for (i in allTraps.indices) {
                                if (allTraps[i].objId == 0) {
                                    url += "&markers=icon:https://onl.bz/FetpS7Y|${allTraps[i].latitude},${allTraps[i].longitude}"
                                }
                            }
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
                }
            }
        }

        return root
    }
}
