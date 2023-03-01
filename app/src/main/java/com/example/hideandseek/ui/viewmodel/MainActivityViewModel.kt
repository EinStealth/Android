package com.example.hideandseek.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hideandseek.data.repository.LocationRepository
import com.example.hideandseek.data.repository.MyInfoRepository
import com.example.hideandseek.data.repository.MyLocationRepository
import com.example.hideandseek.data.repository.TrapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val trapRepository: TrapRepository,
    private val myLocationRepository: MyLocationRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            myLocationRepository.start()
        }
    }

    // Locationデータベースのデータを全消去
    fun deleteAllLocation() = viewModelScope.launch {
        locationRepository.deleteAll()
    }

    fun deleteAllTrap() = viewModelScope.launch {
        trapRepository.deleteAll()
    }
}
