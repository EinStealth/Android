package com.example.hideandseek.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hideandseek.data.repository.MyInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterUserIconFragmentViewModel @Inject constructor(
    private val myInfoRepository: MyInfoRepository
): ViewModel() {
    fun writeUserIcon(icon: Int) {
        myInfoRepository.writeIcon(icon)
    }
}
