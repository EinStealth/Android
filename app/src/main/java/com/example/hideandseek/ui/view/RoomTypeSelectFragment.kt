package com.example.hideandseek.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.hideandseek.R
import com.example.hideandseek.databinding.FragmentRoomTypeSelectBinding
import com.example.hideandseek.ui.viewmodel.RoomTypeSelectFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RoomTypeSelectFragment : Fragment() {
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
