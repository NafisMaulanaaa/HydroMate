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

    val dailyTarget = 2000 // ML

    init {
        loadData()
    }

    fun drink(amount: Int) {
        viewModelScope.launch {
            waterRepository.addDrink(amount)
            loadData()
        }
    }

    fun logout() {
        authRepository.logout()
    }

    private fun loadData() {
        viewModelScope.launch {
            _totalDrink.value = waterRepository.getTodayTotal()
            _history.value = waterRepository.getHistory()
        }
    }
}
