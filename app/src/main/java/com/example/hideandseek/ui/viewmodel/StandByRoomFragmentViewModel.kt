package com.example.hideandseek.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.hideandseek.data.datasource.remote.ResponseData
import com.example.hideandseek.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

data class StandByRoomUiState (
    val allPlayer: List<ResponseData.ResponseGetPlayer> = listOf(),
    val secretWords: String = "",
)

@HiltViewModel
class StandByRoomFragmentViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    private val myInfoRepository: MyInfoRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(StandByRoomUiState())
    val uiState: StateFlow<StandByRoomUiState> = _uiState.asStateFlow()

    init {
        setSecretWords()
        viewModelScope.launch {
            while (true) {
                getPlayer(uiState.value.secretWords)
                delay(1000)
            }
        }
    }

    private fun readSecretWords(): String {
        return myInfoRepository.readSecretWords()
    }

    private fun setSecretWords() {
        viewModelScope.launch {
            _uiState.update { standByRoomUiState ->
                standByRoomUiState.copy(secretWords = readSecretWords())
            }
        }
    }

    private fun getPlayer(secretWords: String) {
        viewModelScope.launch {
            try {
                val response = apiRepository.getPlayer(secretWords)
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        viewModelScope.launch {
                            _uiState.update { standByRoomUiState ->
                                standByRoomUiState.copy(allPlayer = response.body()!!)
                            }
                        }
                    }
                    Log.d("GET_TEST_PLAYER", "${response}\n${response.body()}")
                } else {
                    Log.d("GET_TEST_PLAYER", "$response")
                }
            } catch (e: java.lang.Exception) {
                Log.d("GET_TEST_PLAYER", "$e")
            }
        }
    }
}