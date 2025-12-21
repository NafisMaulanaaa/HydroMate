package com.example.testhydromate.ui.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testhydromate.data.model.User
import com.example.testhydromate.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: AuthRepository, // Gunakan repository, bukan hanya FirebaseAuth
) : ViewModel() {

    var userData by mutableStateOf<User?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        fetchUserProfile()
    }

    private fun fetchUserProfile() {
        viewModelScope.launch {
            isLoading = true
            userData = repository.getUserProfile()
            isLoading = false
        }
    }

    fun logout() {
        repository.logout()
    }
}
