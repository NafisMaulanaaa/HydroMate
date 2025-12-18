package com.example.testhydromate.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testhydromate.data.model.WaterLog
import com.example.testhydromate.data.repository.AuthRepository
import com.example.testhydromate.data.repository.WaterRepository
import com.example.testhydromate.ui.components.*
import com.example.testhydromate.ui.screens.history.ConfirmDeleteDialog
import com.example.testhydromate.ui.screens.history.showDeleteToast
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

    var selectedAmount by mutableStateOf(100)
        private set

    init {
        loadUserProfile()
        loadPreferredAmount()
    }

    fun drink(amount: Int) {
        // Kita paksa kirim 50 (angka yang kamu mau) buat ngetes doang
        // Kalau di Firebase muncul 50, berarti masalahnya ada di UI/BottomSheet
        // Kalau di Firebase TETAP muncul 100, berarti ada masalah di Repository atau Firebase Cache
        viewModelScope.launch {
            waterRepository.addDrink(50)
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

    /*fun updateSelectedAmount(amount: Int) {
        println("INFO: Menghapus keraguan, angka baru adalah: $amount")
        selectedAmount = amount
        savePreferredAmount(amount)
    }*/

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
}