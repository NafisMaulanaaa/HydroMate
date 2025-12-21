package com.example.testhydromate.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testhydromate.data.model.WaterLog
import com.example.testhydromate.data.repository.AuthRepository
import com.example.testhydromate.data.repository.WaterRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val waterRepository: WaterRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    val history: StateFlow<List<WaterLog>> = waterRepository.getAllHistoryRealtime()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val totalDrink: StateFlow<Int> = history.map { logs ->
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        logs.filter { it.timestamp >= calendar.timeInMillis }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _dailyTarget = MutableStateFlow(2000)
    val dailyTarget = _dailyTarget.asStateFlow()

    private val _shouldShowAchievement = MutableStateFlow(false)
    val shouldShowAchievement = _shouldShowAchievement.asStateFlow()

    // Flag lokal agar tidak hit Firebase terus-menerus dalam satu sesi
    private var hasAchievedInThisSession = false

    var selectedAmount by mutableStateOf(100)
        private set

    init {
        loadUserProfile()
        loadPreferredAmount()

        viewModelScope.launch {
            totalDrink.collect { currentTotal ->
                val target = _dailyTarget.value

                // Syarat: Total tercapai, target valid, dan belum pernah muncul di sesi ini
                if (!hasAchievedInThisSession && currentTotal >= target && target > 0 && currentTotal > 0) {
                    checkAndTriggerAchievement()
                }
            }
        }
    }

    private fun checkAndTriggerAchievement() {
        val uid = auth.currentUser?.uid ?: return
        val dateKey = getCurrentDateString()

        // Cek ke Firestore apakah tanggal hari ini sudah pernah klaim achievement
        db.collection("users").document(uid)
            .collection("daily_achievements").document(dateKey)
            .get()
            .addOnSuccessListener { doc ->
                if (!doc.exists()) {
                    // Jika belum ada dokumen untuk hari ini, tampilkan!
                    _shouldShowAchievement.value = true
                    hasAchievedInThisSession = true
                    saveAchievementToFirebase(dateKey)
                } else {
                    // Jika sudah ada, kunci agar tidak muncul lagi
                    hasAchievedInThisSession = true
                }
            }
    }

    private fun saveAchievementToFirebase(dateKey: String) {
        val uid = auth.currentUser?.uid ?: return
        val data = mapOf("achieved" to true, "timestamp" to System.currentTimeMillis())

        db.collection("users").document(uid)
            .collection("daily_achievements").document(dateKey)
            .set(data)
    }

    private fun getCurrentDateString(): String {
        val calendar = Calendar.getInstance()
        return "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH)}-${calendar.get(Calendar.DAY_OF_MONTH)}"
    }

    fun resetAchievementFlag() {
        _shouldShowAchievement.value = false
    }

    // --- Sisanya tetap sama ---
    fun drinkUsingSelectedAmount() {
        viewModelScope.launch { waterRepository.addDrink(selectedAmount) }
    }

    fun deleteLog(log: WaterLog) {
        viewModelScope.launch { waterRepository.deleteLog(log.id) }
    }

    fun updateLog(log: WaterLog) {
        viewModelScope.launch { waterRepository.updateLog(log) }
    }

    fun updateSelectedAmount(amount: Int) {
        selectedAmount = amount
        savePreferredAmount(amount)
    }

    private fun savePreferredAmount(amount: Int) {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).set(mapOf("preferredAmount" to amount), SetOptions.merge())
    }

    private fun loadPreferredAmount() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).get().addOnSuccessListener { doc ->
            doc.getLong("preferredAmount")?.toInt()?.let { selectedAmount = it }
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            authRepository.getUserProfile()?.let {
                if (it.dailyGoal > 0) _dailyTarget.value = it.dailyGoal
            }
        }
    }
}