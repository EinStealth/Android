package com.example.hideandseek.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hideandseek.data.datasource.remote.ResponseData
import com.example.hideandseek.data.repository.ApiRepository
import com.example.hideandseek.data.repository.MyInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HideTimeUiState(
    val allPlayer: List<ResponseData.ResponseGetPlayer> = listOf(),
    val demon: Int = 0,
)

@HiltViewModel
class HideTimeViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    private val myInfoRepository: MyInfoRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HideTimeUiState())
    val uiState: StateFlow<HideTimeUiState> = _uiState.asStateFlow()

    init {
        val secretWords = myInfoRepository.readSecretWords()
        getPlayer(secretWords)
        getRoom(secretWords)
    }

    private fun getPlayer(secretWords: String) {
        viewModelScope.launch {
            try {
                val response = apiRepository.getPlayer(secretWords)
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        viewModelScope.launch {
                            _uiState.update { mainUiState ->
                                mainUiState.copy(allPlayer = response.body()!!)
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

    private fun getRoom(secretWords: String) {
        viewModelScope.launch {
            try {
                val response = apiRepository.getRoom(secretWords)
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        viewModelScope.launch {
                            _uiState.update { mainUiState ->
                                mainUiState.copy(demon = response.body()!![0].deamon)
                            }
                        }
                    }
                    Log.d("GET_TEST_DEMON", "${response}\n${response.body()}")
                } else {
                    Log.d("GET_TEST_DEMON", "$response")
                }
            } catch (e: java.lang.Exception) {
                Log.d("GET_TEST_DEMON", "$e")
            }
        }
    }
}