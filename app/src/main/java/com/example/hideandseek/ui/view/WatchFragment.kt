package com.example.hideandseek.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.hideandseek.R
import com.example.hideandseek.databinding.FragmentWatchBinding
import com.example.hideandseek.ui.viewmodel.StandByRoomFragmentViewModel
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
                    val allTraps = watchUiState.allTrap
                    val latestUser = watchUiState.latestUser

                    // 自分の情報の表示
                    Log.d("UserLive", latestUser.toString())
                    if (latestUser.relativeTime != "") {
                        // 他人の位置を追加
                        Log.d("ALL_Location", allLocation.toString())
                        if (allLocation.isNotEmpty()) {
                            // ユーザーの位置情報
                            for (i in allLocation.indices) {
                                if (allLocation[i].status == 1) {
                                    viewModel.postTrapRoom(1, latestUser)
                                }
                            }
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
                }
            }
        }

        return root
    }
}

@Composable
fun WatchScreen(onNavigate: (Int) -> (Unit), viewModel: WatchFragmentViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val watchUiState by viewModel.uiState.collectAsState()

    Surface(Modifier.fillMaxSize()) {
        watchUiState.map?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "map",
                contentScale = ContentScale.Crop
            )
        }
        ConstraintLayout {
            // Create references for the composable to constrain
            val (ivWatching, btCaptureOff, btSkillOf, user1, user2, user3, user4) = createRefs()

            Image(
                painter = painterResource(R.drawable.text_watching),
                contentDescription = "text_watching",
                modifier = Modifier
                    .constrainAs(ivWatching) {
                        end.linkTo(parent.end)
                        bottom.linkTo(btCaptureOff.top)
                        start.linkTo(parent.start)
                    }
                    .height(80.dp)
            )
            Image(
                painter = painterResource(R.drawable.button_captured_off),
                contentDescription = "鬼に捕まったときに押すボタン",
                modifier = Modifier
                    .constrainAs(btCaptureOff) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
                    .height(100.dp)
                    .width(200.dp)
            )
            Image(
                painter = painterResource(R.drawable.button_skill_off),
                contentDescription = "skill button",
                modifier = Modifier
                    .constrainAs(btSkillOf) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .height(100.dp)
                    .width(180.dp)
            )
            Image(
                painter = painterResource(R.drawable.user01_caputure),
                contentDescription = "user1",
                modifier = Modifier
                    .constrainAs(user1) {
                        top.linkTo(parent.top)
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
fun WatchPreview() {
    Surface(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.title_background_responsive_nontitlever),
            contentDescription = "map",
            contentScale = ContentScale.Crop
        )
        ConstraintLayout {
            // Create references for the composable to constrain
            val (ivWatching, btCaptureOff, btSkillOf, user1, user2, user3, user4) = createRefs()

            Image(
                painter = painterResource(R.drawable.text_watching),
                contentDescription = "text_watching",
                modifier = Modifier
                    .constrainAs(ivWatching) {
                        end.linkTo(parent.end)
                        bottom.linkTo(btCaptureOff.top)
                        start.linkTo(parent.start)
                    }
                    .height(80.dp)
            )
            Image(
                painter = painterResource(R.drawable.button_captured_off),
                contentDescription = "鬼に捕まったときに押すボタン",
                modifier = Modifier
                    .constrainAs(btCaptureOff) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
                    .height(100.dp)
                    .width(200.dp)
            )
            Image(
                painter = painterResource(R.drawable.button_skill_off),
                contentDescription = "skill button",
                modifier = Modifier
                    .constrainAs(btSkillOf) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .height(100.dp)
                    .width(180.dp)
            )
            Image(
                painter = painterResource(R.drawable.user01_caputure),
                contentDescription = "user1",
                modifier = Modifier
                    .constrainAs(user1) {
                        top.linkTo(parent.top)
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
