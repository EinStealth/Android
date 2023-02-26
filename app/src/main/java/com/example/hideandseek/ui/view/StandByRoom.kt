package com.example.hideandseek.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.hideandseek.R
import com.example.hideandseek.databinding.FragmentStandByRoomBinding
import com.example.hideandseek.ui.viewmodel.StandByRoomFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StandByRoom: Fragment() {
    private var _binding: FragmentStandByRoomBinding? = null
    private val viewModel: StandByRoomFragmentViewModel by viewModels()

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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    Log.d("RoomDatabase", it.allRoom.toString())
                }
            }
        }

        btStart.setOnClickListener {
            findNavController().navigate(R.id.navigation_main)
        }

        return root
    }
}