package com.example.hideandseek.ui.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hideandseek.data.datasource.local.LocationData
import com.example.hideandseek.data.datasource.local.TrapData
import com.example.hideandseek.data.datasource.local.UserData
import com.example.hideandseek.data.datasource.remote.PostData
import com.example.hideandseek.data.datasource.remote.ResponseData
import com.example.hideandseek.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

data class CaptureUiState(
    val myName: String = "",
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
}
