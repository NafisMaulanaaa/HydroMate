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

    // 🎯 Achievement flag
    private val _shouldShowAchievement = MutableStateFlow(false)
    val shouldShowAchievement = _shouldShowAchievement.asStateFlow()

    // Track apakah sudah pernah achieve hari ini
    private var hasAchievedToday = false
    private var lastCheckedDate = ""

    var selectedAmount by mutableStateOf(100)
        private set

    init {
        loadUserProfile()
        loadPreferredAmount()
        checkAndResetDailyAchievement()

        // Monitor perubahan total untuk detect achievement
        viewModelScope.launch {
            totalDrink.collect { currentTotal ->
                val currentDate = getCurrentDateString()

                // Reset achievement jika hari berganti
                if (lastCheckedDate != currentDate) {
                    hasAchievedToday = false
                    lastCheckedDate = currentDate
                }

                // Cek apakah baru saja mencapai target dan belum pernah achieve hari ini
                if (!hasAchievedToday && currentTotal >= _dailyTarget.value && _dailyTarget.value > 0) {
                    _shouldShowAchievement.value = true
                    hasAchievedToday = true
                }
            }
        }
    }

    private fun getCurrentDateString(): String {
        val calendar = Calendar.getInstance()
        return "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH)}-${calendar.get(Calendar.DAY_OF_MONTH)}"
    }

    private fun checkAndResetDailyAchievement() {
        lastCheckedDate = getCurrentDateString()
    }

    fun drink(amount: Int) {
        viewModelScope.launch {
            waterRepository.addDrink(amount)
        }
    }

    fun deleteLog(log: WaterLog) {
        viewModelScope.launch {
            waterRepository.deleteLog(log.id)
        }
    }

    fun updateLog(log: WaterLog) {
        viewModelScope.launch {
            waterRepository.updateLog(log)
        }
    }

    private fun savePreferredAmount(amount: Int) {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid)
            .set(mapOf("preferredAmount" to amount), SetOptions.merge())
    }

    private fun loadPreferredAmount() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                val saved = doc.getLong("preferredAmount")?.toInt()
                if (saved != null) {
                    selectedAmount = saved
                }
            }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            authRepository.getUserProfile()?.let {
                if (it.dailyGoal > 0) _dailyTarget.value = it.dailyGoal
            }
        }
    }

    fun drinkUsingSelectedAmount() {
        val amountToSave = selectedAmount
        viewModelScope.launch {
            waterRepository.addDrink(amountToSave)
        }
    }

    fun updateSelectedAmount(amount: Int) {
        println("INFO: Menghapus keraguan, angka baru adalah: $amount")
        selectedAmount = amount
        savePreferredAmount(amount)
    }

    // Reset achievement flag setelah ditampilkan
    fun resetAchievementFlag() {
        _shouldShowAchievement.value = false
    }
}