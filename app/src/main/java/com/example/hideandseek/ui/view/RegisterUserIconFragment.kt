package com.example.hideandseek.ui.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hideandseek.R
import com.example.hideandseek.databinding.FragmentRegisterUserIconBinding
import com.example.hideandseek.ui.viewmodel.RegisterUserNameFragmentViewModel

class RegisterUserIconFragment: Fragment() {
    private var _binding: FragmentRegisterUserIconBinding? = null
    private val viewModel: RegisterUserNameFragmentViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegisterUserIconBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Viewの取得
        val character01: ImageView = binding.character01
        val character02: ImageView = binding.character02
        val character03: ImageView = binding.character03
        val character04: ImageView = binding.character04

        // characterが選択されたときの処理
        character01.setOnClickListener {
            // アイコンの保存
            val sharedPref = activity?.getSharedPreferences("user_info", Context.MODE_PRIVATE)
            with (sharedPref?.edit()) {
                this?.putInt("icon", 1)
                this?.apply()
            }
            findNavController().navigate(R.id.navigation_room_type_select)
        }
        character02.setOnClickListener {
            // アイコンの保存
            val sharedPref = activity?.getSharedPreferences("user_info", Context.MODE_PRIVATE)
            with (sharedPref?.edit()) {
                this?.putInt("icon", 2)
                this?.apply()
            }
            findNavController().navigate(R.id.navigation_room_type_select)
        }
        character03.setOnClickListener {
            // アイコンの保存
            val sharedPref = activity?.getSharedPreferences("user_info", Context.MODE_PRIVATE)
            with (sharedPref?.edit()) {
                this?.putInt("icon", 3)
                this?.apply()
            }
            findNavController().navigate(R.id.navigation_room_type_select)
        }
        character04.setOnClickListener {
            // アイコンの保存
            val sharedPref = activity?.getSharedPreferences("user_info", Context.MODE_PRIVATE)
            with (sharedPref?.edit()) {
                this?.putInt("icon", 4)
                this?.apply()
            }
            findNavController().navigate(R.id.navigation_room_type_select)
        }

        return root
    }
}
