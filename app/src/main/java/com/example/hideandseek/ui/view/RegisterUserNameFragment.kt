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
import com.example.hideandseek.databinding.FragmentRegisterUserNameBinding
import com.example.hideandseek.ui.viewmodel.RegisterUserNameFragmentViewModel

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

        // 決定ボタンが押されたら名前を次のフラグメントに送る
        btDecide.setOnClickListener {
            // 名前の保存
            val sharedPref = activity?.getSharedPreferences("user_info", Context.MODE_PRIVATE)
            with (sharedPref?.edit()) {
                this?.putString("name", editName.text.toString())
                this?.apply()
            }

            findNavController().navigate(R.id.navigation_register_user_icon)
        }

        return root
    }
}
