package com.example.testhydromate.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testhydromate.data.model.WaterLog
import com.example.testhydromate.data.repository.WaterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: WaterRepository
) : ViewModel() {

    val waterLogs: StateFlow<List<WaterLog>> = repository.getAllHistoryRealtime()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteLog(log: WaterLog) {
        viewModelScope.launch {
            repository.deleteLog(log.id)
        }
    }

    fun updateLog(log: WaterLog) {
        viewModelScope.launch {
            repository.updateLog(log)
        }
    }
}