package com.example.hideandseek.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.media.Image
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.hideandseek.R
import com.example.hideandseek.databinding.FragmentRoomTypeSelectBinding
import com.example.hideandseek.ui.viewmodel.RoomTypeSelectFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RoomTypeSelectFragment: Fragment() {
    private var _binding: FragmentRoomTypeSelectBinding? = null
    private val viewModel: RoomTypeSelectFragmentViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                RoomTypeSelectScreen(onNavigate = { dest -> findNavController().navigate(dest) })
            }
        }
//        _binding = FragmentRoomTypeSelectBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        /
//
//        // 名前の編集
//        textName.setOnClickListener {
//            findNavController().navigate(R.id.navigation_register_user_name)
//        }
//        // アイコンの編集
//        userIcon.setOnClickListener {
//            findNavController().navigate(R.id.navigation_register_user_icon)
//        }
//        return root
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun RoomTypeSelectScreen(onNavigate: (Int) -> (Unit), viewModel: RoomTypeSelectFragmentViewModel = viewModel()) {
    // 名前・アイコンの読み込み
    viewModel.readUserInfo()

    Surface(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.title_background_responsive_nontitlever),
            contentDescription = "background",
            contentScale = ContentScale.Crop
        )
        Column(
            Modifier.fillMaxSize(10f)
        ) {
            Box {
                Image(
                    painter = painterResource(R.drawable.user_info_background),
                    contentDescription = "user_info_background",
                    modifier = Modifier
                        .width(300.dp)
                        .height(80.dp)
                        .clickable {
                            onNavigate(R.id.navigation_register_user_icon)
                        }
                )
                Row {
                    Image(
                        painter = when (viewModel.uiState.value.userIcon) {
                            1 -> {
                                painterResource(R.drawable.user01_normal)
                            }
                            2 -> {
                                painterResource(R.drawable.user02_normal)
                            }
                            3 -> {
                                painterResource(R.drawable.user03_normal)
                            }
                            else -> {
                                painterResource(R.drawable.user04_normal)
                            }
                        },
                        contentDescription = "user_icon",
                        modifier = Modifier
                            .width(60.dp)
                            .height(60.dp)
                            .padding(start = 0.dp, top = 10.dp, end = 0.dp, bottom = 0.dp)
                    )
                    Text(
                        text = viewModel.uiState.value.userName,
                        modifier = Modifier.padding(start = 0.dp, top = 25.dp, end = 0.dp, bottom = 0.dp)
                            .clickable {
                                onNavigate(R.id.navigation_register_user_name)
                            }
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.button_room_create),
                contentDescription = "button_room_create",
                modifier = Modifier
                    .width(198.dp)
                    .height(98.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable { onNavigate(R.id.navigation_room_create) }
            )
            Spacer(Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.button_room_search),
                contentDescription = "button_room_search",
                modifier = Modifier
                    .width(198.dp)
                    .height(98.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable { onNavigate(R.id.navigation_room_search) }
            )
            Spacer(Modifier.weight(1f))
        }
    }
}