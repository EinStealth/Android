package com.example.hideandseek.ui.view

import android.content.Context
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hideandseek.R
import com.example.hideandseek.databinding.FragmentRoomTypeSelectBinding
import com.example.hideandseek.ui.viewmodel.RoomTypeSelectFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

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
        val userIcon: ImageView = binding.userIcon

        // 名前の読み込み
        val sharedPref = activity?.getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val name = sharedPref?.getString("name", "")
        val icon = sharedPref?.getInt("icon", 0)

        textName.text = name
        when (icon) {
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

        btCreate.setOnClickListener {
            findNavController().navigate(R.id.navigation_room_create)
        }
        btSearch.setOnClickListener {
            // TODO: 部屋のリストを表示する画面に移動
        }

        return root
    }
}