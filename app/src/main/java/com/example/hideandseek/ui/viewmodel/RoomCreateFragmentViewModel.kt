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

@HiltViewModel
class RoomCreateFragmentViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
) : ViewModel() {
    fun postRoom(secretWords: String) {
        viewModelScope.launch {
            try {
                val request = PostData.PostRoom(secretWords, 0)
                Log.d("POST_TEST_ROOM", request.toString())
                val response = apiRepository.postRoom(request)
                if (response.isSuccessful) {
                    Log.d("POST_TEST_ROOM", "${response}\n${response.body()}")
                } else {
                    Log.d("POST_TEST_ROOM", "$response")
                }
            } catch (e: java.lang.Exception) {
                Log.d("POST_TEST_ROOM", "$e")
            }
        }
    }
}