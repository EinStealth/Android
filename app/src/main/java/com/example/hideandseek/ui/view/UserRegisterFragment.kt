package com.example.hideandseek.ui.view

import android.content.Context
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
            // 名前の保存
            val sharedPref = activity?.getSharedPreferences("user_info", Context.MODE_PRIVATE)
            with (sharedPref?.edit()) {
                this?.putString("name", editName.text.toString())
                this?.apply()
            }

            findNavController().navigate(R.id.navigation_room_type_select)
        }

        return root
    }
}
