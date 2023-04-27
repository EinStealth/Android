package com.example.hideandseek.ui.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hideandseek.R
import com.example.hideandseek.databinding.FragmentCaptureDialogBinding
import com.example.hideandseek.ui.viewmodel.CaptureDialogViewModel
import dagger.hilt.android.AndroidEntryPoint

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
            // ステータスを捕まったに変更
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
        dialog.setContentView(
            ComposeView(requireContext()).apply {
                setContent {
                    CaptureDialogScreen(
                        onNavigate = { dest -> findNavController().navigate(dest) }
                    )
                }
            }
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setDimAmount(0f)
        return dialog
    }
}

@Composable
fun CaptureDialogScreen(onNavigate: (Int) -> (Unit)) {
    Surface(Modifier.fillMaxSize()) {
        ConstraintLayout {
            // Create references for the composable to constrain
            val (dialog, icon, btYes, btNo) = createRefs()

            Image(
                painter = painterResource(R.drawable.text_captureboolean),
                contentDescription = "dialog"
            )
        }
    }
}

@Preview
@Composable
fun CaptureDialogPreview() {
    Surface(Modifier.fillMaxSize()) {
        ConstraintLayout {
            // Create references for the composable to constrain
            val (dialog, icon, btYes, btNo) = createRefs()

            Image(
                painter = painterResource(R.drawable.text_captureboolean),
                contentDescription = "dialog",
                modifier = Modifier.constrainAs(dialog) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
                    .width(380.dp)
                    .height(332.dp)
            )
            Image(
                painter = painterResource(R.drawable.user04_oni),
                contentDescription = "icon",
                modifier = Modifier.constrainAs(icon) {
                    top.linkTo(dialog.top)
                    end.linkTo(dialog.end)
                    bottom.linkTo(dialog.bottom)
                    start.linkTo(dialog.start)
                }
                    .width(160.dp)
                    .height(160.dp)
                    .padding(end = 20.dp)
            )
            Image(
                painter = painterResource(R.drawable.button_yes),
                contentDescription = "button_yes",
                modifier = Modifier.constrainAs(btYes) {
                    end.linkTo(dialog.end)
                    bottom.linkTo(dialog.bottom)
                }
                    .width(160.dp)
                    .height(80.dp)
                    .padding(end = 20.dp, bottom = 20.dp)
            )
            Image(
                painter = painterResource(R.drawable.button_no),
                contentDescription = "button_no",
                modifier = Modifier.constrainAs(btNo) {
                    bottom.linkTo(dialog.bottom)
                    start.linkTo(dialog.start)
                }
                    .width(160.dp)
                    .height(80.dp)
                    .padding(start = 20.dp, bottom = 20.dp)
            )
        }
    }
}
