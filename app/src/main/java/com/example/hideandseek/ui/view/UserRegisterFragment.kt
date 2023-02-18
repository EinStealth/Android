package com.example.hideandseek.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hideandseek.R
import com.example.hideandseek.databinding.FragmentUserRegisterBinding
import com.example.hideandseek.ui.viewmodel.UserRegisterFragmentViewModel

class UserRegisterFragment: Fragment() {
    private var _binding: FragmentUserRegisterBinding? = null
    private val viewModel: UserRegisterFragmentViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUserRegisterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Viewの取得
        val btDecide: ImageView = binding.btDecide
        val editName: EditText = binding.editName

        // 決定ボタンが押されたら名前を次のフラグメントに送る
        btDecide.setOnClickListener {
            setFragmentResult("UserRegisterFragmentName", bundleOf("name" to editName.text.toString()))
            Log.d("nameRegisterTest", editName.text.toString())
            findNavController().navigate(R.id.navigation_main)
        }

        return root
    }
}