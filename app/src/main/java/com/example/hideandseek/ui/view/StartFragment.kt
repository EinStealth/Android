package com.example.hideandseek.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hideandseek.R
import com.example.hideandseek.databinding.FragmentStartBinding
import com.example.hideandseek.databinding.FragmentUserRegisterBinding
import com.example.hideandseek.ui.viewmodel.UserRegisterFragmentViewModel

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

        // 画面が押されたときの処理
        background.setOnClickListener {
            findNavController().navigate(R.id.navigation_user_register)
        }

        return root
    }
}