package com.example.hideandseek.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.constraintlayout.compose.Visibility
import androidx.fragment.app.Fragment
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
class StandByRoomFragment : Fragment() {
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
            findNavController().navigate(R.id.navigation_main)
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

@Composable
@Preview
fun StandByRoomPreview() {
    ConstraintLayout {
        // Create references for the composable to constrain
        val (background, dialog, textSecret, icon1, icon2, icon3, icon4, name1, name2, name3, name4, btStart) = createRefs()

        Image(
            painter = painterResource(R.drawable.title_background_responsive_nontitlever),
            contentDescription = "background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .constrainAs(background) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )
        Image(
            painter = painterResource(R.drawable.stand_by_room),
            contentDescription = "dialog",
            modifier = Modifier
                .constrainAs(dialog) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
                .width(302.dp)
                .height(405.dp)
        )
        Text(
            text = "secretWords",
            fontSize = 24.sp,
            modifier = Modifier
                .constrainAs(textSecret) {
                    top.linkTo(dialog.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                .padding(top = 44.dp)
        )
        Image(
            painter = painterResource(R.drawable.user01_normal),
            contentDescription = "icon1",
            modifier = Modifier
                .constrainAs(icon1) {
                    top.linkTo(dialog.top)
                    start.linkTo(dialog.start)
                }
                .padding(top = 80.dp, start = 12.dp)
                .height(100.dp)
                .width(100.dp)
        )
        Text(
            text = "name1",
            modifier = Modifier
                .constrainAs(name1) {
                    top.linkTo(icon1.bottom)
                    start.linkTo(icon1.start)
                    end.linkTo(icon1.end)
                }
        )
        Image(
            painter = painterResource(R.drawable.user01_normal),
            contentDescription = "icon2",
            modifier = Modifier
                .constrainAs(icon2) {
                    top.linkTo(dialog.top)
                    start.linkTo(icon1.end)
                }
                .padding(top = 80.dp, start = 12.dp)
                .height(100.dp)
                .width(100.dp)
        )
        Text(
            text = "name2",
            modifier = Modifier
                .constrainAs(name2) {
                    top.linkTo(icon1.bottom)
                    start.linkTo(icon2.start)
                    end.linkTo(icon2.end)
                }
        )
        Image(
            painter = painterResource(R.drawable.user01_normal),
            contentDescription = "icon3",
            modifier = Modifier
                .constrainAs(icon3) {
                    top.linkTo(name1.bottom)
                    start.linkTo(dialog.start)
                }
                .height(100.dp)
                .width(100.dp)
        )
        Text(
            text = "name3",
            modifier = Modifier
                .constrainAs(name3) {
                    top.linkTo(icon3.bottom)
                    start.linkTo(icon3.start)
                    end.linkTo(icon3.end)
                }
        )
        Image(
            painter = painterResource(R.drawable.user01_normal),
            contentDescription = "icon4",
            modifier = Modifier
                .constrainAs(icon4) {
                    top.linkTo(name2.bottom)
                    start.linkTo(icon3.end)
                }
                .padding(start = 12.dp)
                .height(100.dp)
                .width(100.dp)
        )
        Text(
            text = "name4",
            modifier = Modifier
                .constrainAs(name4) {
                    top.linkTo(icon4.bottom)
                    start.linkTo(icon4.start)
                    end.linkTo(icon4.end)
                }
        )
        Image(
            painter = painterResource(R.drawable.bt_start),
            contentDescription = "button_start",
            modifier = Modifier
                .constrainAs(btStart) {
                    end.linkTo(parent.end)
                    bottom.linkTo(dialog.bottom)
                    start.linkTo(parent.start)
                }
                .padding(bottom = 12.dp)
                .width(142.dp)
                .height(72.dp)
        )
    }
}
