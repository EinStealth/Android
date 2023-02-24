package com.example.hideandseek.ui.viewmodel

import android.graphics.Bitmap
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hideandseek.data.datasource.local.LocationData
import com.example.hideandseek.data.datasource.local.TrapData
import com.example.hideandseek.data.datasource.local.UserData
import com.example.hideandseek.data.datasource.remote.PostData
import com.example.hideandseek.data.repository.*
import com.example.hideandseek.domain.CalculateRelativeTimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

data class MainUiState(
    val allLocation: List<LocationData> = listOf(),
    val allUser:     List<UserData>     = listOf(),
    val allTrap:     List<TrapData>     = listOf(),
    val myLocation:  Location?           = null,
    val latestUser:  UserData           = UserData(0, "", 0.0, 0.0, 0.0),
    val skillTime:   String             = "",
    val limitTime:   String             = "",
    val isOverSkillTime: Boolean = true,
    val isOverLimitTime: Boolean = false,
    val map: Bitmap? = null
)

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val trapRepository: TrapRepository,
    private val userRepository: UserRepository,
    private val apiRepository: ApiRepository,
    private val mapRepository: MapRepository,
    private val myLocationRepository: MyLocationRepository,
    private val calculateRelativeTimeUseCase: CalculateRelativeTimeUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            locationRepository.allLocations.collect { allLocations ->
                _uiState.update { mainUiState ->
                    mainUiState.copy(allLocation = allLocations)
                }
            }
        }
        viewModelScope.launch {
            userRepository.allUsers.collect { allUsers ->
                _uiState.update { mainUiState ->
                    mainUiState.copy(allUser = allUsers)
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
        viewModelScope.launch {
            calculateRelativeTimeUseCase().collect{ userData ->
                _uiState.update { mainUiState ->
                    mainUiState.copy(latestUser = userData)
                }
            }
//            myLocationRepository.start()
//            myLocationRepository.flowLatestLocation.collect { myLocation ->
//                _uiState.update { mainUiState ->
//                    mainUiState.copy(myLocation = myLocation)
//                }
//            }
        }
    }

    fun getNowUser() {
        viewModelScope.launch {
            val latestUser = userRepository.getLatest()
            _uiState.update { mainUiState ->
                mainUiState.copy(latestUser = latestUser)
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
        _uiState.update { mainUiState ->
            mainUiState.copy(skillTime = nowUser.relativeTime)
        }
    }

    fun setSkillTImeString(skillTime: String) {
        _uiState.update { mainUiState ->
            mainUiState.copy(skillTime = skillTime)
        }
    }

    // RelativeTime+15分の時間を制限時間とする
    fun setLimitTime(relativeTime: String) {
        val limitTime: String
        if (relativeTime.substring(3, 5).toInt() < 45) {
            limitTime = relativeTime.substring(0, 3) + (relativeTime.substring(3, 5).toInt() + 15).toString() + relativeTime.substring(5)
        } else if (relativeTime.substring(3, 5).toInt() < 55) {
            limitTime = if (relativeTime.substring(0, 2).toInt() == 23) {
                "00:0" + ((relativeTime.substring(3, 5).toInt() + 15) % 60).toString() + relativeTime.substring(5)
            } else if (relativeTime.substring(0, 2).toInt() >= 9) {
                (relativeTime.substring(0, 2).toInt() + 1).toString() + ":0" + ((relativeTime.substring(3, 5).toInt() + 15) % 60).toString() + relativeTime.substring(5)
            } else {
                "0" + (relativeTime.substring(0, 2).toInt() + 1).toString() + ":0" + ((relativeTime.substring(3, 5).toInt() + 15) % 60).toString() + relativeTime.substring(5)
            }
        } else {
            limitTime = if (relativeTime.substring(0, 2).toInt() == 23) {
                "00:" + ((relativeTime.substring(3, 5).toInt() + 15) % 60).toString() + relativeTime.substring(5)
            } else if (relativeTime.substring(0, 2).toInt() >= 9) {
                (relativeTime.substring(0, 2).toInt() + 1).toString() + ":" + ((relativeTime.substring(3, 5).toInt() + 15) % 60).toString() + relativeTime.substring(5)
            } else {
                "0" + (relativeTime.substring(0, 2).toInt() + 1).toString() + ":" + ((relativeTime.substring(3, 5).toInt() + 15) % 60).toString() + relativeTime.substring(5)
            }
        }
        _uiState.update { mainUiState ->
            mainUiState.copy(limitTime = limitTime)
        }
    }

    // 相対時間が制限時間を超えてたらtrueを返す
    fun compareTime(relativeTime: String, limitTime: String) {
        _uiState.update { mainUiState ->
            mainUiState.copy(isOverLimitTime = relativeTime.substring(0, 2) == limitTime.substring(0, 2) && relativeTime.substring(3, 5) == limitTime.substring(3, 5) && relativeTime.substring(6) > limitTime.substring(6))
        }
    }

    fun compareSkillTime(relativeTime: String, skillTime: String) {
        Log.d("CompareSkillTime", "relative: $relativeTime, skill: $skillTime")
        if (relativeTime.substring(6, 8) == skillTime.substring(6, 8)) {
            _uiState.update { mainUiState ->
                mainUiState.copy(isOverSkillTime = relativeTime != skillTime)
            }
        }
    }

    fun checkCaughtTrap(user: UserData, trap: TrapData): Boolean {
        // UserがTrapと一定の距離に来たかどうかを返す
        Log.d("checkCaughtTrap", (abs(user.latitude - trap.latitude) + abs(user.longitude - trap.longitude)).toString())
        // 自分の罠の場合は当たり判定を行わない
        if (trap.objId == 0) {
            return false
        }
        // 緯度・経度1どの違いで約100kmの差
        // よって0.00001の差で1m程度の差になる
        // 今回は0.000001以内、つまり10cm以内に入ったら当たった判定
        return (abs(user.latitude - trap.latitude) < 0.000001 && abs(user.longitude - trap.longitude) < 0.000001)
    }

    fun postOthersTrap(trap: TrapData) {
        viewModelScope.launch {
            trapRepository.insert(trap)
        }
    }

    fun deleteTrap(trap: TrapData) {
        viewModelScope.launch {
            trapRepository.delete(trap)
        }
    }

    fun deleteLocation(location: LocationData) {
        viewModelScope.launch {
            locationRepository.delete(location)
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

    fun setIsOverSkillTime(p0: Boolean) {
        _uiState.update { mainUiState ->
            mainUiState.copy(isOverSkillTime = p0)
        }
    }

    private fun setMap(p0: Bitmap) {
        _uiState.update { mainUiState ->
            mainUiState.copy(map = p0)
        }
    }

    fun postTrapSpacetime(type: String) {
        viewModelScope.launch {
            val nowUser = userRepository.getLatest()
            try {
                var request = PostData.PostSpacetime(nowUser.relativeTime.substring(0, 7) + "0", nowUser.latitude, nowUser.longitude, nowUser.altitude, 1)
                if (type == "delete") {
                    request.objId = -1
                }
                Log.d("POST_TEST", request.toString())
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
    fun fetchMap(url: String) {
        viewModelScope.launch {
            val fetchedMap = mapRepository.fetchMap(url)
            setMap(fetchedMap)
        }
    }
}
