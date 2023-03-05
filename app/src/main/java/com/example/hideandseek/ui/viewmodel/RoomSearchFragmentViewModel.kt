package com.example.hideandseek.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hideandseek.data.datasource.remote.PostData
import com.example.hideandseek.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RoomSearchUiState(
    val userName: String = "",
    val userIcon: Int = 0,
)

@HiltViewModel
class RoomSearchFragmentViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    private val myInfoRepository: MyInfoRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(RoomSearchUiState())
    val uiState: StateFlow<RoomSearchUiState> = _uiState.asStateFlow()

    fun readUserInfo() {
        val name = myInfoRepository.readName()
        val icon = myInfoRepository.readIcon()
        viewModelScope.launch {
            _uiState.update { roomTypeSelectUiState ->
                roomTypeSelectUiState.copy(userName = name, userIcon = icon)
            }
        }
    }

    fun writeSecretWords(secretWords: String) {
        myInfoRepository.writeSecretWords(secretWords)
    }

    fun postPlayer(secretWords: String, name: String, icon: Int) {
        viewModelScope.launch {
            try {
                val request = PostData.PostPlayer(secretWords, name, icon, 0)
                Log.d("POST_TEST_PLAYER", request.toString())
                val response = apiRepository.postPlayer(request)
                if (response.isSuccessful) {
                    Log.d("POST_TEST_PLAYER", "${response}\n${response.body()}")
                } else {
                    Log.d("POST_TEST_PLAYER", "$response")
                }
            } catch (e: java.lang.Exception) {
                Log.d("POST_TEST_PLAYER", "$e")
            }
        }
    }
}