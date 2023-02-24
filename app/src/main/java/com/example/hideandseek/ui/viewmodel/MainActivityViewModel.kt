package com.example.hideandseek.ui.viewmodel

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hideandseek.data.datasource.remote.PostData
import com.example.hideandseek.data.datasource.remote.ResponseData
import com.example.hideandseek.data.repository.ApiRepository
import com.example.hideandseek.data.repository.LocationRepository
import com.example.hideandseek.data.repository.TrapRepository
import com.example.hideandseek.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val trapRepository: TrapRepository,
) : ViewModel() {
    // Locationデータベースのデータを全消去
    fun deleteAllLocation() = viewModelScope.launch {
        locationRepository.deleteAll()
    }

    fun deleteAllTrap() = viewModelScope.launch {
        trapRepository.deleteAll()
    }
}
