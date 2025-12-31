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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val waterRepository: WaterRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _streakCount = MutableStateFlow(0)
    val streakCount = _streakCount.asStateFlow()

    private val _isStreakActiveToday = MutableStateFlow(false)
    val isStreakActiveToday = _isStreakActiveToday.asStateFlow()

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

    private var hasAchievedInThisSession = false

    var selectedAmount by mutableStateOf(100)
        private set

    init {
        loadUserProfile()
        loadPreferredAmount()
        loadStreakFromDatabase()

        viewModelScope.launch {
            totalDrink.collect { currentTotal ->
                val target = _dailyTarget.value

                // JIKA TARGET MINUM HARI INI TERCAPAI
                if (currentTotal >= target && target > 0 && currentTotal > 0) {
                    // 1. Munculkan piala Achievement jika belum di sesi ini
                    if (!hasAchievedInThisSession) {
                        checkAndTriggerAchievement()
                    }

                    // 2. SIMPAN/UPDATE STREAK KE DB
                    // Fungsi ini di dalam sudah ada proteksi "if lastDate != today"
                    updateStreakLogic()
                }
            }
        }
    }

    private fun loadStreakFromDatabase() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).addSnapshotListener { snapshot, _ ->
            if (snapshot != null && snapshot.exists()) {
                val streak = snapshot.getLong("currentStreak")?.toInt() ?: 0
                val lastDate = snapshot.getString("lastStreakDate") ?: ""
                val today = getCurrentDateString()

                _streakCount.value = streak
                // Sinar piala menyala jika target sudah tercapai hari ini
                _isStreakActiveToday.value = (lastDate == today)
            }
        }
    }

    private fun checkAndTriggerAchievement() {
        val uid = auth.currentUser?.uid ?: return
        val dateKey = getCurrentDateString()

        db.collection("users").document(uid)
            .collection("daily_achievements").document(dateKey)
            .get()
            .addOnSuccessListener { doc ->
                if (!doc.exists()) {
                    _shouldShowAchievement.value = true
                    hasAchievedInThisSession = true
                    saveAchievementToFirebase(dateKey)
                } else {
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

    private fun updateStreakLogic() {
        val uid = auth.currentUser?.uid ?: return
        val today = getCurrentDateString()
        val userRef = db.collection("users").document(uid)

        userRef.get().addOnSuccessListener { doc ->
            val lastDate = doc.getString("lastStreakDate") ?: ""
            val currentStreak = doc.getLong("currentStreak")?.toInt() ?: 0

            // HANYA UPDATE JIKA TANGGAL TERAKHIR DI DB BUKAN HARI INI
            if (lastDate != today) {
                val yesterday = getYesterdayDateString()

                // Jika kemarin tercapai, streak +1. Jika bolong, reset ke 1.
                val newStreakValue = if (lastDate == yesterday) currentStreak + 1 else 1

                val updates = mapOf(
                    "currentStreak" to newStreakValue,
                    "lastStreakDate" to today
                )

                userRef.update(updates).addOnSuccessListener {
                    _isStreakActiveToday.value = true
                }
            }
        }
    }

    private fun getCurrentDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun getYesterdayDateString(): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(cal.time)
    }

    fun resetAchievementFlag() {
        _shouldShowAchievement.value = false
    }

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
