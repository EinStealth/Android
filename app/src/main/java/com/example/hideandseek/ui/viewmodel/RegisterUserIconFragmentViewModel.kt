package com.example.hideandseek.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.hideandseek.data.repository.MyInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterUserIconFragmentViewModel @Inject constructor(
    private val myInfoRepository: MyInfoRepository
) : ViewModel() {
    fun writeUserIcon(icon: Int) {
        myInfoRepository.writeIcon(icon)
    }
}
