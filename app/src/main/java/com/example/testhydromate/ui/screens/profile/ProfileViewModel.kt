package com.example.testhydromate.ui.screens.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.testhydromate.data.model.ReminderSettings
import com.example.testhydromate.data.model.User
import com.example.testhydromate.data.repository.AuthRepository
import com.example.testhydromate.data.repository.ReminderRepository
import com.example.testhydromate.util.WaterReminderWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val reminderRepository: ReminderRepository, // Inject ReminderRepository
    @ApplicationContext private val context: Context
) : ViewModel() {

    var userData by mutableStateOf<User?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    // --- PERBAIKAN UTAMA DI SINI ---
    // Tambahkan tipe eksplisit ": StateFlow<ReminderSettings?>" agar tidak error
    val isReminderEnabled: StateFlow<ReminderSettings?> = reminderRepository.reminderSettings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    init {
        fetchUserProfile()
    }

    private fun fetchUserProfile() {
        viewModelScope.launch {
            isLoading = true
            userData = authRepository.getUserProfile()
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

    // Fungsi untuk mengubah status Toggle Reminder
    fun setReminderEnabled(enabled: Boolean) {
        viewModelScope.launch {
            // 1. Simpan ke DataStore
            reminderRepository.updateSwitch("enabled", enabled)

            // 2. Reschedule Worker (agar settingan baru langsung terbaca)
            WaterReminderWorker.schedule(context, enabled)
        }
    }

    fun logout() {
        // Matikan notifikasi saat logout
        WorkManager.getInstance(context).cancelUniqueWork("WaterReminderWork")
        authRepository.logout()
    }
}