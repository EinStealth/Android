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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
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
        val user1Icon: ImageView = binding.user1Icon
        val user1Name: TextView = binding.user1Name
        val user2Icon: ImageView = binding.user2Icon
        val user2Name: TextView = binding.user2Name
        val user3Icon: ImageView = binding.user3Icon
        val user3Name: TextView = binding.user3Name
        val user4Icon: ImageView = binding.user4Icon
        val user4Name: TextView = binding.user4Name

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { standByRoomUiState ->
                    Log.d("StandByRoom", "secret_words: ${standByRoomUiState.secretWords}, is_start: ${standByRoomUiState.isStart}")
                    if (standByRoomUiState.isStart == 1) {
                        findNavController().navigate(R.id.navigation_main)
                    }
                    textSecretWord.text = standByRoomUiState.secretWords
                    val allPlayer = standByRoomUiState.allPlayer
                    if (allPlayer.isNotEmpty()) {
                        for (i in allPlayer.indices) {
                            if (i == 0) {
                                user1Icon.setImageResource(selectDrawable(allPlayer[i].icon))
                                user1Name.text = allPlayer[i].name
                                user1Icon.visibility = View.VISIBLE
                                user1Name.visibility = View.VISIBLE
                            }
                            if (i == 1) {
                                user2Icon.setImageResource(selectDrawable(allPlayer[i].icon))
                                user2Name.text = allPlayer[i].name
                                user2Icon.visibility = View.VISIBLE
                                user2Name.visibility = View.VISIBLE
                            }
                            if (i == 2) {
                                user3Icon.setImageResource(selectDrawable(allPlayer[i].icon))
                                user3Name.text = allPlayer[i].name
                                user3Icon.visibility = View.VISIBLE
                                user3Name.visibility = View.VISIBLE
                            }
                            if (i == 3) {
                                user4Icon.setImageResource(selectDrawable(allPlayer[i].icon))
                                user4Name.text = allPlayer[i].name
                                user4Icon.visibility = View.VISIBLE
                                user4Name.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        }

        btStart.setOnClickListener {
            viewModel.updateIsStart()
        }

        return root
    }

    private fun selectDrawable(icon: Int): Int {
        return when (icon) {
            1 -> {
                R.drawable.user01_normal
            }
            2 -> {
                R.drawable.user02_normal
            }
            3 -> {
                R.drawable.user03_normal
            }
            else -> {
                R.drawable.user04_normal
            }
        }
    }
}