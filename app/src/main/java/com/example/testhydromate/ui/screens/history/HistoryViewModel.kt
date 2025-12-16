package com.example.testhydromate.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testhydromate.data.model.WaterLog
import com.example.testhydromate.data.repository.WaterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: WaterRepository
) : ViewModel() {

    private val _waterLogs = MutableStateFlow<List<WaterLog>>(emptyList())
    val waterLogs = _waterLogs.asStateFlow()

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            _waterLogs.value = repository.getAllHistory()
        }
    }
}
