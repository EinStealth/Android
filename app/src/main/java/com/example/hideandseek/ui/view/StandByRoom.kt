package com.example.hideandseek.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.hideandseek.R
import com.example.hideandseek.databinding.FragmentRoomCreateBinding
import com.example.hideandseek.databinding.FragmentStandByRoomBinding

class StandByRoom: Fragment() {
    private var _binding: FragmentStandByRoomBinding? = null
//    private val viewModel: RoomCreateFragmentViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentStandByRoomBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Viewの取得
        val btStart: ImageView = binding.btStart
        val textSecretWord: TextView = binding.textSecretWord

        setFragmentResultListener("RoomCreateFragment") { _, bundle ->
            val result = bundle.getString("secretWord")
            textSecretWord.text = "合言葉: $result"
        }

        btStart.setOnClickListener {
            findNavController().navigate(R.id.navigation_main)
        }

        return root
    }
}