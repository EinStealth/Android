package com.example.hideandseek.ui.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hideandseek.R
import com.example.hideandseek.databinding.FragmentStartBinding

class StartFragment: Fragment() {
    private var _binding: FragmentStartBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Viewの取得
        val background: ImageView = binding.background

        // 名前の読み込み
        val sharedPref = activity?.getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val name = sharedPref?.getString("name", "")

        // 画面が押されたときの処理
        background.setOnClickListener {
            Log.d("StartFragment", name.toString())
            if (name != null && name.toString() != "") {
                findNavController().navigate(R.id.navigation_room_type_select)
            } else {
                findNavController().navigate(R.id.navigation_register_user_name)
            }
        }

        return root
    }
}