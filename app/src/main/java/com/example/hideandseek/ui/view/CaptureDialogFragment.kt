package com.example.hideandseek.ui.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hideandseek.R
import com.example.hideandseek.ui.viewmodel.CaptureDialogViewModel
import dagger.hilt.android.AndroidEntryPoint

var flag: Boolean = false

@AndroidEntryPoint
class CaptureDialogFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        dialog?.setOnDismissListener {
            if (flag) {
                val notifyCaptureDialogFragment = NotifyCaptureDialogFragment()
                val supportFragmentManager = childFragmentManager
                notifyCaptureDialogFragment.show(supportFragmentManager, "notifyCapture")
            }
        }

        return ComposeView(requireContext()).apply {
            setContent {
                CaptureDialogScreen(d = dialog)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setDimAmount(0f)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            dialog.window?.decorView?.windowInsetsController?.hide(
                WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars()
            )
            dialog.window?.decorView?.windowInsetsController?.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        return dialog
    }
}

@Composable
fun CaptureDialogScreen(viewModel: CaptureDialogViewModel = viewModel(), d: Dialog?) {
    ConstraintLayout(Modifier.width(380.dp)) {
        // Create references for the composable to constrain
        val (dialog, icon, btYes, btNo) = createRefs()

        Image(
            painter = painterResource(R.drawable.text_captureboolean),
            contentDescription = "dialog",
            modifier = Modifier
                .constrainAs(dialog) {
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
            modifier = Modifier
                .constrainAs(icon) {
                    top.linkTo(dialog.top)
                    end.linkTo(dialog.end)
                    bottom.linkTo(dialog.bottom)
                    start.linkTo(dialog.start)
                }
                .padding(end = 20.dp)
                .width(160.dp)
                .height(160.dp)
        )
        Image(
            painter = painterResource(R.drawable.button_yes),
            contentDescription = "button_yes",
            modifier = Modifier
                .constrainAs(btYes) {
                    end.linkTo(dialog.end)
                    bottom.linkTo(dialog.bottom)
                }
                .padding(end = 20.dp, bottom = 20.dp)
                .width(160.dp)
                .height(80.dp)
                .clickable {
                    flag = true
                    // ステータスを捕まったに変更
                    viewModel.updatePlayerStatus(3)
                    d?.dismiss()
                }
        )
        Image(
            painter = painterResource(R.drawable.button_no),
            contentDescription = "button_no",
            modifier = Modifier
                .constrainAs(btNo) {
                    bottom.linkTo(dialog.bottom)
                    start.linkTo(dialog.start)
                }
                .padding(start = 20.dp, bottom = 20.dp)
                .width(160.dp)
                .height(80.dp)
                .clickable {
                    d?.dismiss()
                }
        )
    }
}

@Preview
@Composable
fun CaptureDialogPreview() {
    ConstraintLayout {
        // Create references for the composable to constrain
        val (dialog, icon, btYes, btNo) = createRefs()

        Image(
            painter = painterResource(R.drawable.text_captureboolean),
            contentDescription = "dialog",
            modifier = Modifier
                .constrainAs(dialog) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
                .width(400.dp)
                .height(352.dp)
        )
        Image(
            painter = painterResource(R.drawable.user04_oni),
            contentDescription = "icon",
            modifier = Modifier
                .constrainAs(icon) {
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
            modifier = Modifier
                .constrainAs(btYes) {
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
            modifier = Modifier
                .constrainAs(btNo) {
                    bottom.linkTo(dialog.bottom)
                    start.linkTo(dialog.start)
                }
                .width(160.dp)
                .height(80.dp)
                .padding(start = 20.dp, bottom = 20.dp)
        )
    }
}
