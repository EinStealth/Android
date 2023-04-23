package com.example.hideandseek.ui.view

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        _binding = FragmentRoomTypeSelectBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Viewの取得
        val btCreate: ImageView = binding.btCreate
        val btSearch: ImageView = binding.btSearch
        val textName: TextView = binding.textName
        val editName: EditText = binding.editName
        val userIcon: ImageView = binding.userIcon

        // 名前・アイコンの読み込み
        viewModel.readUserInfo()

        // 名前・アイコンの設定
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    textName.text = it.userName
                    when (it.userIcon) {
                        1 -> {
                            userIcon.setImageResource(R.drawable.user01_normal)
                        }
                        2 -> {
                            userIcon.setImageResource(R.drawable.user02_normal)
                        }
                        3 -> {
                            userIcon.setImageResource(R.drawable.user03_normal)
                        }
                        else -> {
                            userIcon.setImageResource(R.drawable.user04_normal)
                        }
                    }
                }
            }
        }

        // 名前の編集
        textName.setOnClickListener {
            findNavController().navigate(R.id.navigation_register_user_name)
        }
        // アイコンの編集
        userIcon.setOnClickListener {
            findNavController().navigate(R.id.navigation_register_user_icon)
        }

        // 部屋の作成
        btCreate.setOnClickListener {
            findNavController().navigate(R.id.navigation_room_create)
        }
        btSearch.setOnClickListener {
            findNavController().navigate(R.id.navigation_room_search)
        }

        return root
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun RoomTypeSelectScreen() {
    Surface(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.title_background_responsive_nontitlever),
            contentDescription = "background",
            contentScale = ContentScale.Crop
        )
        Column(
            Modifier.fillMaxSize(10f)
        ) {
            UserInfoCard()
            Spacer(Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.button_room_create),
                contentDescription = "button_room_create",
                modifier = Modifier
                    .width(198.dp)
                    .height(98.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.button_room_search),
                contentDescription = "button_room_search",
                modifier = Modifier
                    .width(198.dp)
                    .height(98.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.weight(1f))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun UserInfoCard() {
    Box {
        Image(
            painter = painterResource(R.drawable.user_info_background),
            contentDescription = "user_info_background",
            modifier = Modifier
                .width(300.dp)
                .height(80.dp)
        )
        Row {
            Image(
                painter = painterResource(R.drawable.user01_normal),
                contentDescription = "user_icon",
                modifier = Modifier
                    .width(60.dp)
                    .height(60.dp)
                    .padding(start = 0.dp, top = 10.dp, end = 0.dp, bottom = 0.dp)
            )
            Text(
                text = "username",
                modifier = Modifier.padding(start = 0.dp, top = 25.dp, end = 0.dp, bottom = 0.dp)
            )
        }
    }
}