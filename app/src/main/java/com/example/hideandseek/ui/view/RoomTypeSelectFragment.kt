package com.example.hideandseek.ui.view

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

        setFragmentResultListener("UserRegisterFragmentName") { _, bundle ->
            val result = bundle.getString("name")
            textName.text = result.toString()
        }

        btCreate.setOnClickListener {
            setFragmentResult("RoomTypeSelectFragment", bundleOf("name" to textName.text.toString()))
            findNavController().navigate(R.id.navigation_room_create)
        }
        btSearch.setOnClickListener {
            // TODO: 部屋のリストを表示する画面に移動
        }

        return root
    }
}