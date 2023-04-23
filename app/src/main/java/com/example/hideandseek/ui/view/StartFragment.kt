package com.example.hideandseek.ui.view

import android.content.Context
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

class StartFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // 名前の読み込み
        val sharedPref = activity?.getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val name = sharedPref?.getString("name", "")

        return ComposeView(requireContext()).apply {
            setContent {
                StartScreen(
                    onNavigate = { dest -> findNavController().navigate(dest)},
                    name = name.toString()
                )
            }
        }
    }
}

@Composable
fun StartScreen(onNavigate: (Int) -> (Unit), name: String) {
    Column(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.title_background_responsive),
            contentDescription = "title",
            contentScale = ContentScale.Crop,
            modifier = Modifier.clickable {
                if (name != "") {
                    onNavigate(R.id.navigation_room_type_select)
                } else {
                    onNavigate(R.id.navigation_register_user_name)
                }
            }
        )
    }
}