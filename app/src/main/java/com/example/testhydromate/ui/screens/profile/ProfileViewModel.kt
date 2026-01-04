package com.example.testhydromate.ui.screens.profile

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.example.testhydromate.data.model.User
import com.example.testhydromate.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: AuthRepository,
    @ApplicationContext private val context: Context // 1. Tambahkan Inject Context
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

                val names = fullName.trim().split(" ")
                val firstName = names.getOrNull(0) ?: ""
                val lastName = if (names.size > 1) names.drop(1).joinToString(" ") else ""

                val updates = mapOf(
                    "firstName" to firstName,
                    "lastName" to lastName,
                    "gender" to gender,
                    "age" to (age.toIntOrNull() ?: 0),
                    "weight" to (weight.toDoubleOrNull() ?: 0.0),
                    "height" to (height.toDoubleOrNull() ?: 0.0)
                )

                FirebaseFirestore.getInstance().collection("users")
                    .document(uid)
                    .update(updates)
                    .await()

                fetchUserProfile()
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Failed to update profile")
            } finally {
                isLoading = false
            }
        }
    }

    // --- FUNGSI LOGOUT YANG DIPERBARUI ---
    fun logout() {
        // 1. Matikan Notifikasi Reminder (WorkManager)
        // String "WaterReminderWork" harus SAMA PERSIS dengan yang ada di WaterReminderWorker.kt
        WorkManager.getInstance(context).cancelUniqueWork("WaterReminderWork")

        // 2. Logout dari Firebase
        repository.logout()
    }
}