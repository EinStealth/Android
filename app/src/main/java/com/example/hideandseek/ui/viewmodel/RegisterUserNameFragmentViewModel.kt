package com.example.hideandseek.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hideandseek.data.repository.MyInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUserNameUiState(
    val userName: String = "",
    val isEdit: Boolean = false, // RoomTypeSelectFragmentから遷移してきた場合true
)

@HiltViewModel
class RegisterUserNameFragmentViewModel @Inject constructor(
    private val myInfoRepository: MyInfoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUserNameUiState())
    val uiState: StateFlow<RegisterUserNameUiState> = _uiState.asStateFlow()
    fun readUserInfo() {
        val name = myInfoRepository.readName()
        viewModelScope.launch {
            _uiState.update { roomTypeSelectUiState ->
                roomTypeSelectUiState.copy(userName = name, isEdit = name != "")
            }
        }
    }

    fun writeUserName(name: String) {
        myInfoRepository.writeName(name)
    }
}
