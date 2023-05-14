package com.example.hideandseek.ui.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hideandseek.data.datasource.local.LocationData
import com.example.hideandseek.data.datasource.local.TrapData
import com.example.hideandseek.data.datasource.local.UserData
import com.example.hideandseek.data.datasource.remote.ResponseData
import com.example.hideandseek.data.repository.ApiRepository
import com.example.hideandseek.data.repository.LocationRepository
import com.example.hideandseek.data.repository.MapRepository
import com.example.hideandseek.data.repository.MyInfoRepository
import com.example.hideandseek.data.repository.TrapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WatchUiState(
    val allLocation: List<LocationData> = listOf(),
    val latestUser: UserData = UserData(0, "", 0.0, 0.0, 0.0),
    val allTrap: List<TrapData> = listOf(),
    val allPlayer: List<ResponseData.ResponseGetPlayer> = listOf(),
    val map: Bitmap? = null,
    val preRelativeTime: String = ""
)

@HiltViewModel
class WatchFragmentViewModel @Inject constructor(
    locationRepository: LocationRepository,
    private val trapRepository: TrapRepository,
    private val mapRepository: MapRepository,
    private val myInfoRepository: MyInfoRepository,
    private val apiRepository: ApiRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(WatchUiState())
    val uiState: StateFlow<WatchUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            locationRepository.allLocations.collect { allLocations ->
                _uiState.update { mainUiState ->
                    mainUiState.copy(allLocation = allLocations)
                }
            }
        }
        viewModelScope.launch {
            while (true) {
                val relativeTime = myInfoRepository.readRelativeTime()
                val location = myInfoRepository.raedLocation() // List<latitude, longitude, altitude, speed>
                val userData = UserData(0, relativeTime, location[0].toDouble(), location[1].toDouble(), location[2].toDouble())
                if (_uiState.value.latestUser != userData) {
                    _uiState.update { mainUiState ->
                        mainUiState.copy(latestUser = userData)
                    }
                }
                delay(100)
            }
        }
        viewModelScope.launch {
            trapRepository.allTraps.collect { allTraps ->
                _uiState.update { mainUiState ->
                    mainUiState.copy(allTrap = allTraps)
                }
            }
        }
        viewModelScope.launch {
            while (true) {
                val secretWords = myInfoRepository.readSecretWords()
                getPlayer(secretWords)
                delay(100)
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

    fun updatePreRelativeTime() {
        _uiState.update { watchUiState ->
            watchUiState.copy(preRelativeTime = watchUiState.latestUser.relativeTime)
        }
    }

    private fun setMap(p0: Bitmap) {
        _uiState.update { mainUiState ->
            mainUiState.copy(map = p0)
        }
    }

    fun postTrapRoom(isMine: Int, latestUser: UserData) = viewModelScope.launch {
        Log.d("USER_TRAP", latestUser.toString())
        val trap = TrapData(0, latestUser.latitude, latestUser.longitude, isMine)
        trapRepository.insert(trap)
    }

    fun fetchMap(latestUser: UserData, allLocation: List<LocationData>, allTraps: List<TrapData>) {
        viewModelScope.launch {
            val fetchedMap = mapRepository.fetchMap(latestUser, allLocation, allTraps)
            setMap(fetchedMap)
        }
    }
}
