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
import android.widget.ImageView
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
import androidx.navigation.fragment.findNavController
import com.example.hideandseek.R
import com.example.hideandseek.databinding.FragmentSuccessEscapeDialogBinding
import com.example.hideandseek.ui.viewmodel.CaptureDialogViewModel
import dagger.hilt.android.AndroidEntryPoint

private var d: Dialog? = null

@AndroidEntryPoint
class SuccessEscapeDialogFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        d = dialog

        return ComposeView(requireContext()).apply {
            setContent {
                SuccessEscapeDialogScreen(
                    onNavigate = { dest -> findNavController().navigate(dest) },
                    d = d
                )
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(ComposeView(requireContext()).apply {
            setContent {
                SuccessEscapeDialogScreen(
                    onNavigate = { dest -> findNavController().navigate(dest) },
                    d = d
                )
            }
        })
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
fun SuccessEscapeDialogScreen(onNavigate: (Int) -> (Unit), d: Dialog?) {
    ConstraintLayout {
        // Create references for the composable to constrain
        val (dialog, icon, btClose) = createRefs()

        Image(
            painter = painterResource(R.drawable.text_clear),
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
            painter = painterResource(R.drawable.user01_clear),
            contentDescription = "demon",
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
            painter = painterResource(R.drawable.button_close),
            contentDescription = "button_close",
            modifier = Modifier
                .constrainAs(btClose) {
                    start.linkTo(dialog.start)
                    end.linkTo(dialog.end)
                    bottom.linkTo(dialog.bottom)
                }
                .padding(bottom = 20.dp)
                .width(160.dp)
                .height(80.dp)
                .clickable {
                    onNavigate(R.id.navigation_result)
                    d?.dismiss()
                }
        )
    }
}

@Preview
@Composable
fun SuccessEscapeDialogPreview() {
    ConstraintLayout {
        // Create references for the composable to constrain
        val (dialog, icon, btClose) = createRefs()

        Image(
            painter = painterResource(R.drawable.text_clear),
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
            painter = painterResource(R.drawable.user01_clear),
            contentDescription = "demon",
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
            painter = painterResource(R.drawable.button_close),
            contentDescription = "button_close",
            modifier = Modifier
                .constrainAs(btClose) {
                    start.linkTo(dialog.start)
                    end.linkTo(dialog.end)
                    bottom.linkTo(dialog.bottom)
                }
                .padding(bottom = 20.dp)
                .width(160.dp)
                .height(80.dp)
        )
    }
}
