package com.example.hideandseek.ui.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hideandseek.data.datasource.local.LocationData
import com.example.hideandseek.data.datasource.local.TrapData
import com.example.hideandseek.data.datasource.local.UserData
import com.example.hideandseek.data.repository.LocationRepository
import com.example.hideandseek.data.repository.MapRepository
import com.example.hideandseek.data.repository.TrapRepository
import com.example.hideandseek.domain.CalculateRelativeTimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WatchUiState(
    val allLocation: List<LocationData> = listOf(),
    val latestUser:  UserData           = UserData(0, "", 0.0, 0.0, 0.0),
    val allTrap:     List<TrapData>     = listOf(),
    val map: Bitmap? = null
)

@HiltViewModel
class WatchFragmentViewModel @Inject constructor(
    locationRepository: LocationRepository,
    private val trapRepository: TrapRepository,
    private val mapRepository: MapRepository,
    private val calculateRelativeTimeUseCase: CalculateRelativeTimeUseCase,
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
            calculateRelativeTimeUseCase().collect { userData ->
                _uiState.update { mainUiState ->
                    mainUiState.copy(latestUser = userData)
                }
            }
        }
        viewModelScope.launch {
            trapRepository.allTraps.collect { allTraps ->
                _uiState.update { mainUiState ->
                    mainUiState.copy(allTrap = allTraps)
                }
            }
        }
    }

    private fun setMap(p0: Bitmap) {
        _uiState.update { mainUiState ->
            mainUiState.copy(map = p0)
        }
    }

    fun postTrapRoom(isMine: Int, latestUser: UserData) = viewModelScope.launch {
        Log.d("USER_TRAP", latestUser.toString())
        val trap = TrapData(0, latestUser.latitude, latestUser.longitude, latestUser.altitude, isMine)
        trapRepository.insert(trap)
    }

    fun fetchMap(latestUser: UserData, allLocation: List<LocationData>, allTraps: List<TrapData>) {
        viewModelScope.launch {
            val fetchedMap = mapRepository.fetchMap(latestUser, allLocation, allTraps)
            setMap(fetchedMap)
        }
    }
}
