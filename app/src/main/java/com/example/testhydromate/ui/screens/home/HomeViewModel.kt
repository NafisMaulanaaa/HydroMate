package com.example.testhydromate.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testhydromate.data.model.DrinkHistory
import com.example.testhydromate.data.repository.AuthRepository
import com.example.testhydromate.data.repository.WaterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val waterRepository: WaterRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _totalDrink = MutableStateFlow(0)
    val totalDrink = _totalDrink.asStateFlow()

    private val _history = MutableStateFlow<List<DrinkHistory>>(emptyList())
    val history = _history.asStateFlow()

    // Ubah dari val biasa jadi StateFlow agar UI terupdate saat data diambil
    private val _dailyTarget = MutableStateFlow(2000)
    val dailyTarget = _dailyTarget.asStateFlow()

    init {
        loadData()
        loadUserProfile() // <-- Ambil target user
    }

    fun drink(amount: Int) {
        viewModelScope.launch {
            waterRepository.addDrink(amount) // Simpan ke Firestore
            loadData() // Refresh data total
        }
    }

    fun logout() {
        authRepository.logout()
    }

    // Ambil Target Minum dari Profil User
    private fun loadUserProfile() {
        viewModelScope.launch {
            val user = authRepository.getUserProfile()
            if (user != null && user.dailyGoal > 0) {
                _dailyTarget.value = user.dailyGoal
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _totalDrink.value = waterRepository.getTodayTotal()
            _history.value = waterRepository.getHistory()
        }
    }
}