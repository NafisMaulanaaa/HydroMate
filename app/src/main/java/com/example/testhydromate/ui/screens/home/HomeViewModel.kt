package com.example.testhydromate.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testhydromate.data.model.WaterLog
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

    private val _history = MutableStateFlow<List<WaterLog>>(emptyList())
    val history = _history.asStateFlow()

    private val _dailyTarget = MutableStateFlow(2000)
    val dailyTarget = _dailyTarget.asStateFlow()

    init {
        refresh()
        loadUserProfile()
    }

    fun drink(amount: Int) {
        viewModelScope.launch {
            // UPDATE UI DULU (biar instan)
            _totalDrink.value += amount

            waterRepository.addDrink(amount)
            refresh()
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _totalDrink.value = waterRepository.getTodayTotal()
            _history.value = waterRepository.getAllHistory().take(10)
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            authRepository.getUserProfile()?.let {
                if (it.dailyGoal > 0) {
                    _dailyTarget.value = it.dailyGoal
                }
            }
        }
    }
}
