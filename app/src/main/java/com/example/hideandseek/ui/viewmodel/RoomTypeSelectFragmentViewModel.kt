package com.example.hideandseek.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hideandseek.data.repository.ApiRepository
import com.example.hideandseek.data.repository.MyInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RoomTypeSelectUiState(
    val userName: String = "",
    val userIcon: Int = 0,
)

@HiltViewModel
class RoomTypeSelectFragmentViewModel @Inject constructor(
    private val myInfoRepository: MyInfoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RoomTypeSelectUiState())
    val uiState: StateFlow<RoomTypeSelectUiState> = _uiState.asStateFlow()
    fun readUserInfo() {
        val name = myInfoRepository.readName()
        val icon = myInfoRepository.readIcon()
        viewModelScope.launch {
            _uiState.update { roomTypeSelectUiState ->
                roomTypeSelectUiState.copy(userName = name, userIcon = icon)
            }
        }
    }
}
