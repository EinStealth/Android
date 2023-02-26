package com.example.hideandseek.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hideandseek.R
import com.example.hideandseek.databinding.FragmentRoomCreateBinding
import com.example.hideandseek.ui.viewmodel.RoomCreateFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomCreateFragment: Fragment() {
    private var _binding: FragmentRoomCreateBinding? = null
    private val viewModel: RoomCreateFragmentViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRoomCreateBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Viewの取得
        val btCreate: ImageView = binding.btCreate
        val editSecretWord: EditText = binding.editSecretWord
        var userName = "a"
        val userIcon = 0

        setFragmentResultListener("UserRegisterFragmentName") { _, bundle ->
            val result = bundle.getString("name")
            userName = result.toString()
        }

        btCreate.setOnClickListener {
            viewModel.postRoom(editSecretWord.text.toString())
            setFragmentResult("RoomCreateFragment", bundleOf("secretWord" to editSecretWord.text.toString()))
            findNavController().navigate(R.id.navigation_stand_by_room)
        }

        return root
    }
}