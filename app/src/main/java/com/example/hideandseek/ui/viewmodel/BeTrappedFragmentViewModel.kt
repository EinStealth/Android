package com.example.hideandseek.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hideandseek.data.datasource.local.TrapData
import com.example.hideandseek.data.datasource.local.UserData
import com.example.hideandseek.data.datasource.remote.PostData
import com.example.hideandseek.data.repository.ApiRepository
import com.example.hideandseek.data.repository.TrapRepository
import com.example.hideandseek.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BeTrappedUiState(
    val allUser:     List<UserData>     = listOf(),
    val allTrap:     List<TrapData>     = listOf(),
    val latestUser:  UserData           = UserData(0, "", 0.0, 0.0, 0.0),
    val skillTime:   String             = "",
    val limitTime:   String             = "",
    val isOverSkillTime: Boolean = true,
    val isOverLimitTime: Boolean = false,
    val isOverTrapTime:  Boolean = false
)

@HiltViewModel
class BeTrappedFragmentViewModel @Inject constructor(
    private val trapRepository: TrapRepository,
    private val userRepository: UserRepository,
    private val apiRepository: ApiRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(BeTrappedUiState())
    val uiState: StateFlow<BeTrappedUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.allUsers.collect { allUsers ->
                _uiState.update { beTrappedUiState ->
                    beTrappedUiState.copy(allUser = allUsers)
                }
            }
        }
        viewModelScope.launch {
            trapRepository.allTraps.collect { allTraps ->
                _uiState.update { beTrappedUiState ->
                    beTrappedUiState.copy(allTrap = allTraps)
                }
            }
        }
    }

    fun getNowUser() {
        viewModelScope.launch {
            val latestUser = userRepository.getLatest()
            _uiState.update { beTrappedUiState ->
                beTrappedUiState.copy(latestUser = latestUser)
            }
        }
    }

    fun postTrapRoom(isMine: Int) = viewModelScope.launch {
        Log.d("USER_TRAP", userRepository.getLatest().toString())
        val nowUser = userRepository.getLatest()
        val trap = TrapData(0, nowUser.latitude, nowUser.longitude, nowUser.altitude, isMine)
        trapRepository.insert(trap)
    }

    fun setSkillTime() = viewModelScope.launch {
        val nowUser = userRepository.getLatest()
        _uiState.update { beTrappedUiState ->
            beTrappedUiState.copy(skillTime = nowUser.relativeTime)
        }
    }

    fun setSkillTimeInit(skillTime: String) {
        _uiState.update { beTrappedUiState ->
            beTrappedUiState.copy(skillTime = skillTime)
        }
    }

    // 相対時間が制限時間を超えてたらtrueを返す
    fun compareTime(relativeTime: String, limitTime: String) {
        _uiState.update { beTrappedUiState ->
            beTrappedUiState.copy(isOverLimitTime = relativeTime.substring(0, 2) == limitTime.substring(0, 2) && relativeTime.substring(3, 5) == limitTime.substring(3, 5) && relativeTime.substring(6) > limitTime.substring(6))
        }
    }

    fun compareSkillTime(relativeTime: String, skillTime: String) {
        Log.d("CompareSkillTime", "relative: $relativeTime, skill: $skillTime")
        if (relativeTime.substring(6, 8) == skillTime.substring(6, 8)) {
            _uiState.update { beTrappedUiState ->
                beTrappedUiState.copy(isOverSkillTime = relativeTime != skillTime)
            }
        }
    }

    fun compareTrapTime(relativeTime: String, trapTime: String) {
        Log.d("CompareTrapTime", "relative: $relativeTime, trap: $trapTime")
        if (relativeTime.substring(6, 8) == trapTime.substring(6, 8)) {
            _uiState.update { beTrappedUiState ->
                beTrappedUiState.copy(isOverTrapTime = relativeTime != trapTime)
            }
        }
    }

    fun howProgressSkillTime(relativeTime: String, skillTime: String): Int {
        Log.d("HowProgress", ((60 + relativeTime.substring(6).toInt() - skillTime.substring(6).toInt()) % 60).toString())
        return if (relativeTime.substring(6).toInt() < skillTime.substring(6).toInt()) {
            (60 + relativeTime.substring(6).toInt() - skillTime.substring(6).toInt()) % 60
        } else {
            relativeTime.substring(6).toInt() - skillTime.substring(6).toInt()
        }
    }

    fun howProgressTrapTime(relativeTime: String, trapTime: String): Int {
        Log.d("HowProgressTrap", ((60 + relativeTime.substring(6).toInt() - trapTime.substring(6).toInt()) % 60).toString())
        return if (relativeTime.substring(6).toInt() < trapTime.substring(6).toInt()) {
            (60 + relativeTime.substring(6).toInt() - trapTime.substring(6).toInt()) % 60
        } else {
            relativeTime.substring(6).toInt() - trapTime.substring(6).toInt()
        }
    }

    fun setIsOverSkillTime(p0: Boolean) {
        _uiState.update { beTrappedUiState ->
            beTrappedUiState.copy(isOverSkillTime = p0)
        }
    }

    fun postTrapSpacetime() {
        viewModelScope.launch {
            val nowUser = userRepository.getLatest()
            try {
                val request = PostData.PostSpacetime(nowUser.relativeTime.substring(0, 7) + "0", nowUser.latitude, nowUser.longitude, nowUser.altitude, 1)
                val response = apiRepository.postSpacetime(request)
                if (response.isSuccessful) {
                    Log.d("POST_TEST", "${response}\n${response.body()}")
                } else {
                    Log.d("POST_TEST", "$response")
                }
            } catch (e: java.lang.Exception) {
                Log.d("POST_TEST", "$e")
            }
        }
    }
}
