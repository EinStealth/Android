package com.example.hideandseek.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.hideandseek.databinding.FragmentRoomTypeSelectBinding
import com.example.hideandseek.ui.viewmodel.RoomTypeSelectFragmentViewModel

class RoomTypeSelectFragment: Fragment() {
    private var _binding: FragmentRoomTypeSelectBinding? = null
    private val viewModel: RoomTypeSelectFragmentViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRoomTypeSelectBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // TODO: 結果に応じてResultを変える

        return root
    }
}