package com.example.hideandseek.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hideandseek.data.datasource.local.RoomData
import com.example.hideandseek.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StandByRoomUiState(
    val allRoom: List<RoomData> = listOf(),
)

@HiltViewModel
class StandByRoomFragmentViewModel @Inject constructor(
    private val roomRepository: RoomRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(StandByRoomUiState())
    val uiState: StateFlow<StandByRoomUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            roomRepository.allRoom.collect { allRoom ->
                _uiState.update { standByRoomUiState ->
                    standByRoomUiState.copy(allRoom = allRoom)
                }
            }
        }
    }

    fun postRoom(secretWords: String) {
        // TODO:　同じ合言葉の部屋が無いか確認する
        viewModelScope.launch {
            val room = RoomData(0, secretWords, false)
            roomRepository.insert(room)
        }
    }
}