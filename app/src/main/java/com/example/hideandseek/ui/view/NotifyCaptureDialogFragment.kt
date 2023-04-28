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
import com.example.hideandseek.databinding.FragmentNotifyCaptureDialogBinding
import com.example.hideandseek.ui.viewmodel.CaptureDialogViewModel
import dagger.hilt.android.AndroidEntryPoint

private var d: Dialog? = null

@AndroidEntryPoint
class NotifyCaptureDialogFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        d = dialog

        return ComposeView(requireContext()).apply {
            setContent {
                NotifyCaptureDialogScreen(
                    onNavigate = { dest -> findNavController().navigate(dest) },
                    d = d
                )
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(
            ComposeView(requireContext()).apply {
                setContent {
                    NotifyCaptureDialogScreen(
                        onNavigate = { dest -> findNavController().navigate(dest) },
                        d = d
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
fun NotifyCaptureDialogScreen(onNavigate: (Int) -> (Unit), viewModel: CaptureDialogViewModel = viewModel(), d: Dialog?) {
    ConstraintLayout {
        // Create references for the composable to constrain
        val (dialog, demon, normal, metalRod, btClose) = createRefs()

        Image(
            painter = painterResource(R.drawable.text_captured),
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
            contentDescription = "demon",
            modifier = Modifier
                .constrainAs(demon) {
                    top.linkTo(dialog.top)
                    bottom.linkTo(dialog.bottom)
                    start.linkTo(dialog.start)
                }
                .padding(end = 20.dp)
                .width(160.dp)
                .height(160.dp)
        )
        Image(
            painter = painterResource(R.drawable.user01_caputure),
            contentDescription = "normal",
            modifier = Modifier
                .constrainAs(normal) {
                    top.linkTo(dialog.top)
                    end.linkTo(dialog.end)
                    bottom.linkTo(dialog.bottom)
                }
                .padding(end = 20.dp)
                .width(160.dp)
                .height(160.dp)
        )
        Image(
            painter = painterResource(R.drawable.kanabou),
            contentDescription = "metal_rod",
            modifier = Modifier
                .constrainAs(metalRod) {
                    top.linkTo(normal.top)
                    start.linkTo(demon.end)
                }
                .width(120.dp)
                .height(120.dp)
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
                    onNavigate(R.id.navigation_watch)
                    d?.dismiss()
                }
        )
    }
}

@Preview
@Composable
fun NotifyCaptureDialogPreview() {
    ConstraintLayout {
        // Create references for the composable to constrain
        val (dialog, demon, normal, metalRod, btClose) = createRefs()

        Image(
            painter = painterResource(R.drawable.text_captured),
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
            contentDescription = "demon",
            modifier = Modifier
                .constrainAs(demon) {
                    top.linkTo(dialog.top)
                    bottom.linkTo(dialog.bottom)
                    start.linkTo(dialog.start)
                }
                .padding(end = 20.dp)
                .width(160.dp)
                .height(160.dp)
        )
        Image(
            painter = painterResource(R.drawable.user01_caputure),
            contentDescription = "normal",
            modifier = Modifier
                .constrainAs(normal) {
                    top.linkTo(dialog.top)
                    end.linkTo(dialog.end)
                    bottom.linkTo(dialog.bottom)
                }
                .padding(end = 20.dp)
                .width(160.dp)
                .height(160.dp)
        )
        Image(
            painter = painterResource(R.drawable.kanabou),
            contentDescription = "metal_rod",
            modifier = Modifier
                .constrainAs(metalRod) {
                    top.linkTo(normal.top)
                    start.linkTo(demon.end)
                }
                .width(120.dp)
                .height(120.dp)
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
