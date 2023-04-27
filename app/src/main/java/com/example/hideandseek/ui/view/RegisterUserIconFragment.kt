package com.example.hideandseek.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.example.hideandseek.R
import com.example.hideandseek.databinding.FragmentRegisterUserIconBinding
import com.example.hideandseek.ui.viewmodel.RegisterUserIconFragmentViewModel
import com.example.hideandseek.ui.viewmodel.RegisterUserNameFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterUserIconFragment : Fragment() {
    private var _binding: FragmentRegisterUserIconBinding? = null
    private val viewModel: RegisterUserIconFragmentViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegisterUserIconBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Viewの取得
        val character01: ImageView = binding.character01
        val character02: ImageView = binding.character02
        val character03: ImageView = binding.character03
        val character04: ImageView = binding.character04

        // characterが選択されたときの処理
        character01.setOnClickListener {
            // アイコンの保存
            viewModel.writeUserIcon(1)
            findNavController().navigate(R.id.navigation_room_type_select)
        }
        character02.setOnClickListener {
            // アイコンの保存
            viewModel.writeUserIcon(2)
            findNavController().navigate(R.id.navigation_room_type_select)
        }
        character03.setOnClickListener {
            // アイコンの保存
            viewModel.writeUserIcon(3)
            findNavController().navigate(R.id.navigation_room_type_select)
        }
        character04.setOnClickListener {
            // アイコンの保存
            viewModel.writeUserIcon(4)
            findNavController().navigate(R.id.navigation_room_type_select)
        }

        return ComposeView(requireContext()).apply {
            setContent {
                RegisterUserIconScreen(
                    onNavigate = { dest -> findNavController().navigate(dest) }
                )
            }
        }
    }
}


@Composable
fun RegisterUserIconScreen(onNavigate: (Int) -> (Unit), viewModel: RegisterUserIconFragmentViewModel = viewModel()) {
    Surface(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.title_background_responsive_nontitlever),
            contentDescription = "background",
            contentScale = ContentScale.Crop
        )
        ConstraintLayout {
            // Create references for the composable to constrain
            val (dialog, btDecide, textField) = createRefs()

            Image(
                painter = painterResource(R.drawable.text_null),
                contentDescription = "dialog",
                modifier = Modifier.constrainAs(dialog) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
            )
            var text by remember { mutableStateOf("") }

            TextField(
                value = text,
                onValueChange = { text = it },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                ),
                placeholder = { Text(text = "名前を入力して下さい") },
                modifier = Modifier.constrainAs(textField) {
                    top.linkTo(dialog.top)
                    end.linkTo(dialog.end)
                    bottom.linkTo(dialog.bottom)
                    start.linkTo(dialog.start)
                }
            )

            Image(
                painter = painterResource(R.drawable.button_decide),
                contentDescription = "decide_button",
                modifier = Modifier
                    .constrainAs(btDecide) {
                        top.linkTo(textField.bottom)
                        end.linkTo(dialog.end)
                        bottom.linkTo(dialog.bottom)
                        start.linkTo(dialog.start)
                    }
                    .width(142.dp)
                    .height(72.dp)
                    .clickable {
//                        viewModel.writeUserName(text)
//                        if (viewModel.uiState.value.isEdit) {
//                            onNavigate(R.id.navigation_room_type_select)
//                        } else {
//                            onNavigate(R.id.navigation_register_user_icon)
//                        }
                    }
            )
        }
    }
}

@Preview
@Composable
fun RegisterUserIconPreview() {
    ConstraintLayout {
        // Create references for the composable to constrain
        val (background, dialog, iconList, icon1, icon2, icon3, icon4) = createRefs()

        Image(
            painter = painterResource(R.drawable.title_background_responsive_nontitlever),
            contentDescription = "background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.constrainAs(background) {
                top.linkTo(parent.top)
            }
        )
        Image(
            painter = painterResource(R.drawable.text_null),
            contentDescription = "dialog",
            modifier = Modifier.constrainAs(dialog) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        )
        Row(
            modifier = Modifier.constrainAs(iconList) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
                .fillMaxWidth(1f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(R.drawable.user01_normal),
                contentDescription = "icon1",
                modifier = Modifier.width(92.dp)
                    .height(92.dp)
            )
            Image(
                painter = painterResource(R.drawable.user02_normal),
                contentDescription = "icon2",
                modifier = Modifier.width(92.dp)
                    .height(92.dp)
            )
            Image(
                painter = painterResource(R.drawable.user03_normal),
                contentDescription = "icon3",
                modifier = Modifier.width(92.dp)
                    .height(92.dp)
            )
            Image(
                painter = painterResource(R.drawable.user04_normal),
                contentDescription = "icon4",
                modifier = Modifier.width(92.dp)
                    .height(92.dp)
            )
        }
    }
}
