package com.example.testhydromate.ui.screens.home

import androidx.lifecycle.ViewModel
import com.example.testhydromate.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    fun logout() {
        repository.logout() // Pastikan ada fungsi logout di AuthRepository kamu
    }
}