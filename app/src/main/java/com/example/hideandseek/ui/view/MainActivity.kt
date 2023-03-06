package com.example.hideandseek.ui.view

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hideandseek.R
import com.example.hideandseek.data.repository.MyLocationRepository
import com.example.hideandseek.databinding.ActivityMainBinding
import com.example.hideandseek.ui.viewmodel.MainActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // fragmentのnavGraph
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_register_user_name,
                R.id.navigation_main,
                R.id.navigation_be_trapped,
                R.id.navigation_result,
                R.id.navigation_watch,
                R.id.navigation_room_type_select,
                R.id.navigation_start,

            ),
        )

        // フルスクリーンにするために、TitleBarなどを隠す
        hideSystemUI()

        // BottomNavigationのセットアップ
        setupActionBarWithNavController(navController, appBarConfiguration)

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
