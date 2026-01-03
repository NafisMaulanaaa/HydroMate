package com.example.testhydromate.ui.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testhydromate.data.model.User
import com.example.testhydromate.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: AuthRepository,
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

    // --- FUNGSI BARU: UPDATE PROFILE ---
    fun updateUserProfile(
        fullName: String,
        gender: String,
        age: String,
        weight: String,
        height: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            try {
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                    ?: throw Exception("User not logged in")

                // Pecah Nama Lengkap menjadi First dan Last Name
                val names = fullName.trim().split(" ")
                val firstName = names.getOrNull(0) ?: ""
                val lastName = if (names.size > 1) names.drop(1).joinToString(" ") else ""

                // Siapkan data Map untuk Firestore
                val updates = mapOf(
                    "firstName" to firstName,
                    "lastName" to lastName,
                    "gender" to gender,
                    "age" to (age.toIntOrNull() ?: 0),
                    "weight" to (weight.toDoubleOrNull() ?: 0.0),
                    "height" to (height.toDoubleOrNull() ?: 0.0)
                )

                // Update langsung ke Firestore
                FirebaseFirestore.getInstance().collection("users")
                    .document(uid)
                    .update(updates)
                    .await()

                // Refresh data lokal setelah update berhasil
                fetchUserProfile()
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Failed to update profile")
            } finally {
                isLoading = false
            }
        }
    }

    fun logout() {
        repository.logout()
    }
}
