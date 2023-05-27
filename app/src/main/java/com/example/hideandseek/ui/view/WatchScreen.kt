package com.example.hideandseek.ui.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hideandseek.R
import com.example.hideandseek.data.datasource.remote.ResponseData
import com.example.hideandseek.ui.viewmodel.WatchFragmentViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private fun selectDrawable(icon: Int, status: Int): Int {
    val list: List<List<Int>> = listOf(
        listOf(R.drawable.user01_runaway, R.drawable.user01_caputure, R.drawable.user01_clear, R.drawable.user01_oni),
        listOf(R.drawable.user02_runaway, R.drawable.user02_caputure, R.drawable.user02_clear, R.drawable.user02_oni),
        listOf(R.drawable.user03_runaway, R.drawable.user03_capture, R.drawable.user03_clear, R.drawable.user03_oni),
        listOf(R.drawable.user04_runaway, R.drawable.user04_capture, R.drawable.user04_clear, R.drawable.user04_oni),
    )
    return list[icon - 1][status % 10]
}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun WatchScreen(viewModel: WatchFragmentViewModel = androidx.lifecycle.viewmodel.compose.viewModel(), mainDispatcher: CoroutineDispatcher = Dispatchers.Main) {
    val watchUiState by viewModel.uiState.collectAsState()

    val coroutineScope = CoroutineScope(mainDispatcher)

    val allLocation = watchUiState.allLocation
    val allTraps = watchUiState.allTrap
    val latestUser = watchUiState.latestUser
    val allPlayer = watchUiState.allPlayer

    // 自分の情報の表示
    Log.d("UserLive", latestUser.toString())
    if (latestUser.relativeTime != "") {
        if (watchUiState.map == null) {
            // URLから画像を取得
            // 相対時間10秒おきに行う
            Log.d("fetchMAP", "Mapが更新されました")
            coroutineScope.launch {
                viewModel.fetchMap(latestUser, allLocation, allTraps)
            }
        }

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
        if (watchUiState.preRelativeTime == "") {
            viewModel.updatePreRelativeTime()
        } else {
            Log.d("fetchMap", "re: ${latestUser.relativeTime}, pre: ${watchUiState.preRelativeTime}")
            if (latestUser.relativeTime.substring(6, 7) != watchUiState.preRelativeTime.substring(6, 7)) {
                Log.d("fetchMAP", "Mapが更新されました")
                coroutineScope.launch {
                    viewModel.fetchMap(latestUser, allLocation, allTraps)
                }
                viewModel.updatePreRelativeTime()
            }
        }
    }

    WatchLayout(
        map = watchUiState.map,
        allPlayer = allPlayer,
    )
}

@Composable
private fun WatchLayout(
    map: Bitmap?,
    allPlayer: List<ResponseData.ResponseGetPlayer>,
) {
    Surface(Modifier.fillMaxSize()) {
        map?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
        Column {
            LazyRow {
                items(allPlayer) {
                    Image(
                        painter = painterResource(id = selectDrawable(it.icon, it.status)),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 28.dp)
                            .size(width = 72.dp, height = 72.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.text_watching),
                contentDescription = null,
                modifier = Modifier
                    .height(80.dp)
            )
            Row {
                Image(
                    painter = painterResource(R.drawable.button_captured_off),
                    contentDescription = null,
                    modifier = Modifier
                        .size(width = 200.dp, height = 100.dp)
                )
                Image(
                    painter = painterResource(R.drawable.button_skill_off),
                    contentDescription = null,
                    modifier = Modifier
                        .size(width = 180.dp, height = 100.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun WatchPreview() {
    WatchLayout(
        map = null,
        allPlayer = listOf(
            ResponseData.ResponseGetPlayer("", "", 1, 1),
            ResponseData.ResponseGetPlayer("", "", 1, 1),
            ResponseData.ResponseGetPlayer("", "", 1, 1),
            ResponseData.ResponseGetPlayer("", "", 1, 1),
        ),
    )
}
