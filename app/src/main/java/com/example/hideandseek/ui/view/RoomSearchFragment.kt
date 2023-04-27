package com.example.hideandseek.ui.view

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
import com.example.hideandseek.databinding.FragmentRoomSearchBinding
import com.example.hideandseek.ui.viewmodel.RoomSearchFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RoomSearchFragment : Fragment() {
    private var _binding: FragmentRoomSearchBinding? = null
    private val viewModel: RoomSearchFragmentViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRoomSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Viewの取得
        val btSearch: ImageView = binding.btSearch
        val editSecretWord: EditText = binding.editSecretWord

        // 名前・アイコンの読み込み
        viewModel.readUserInfo()

        btSearch.setOnClickListener {
            // TODO: 部屋が存在している確認
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.uiState.collect {
                        // player情報のpost
                        viewModel.postPlayer(editSecretWord.text.toString(), it.userName, it.userIcon)
                    }
                }
            }
            // secret_wordsの保存
            viewModel.writeSecretWords(editSecretWord.text.toString())
            findNavController().navigate(R.id.navigation_stand_by_room)
        }

        return root
    }
}
