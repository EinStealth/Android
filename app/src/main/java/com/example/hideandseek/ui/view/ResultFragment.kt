package com.example.hideandseek.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.hideandseek.R
import com.example.hideandseek.databinding.FragmentResultBinding
import com.example.hideandseek.ui.viewmodel.ResultFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val viewModel: ResultFragmentViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // TODO: 結果に応じてResultを変える

        return root
    }
}

@Composable
fun ResultScreen(onNavigate: (Int) -> (Unit)) {
    Surface(Modifier.fillMaxSize()) {
        ConstraintLayout {
            Image(
                painter = painterResource(R.drawable.title_background_responsive),
                contentDescription = "title"
            )
        }
    }
}

@Composable
@Preview
fun ResultPreview() {
    ConstraintLayout {
        // Create references for the composable to constrain
        val (dialog, textResult, textWinner, textLoser, winner1, winner2, loser1, loser2, btClose) = createRefs()

        Image(
            painter = painterResource(R.drawable.text_null),
            contentDescription = "dialog",
            modifier = Modifier
                .constrainAs(dialog) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
                .scale(scaleX = 1f, scaleY = 2f)
                .fillMaxHeight()
                .padding(10.dp)
        )
        Text(
            text = "RESULT",
            fontSize = 32.sp,
            modifier = Modifier
                .constrainAs(textResult) {
                    end.linkTo(parent.end)
                    bottom.linkTo(textWinner.top)
                    start.linkTo(parent.start)
                }
                .padding(bottom = 12.dp)
        )
        Text(
            text = "WINNER",
            fontSize = 32.sp,
            modifier = Modifier
                .constrainAs(textWinner) {
                    top.linkTo(dialog.top)
                    bottom.linkTo(textLoser.top)
                    start.linkTo(dialog.start)
                }
                .padding(start = 32.dp)
        )
        Image(
            painter = painterResource(R.drawable.user01_normal),
            contentDescription = "winner1",
            modifier = Modifier
                .constrainAs(winner1) {
                    top.linkTo(textWinner.bottom)
                    bottom.linkTo(textLoser.top)
                    start.linkTo(textWinner.start)
                }
                .height(120.dp)
                .width(120.dp)
        )
        Image(
            painter = painterResource(R.drawable.user02_normal),
            contentDescription = "winner2",
            modifier = Modifier
                .constrainAs(winner2) {
                    top.linkTo(textWinner.bottom)
                    bottom.linkTo(textLoser.top)
                    start.linkTo(winner1.end)
                }
                .padding(start = 12.dp)
                .height(120.dp)
                .width(120.dp)
        )
        Text(
            text = "LOSER",
            fontSize = 32.sp,
            modifier = Modifier
                .constrainAs(textLoser) {
                    top.linkTo(dialog.top)
                    bottom.linkTo(dialog.bottom)
                    start.linkTo(dialog.start)
                }
                .padding(start = 32.dp)
        )
        Image(
            painter = painterResource(R.drawable.user03_normal),
            contentDescription = "loser1",
            modifier = Modifier
                .constrainAs(loser1) {
                    top.linkTo(textLoser.bottom)
                    start.linkTo(textWinner.start)
                }
                .padding(top = 24.dp)
                .height(120.dp)
                .width(120.dp)
        )
        Image(
            painter = painterResource(R.drawable.user02_normal),
            contentDescription = "loser2",
            modifier = Modifier
                .constrainAs(loser2) {
                    top.linkTo(textLoser.bottom)
                    start.linkTo(loser1.end)
                }
                .padding(top = 24.dp, start = 12.dp)
                .height(120.dp)
                .width(120.dp)
        )
        Image(
            painter = painterResource(R.drawable.button_close),
            contentDescription = "button_close",
            modifier = Modifier
                .constrainAs(btClose) {
                    top.linkTo(loser1.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                .padding(top = 52.dp)
                .height(80.dp)
                .width(160.dp)
        )
    }
}
