package com.example.hideandseek.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hideandseek.R
import com.example.hideandseek.databinding.FragmentStartBinding

class StartFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                StartScreen(onNavigate = { dest -> findNavController().navigate(dest)})
            }
        }
//        _binding = FragmentStartBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        // Viewの取得
//        val background: ImageView = binding.background
//
//        // 名前の読み込み
//        val sharedPref = activity?.getSharedPreferences("user_info", Context.MODE_PRIVATE)
//        val name = sharedPref?.getString("name", "")
//
//        // 画面が押されたときの処理
//        background.setOnClickListener {
//            Log.d("StartFragment", name.toString())
//            if (name != null && name.toString() != "") {
//                findNavController().navigate(R.id.navigation_room_type_select)
//            } else {
//                findNavController().navigate(R.id.navigation_register_user_name)
//            }
//        }
//
//        return root
    }
}

@Composable
fun StartScreen(onNavigate: (Int) -> (Unit)) {
    Column(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.title_background_responsive),
            contentDescription = "title",
            contentScale = ContentScale.Crop,
            modifier = Modifier.clickable {
                onNavigate(R.id.navigation_room_type_select)
            }
        )
    }
}