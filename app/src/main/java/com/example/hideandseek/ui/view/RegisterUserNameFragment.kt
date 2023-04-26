package com.example.hideandseek.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.example.hideandseek.R
import com.example.hideandseek.databinding.FragmentRegisterUserNameBinding
import com.example.hideandseek.ui.viewmodel.RegisterUserNameFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterUserNameFragment : Fragment() {
    private val viewModel: RegisterUserNameFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // 既に名前が保存されているか確認する
        viewModel.readUserInfo()

        return ComposeView(requireContext()).apply {
            setContent {
                RegisterUserNameScreen(
                    onNavigate = { dest -> findNavController().navigate(dest) }
                )
            }
        }
    }
}

@Composable
fun RegisterUserNameScreen(onNavigate: (Int) -> (Unit), viewModel: RegisterUserNameFragmentViewModel = viewModel()) {
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
                        viewModel.writeUserName(text)
                        if (viewModel.uiState.value.isEdit) {
                            onNavigate(R.id.navigation_room_type_select)
                        } else {
                            onNavigate(R.id.navigation_register_user_icon)
                        }
                    }
            )
        }
    }
}

@Preview
@Composable
fun RegisterUserNamePreview() {
    ConstraintLayout {
        // Create references for the composable to constrain
        val (background, dialog, btDecide, textField) = createRefs()

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
        )
    }
}
