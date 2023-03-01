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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.hideandseek.R
import com.example.hideandseek.databinding.FragmentRegisterUserNameBinding
import com.example.hideandseek.ui.viewmodel.RegisterUserNameFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterUserNameFragment: Fragment() {
    private var _binding: FragmentRegisterUserNameBinding? = null
    private val viewModel: RegisterUserNameFragmentViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegisterUserNameBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Viewの取得
        val btDecide: ImageView = binding.btDecide
        val editName: EditText = binding.editName
        var isEdit = false

        // 既に名前が保存されているか確認する
        viewModel.readUserInfo()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    isEdit = it.isEdit
                }
            }
        }

        // 決定ボタンが押されたら名前を次のフラグメントに送る
        btDecide.setOnClickListener {
            // 名前の保存
            viewModel.writeUserName(editName.text.toString())

            if (isEdit) {
                findNavController().navigate(R.id.navigation_room_type_select)
            } else {
                findNavController().navigate(R.id.navigation_register_user_icon)
            }
        }

        return root
    }
}
