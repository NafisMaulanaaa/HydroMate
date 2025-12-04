package com.example.testhydromate.ui.screens.splash

import androidx.lifecycle.ViewModel
import com.example.testhydromate.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    // Cek apakah user sudah login di Firebase
    fun isUserLoggedIn(): Boolean {
        return repository.isUserLoggedIn()
    }
}