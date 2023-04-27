package com.example.hideandseek.ui.viewmodel

import android.util.Log
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

data class CaptureUiState(
    val myName: String = "",
    val flag: Boolean = false,
    val isDismiss: Boolean = false
)

@HiltViewModel
class CaptureDialogViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    private val myInfoRepository: MyInfoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CaptureUiState())
    val uiState: StateFlow<CaptureUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val myName = myInfoRepository.readName()
            _uiState.update { mainUiState ->
                mainUiState.copy(myName = myName)
            }
        }
    }

    fun updatePlayerStatus(status: Int) {
        viewModelScope.launch {
            try {
                val response = apiRepository.postPlayerStatus(uiState.value.myName, status)
                if (response.isSuccessful) {
                    Log.d("UPDATE_TEST_PLAYER", "${response}\n${response.body()}")
                } else {
                    Log.d("UPDATE_TEST_PLAYER", "$response")
                }
            } catch (e: java.lang.Exception) {
                Log.d("UPDATE_TEST_PLAYER", "$e")
            }
        }
    }

    fun changeFlag() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(flag = true)
            }
        }
    }

    fun dialogDismiss() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isDismiss = true)
            }
        }
    }
}
