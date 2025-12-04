package com.example.testhydromate.ui.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testhydromate.data.repository.AuthRepository
import com.example.testhydromate.data.model.User
import com.example.testhydromate.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    // State untuk menyimpan status: Loading / Success / Error
    var authState by mutableStateOf<Resource<String>?>(null)
        private set

    // Fungsi Login
    fun login(email: String, pass: String) {
        viewModelScope.launch {
            authState = Resource.Loading()
            val result = repository.loginUser(email, pass)
            authState = result
        }
    }

    // Fungsi Register (Menerima data lengkap)
    fun register(
        firstName: String,
        lastName: String,
        email: String,
        pass: String,
        birthDate: String,
        phoneNumber: String,
        countryCode: String
    ) {
        viewModelScope.launch {
            authState = Resource.Loading()

            // Buat object User baru
            val newUser = User(
                firstName = firstName,
                lastName = lastName,
                email = email,
                birthDate = birthDate,
                phoneNumber = phoneNumber,
                countryCode = countryCode
            )

            val result = repository.registerUser(email, pass, newUser)
            authState = result
        }
    }

    // Fungsi untuk mereset status (misal setelah muncul Toast error)
    fun resetState() {
        authState = null
    }
}