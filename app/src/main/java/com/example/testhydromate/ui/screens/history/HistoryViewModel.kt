package com.example.testhydromate.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testhydromate.data.model.WaterLog
import com.example.testhydromate.data.repository.WaterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
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

    fun formatDateHeader(date: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateObj = sdf.parse(date) ?: return date
        val today = sdf.format(Date())
        val cal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
        val yesterday = sdf.format(cal.time)

        return when (date) {
            today -> "Today"
            yesterday -> "Yesterday"
            else -> SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(dateObj)
        }
    }
}