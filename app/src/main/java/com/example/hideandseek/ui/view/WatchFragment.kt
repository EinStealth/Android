package com.example.hideandseek.ui.view

import android.os.Build
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

        // 画面サイズの取得
        var width: Int? = 100
        var height: Int? = 100
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            width = activity?.windowManager?.currentWindowMetrics?.bounds?.width()?.div(4)?.plus(10)
            height = activity?.windowManager?.currentWindowMetrics?.bounds?.height()?.div(4)
        }

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
                        // 他人の位置を追加
                        Log.d("ALL_Location", allLocation.toString())
                        if (allLocation.isNotEmpty()) {
                            // ユーザーの位置情報
                            for (i in allLocation.indices) {
                                if (allLocation[i].objId == 1) {
                                    viewModel.postTrapRoom(1, latestUser)
                                }
                            }
                        }

                        // URLから画像を取得
                        // 相対時間10秒おきに行う
                        if (latestUser.relativeTime.substring(7, 8) == "0") {
                            Log.d("fetchMAP", "Mapが更新されました")
                            coroutineScope.launch {
                                if (width != null && height != null) {
                                    viewModel.fetchMap(latestUser, width, height, allLocation, allTraps)
                                }
                            }
                        }
                    }
                }
            }
        }

        return root
    }
}
