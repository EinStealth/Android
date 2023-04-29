package com.example.hideandseek.ui.view

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hideandseek.ui.viewmodel.MainActivityViewModel
import com.example.hideandseek.ui.viewmodel.RegisterUserIconFragmentViewModel
import com.example.hideandseek.ui.viewmodel.RegisterUserNameFragmentViewModel
import com.example.hideandseek.ui.viewmodel.RoomTypeSelectFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApp()
        }

        // フルスクリーンにするために、TitleBarなどを隠す
        hideSystemUI()

        // 位置情報の権限をリクエスト
        // 正確な位置情報、おおよその位置情報どちらを許可しますか？というダイアログが出る。
        // どちらか選んで許可すれば、選ばれたもの、許可されなければ権限はなし
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted
                } else -> {
                    // No location access granted
                }
            }
        }

        // リクエストを送る
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ),
        )

        // データベースの初期化
        viewModel.deleteAllTrap()
        viewModel.deleteAllLocation()
    }

    private fun hideSystemUI() {
        supportActionBar?.hide()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController?.hide(
                WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars()
            )
            window.decorView.windowInsetsController?.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "registerName") {
        composable("start") {
            StartScreen(navController)
        }
        composable("registerName") {
            val viewModel = hiltViewModel<RegisterUserNameFragmentViewModel>()
            RegisterUserNameScreen(viewModel, navController)
        }
        composable("registerIcon") {
            val viewModel = hiltViewModel<RegisterUserIconFragmentViewModel>()
            RegisterUserIconScreen(viewModel)
        }
        composable("roomTypeSelect") {
            val viewModel = hiltViewModel<RoomTypeSelectFragmentViewModel>()
            RoomTypeSelectScreen(viewModel)
        }
    }
}
