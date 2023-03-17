package com.example.hideandseek.ui.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.hideandseek.R
import com.example.hideandseek.data.repository.ApiRepository
import com.example.hideandseek.data.repository.MyInfoRepository
import com.example.hideandseek.databinding.FragmentCaptureDialogBinding
import com.example.hideandseek.ui.viewmodel.CaptureDialogViewModel
import com.example.hideandseek.ui.viewmodel.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CaptureDialogFragment : DialogFragment() {
    private var _binding: FragmentCaptureDialogBinding? = null
    private val viewModel: CaptureDialogViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCaptureDialogBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 捕まったか確認するダイアログ
        val btCaptureYes: ImageView = binding.btCaptureYes
        val btCaptureNo: ImageView = binding.btCaptureNo

        var flag = false

        btCaptureYes.setOnClickListener {
            flag = true
            //　ステータスを捕まったに変更
            viewModel.updatePlayerStatus(3)
            dialog?.dismiss()
        }

        btCaptureNo.setOnClickListener {
            dialog?.dismiss()
        }

        dialog?.setOnDismissListener {
            if (flag) {
                val notifyCaptureDialogFragment = NotifyCaptureDialogFragment()
                val supportFragmentManager = childFragmentManager
                notifyCaptureDialogFragment.show(supportFragmentManager, "notifyCapture")
            }
        }

        return root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.fragment_capture_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setDimAmount(0f)
        return dialog
    }
}
