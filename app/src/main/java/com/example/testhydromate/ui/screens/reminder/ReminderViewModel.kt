package com.example.testhydromate.ui.screens.reminder

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testhydromate.data.model.ReminderSettings
import com.example.testhydromate.data.repository.ReminderRepository
import com.example.testhydromate.util.WaterReminderWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val repository: ReminderRepository,
    @ApplicationContext private val context: Context // Untuk reschedule worker
) : ViewModel() {

    val settings: StateFlow<ReminderSettings> = repository.reminderSettings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ReminderSettings())

    fun toggleSwitch(key: String, currentValue: Boolean) {
        viewModelScope.launch {
            repository.updateSwitch(key, !currentValue)

            if (key == "enabled") {
                rescheduleWorker(!currentValue, settings.value.interval)
            }
        }
    }

    fun setTime(isStart: Boolean, hour: Int, minute: Int) {
        val formatted = String.format("%02d:%02d", hour, minute)
        viewModelScope.launch {
            repository.updateTime(isStart, formatted)
        }
    }

    fun setInterval(minutes: Int) {
        viewModelScope.launch {
            repository.updateInterval(minutes)
            // Reschedule worker dengan interval baru
            rescheduleWorker(settings.value.isEnabled, minutes)
        }
    }

    fun toggleDay(dayId: String) {
        val currentDays = settings.value.repeatDays.toMutableSet()
        if (currentDays.contains(dayId)) {
            if (currentDays.size > 1) currentDays.remove(dayId)
        } else {
            currentDays.add(dayId)
        }
        viewModelScope.launch {
            repository.updateDays(currentDays)
        }
    }

    private fun rescheduleWorker(isEnabled: Boolean, interval: Int) {
        WaterReminderWorker.schedule(context, isEnabled, interval.toLong())
    }
}