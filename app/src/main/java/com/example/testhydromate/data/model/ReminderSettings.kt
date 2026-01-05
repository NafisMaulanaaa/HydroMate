package com.example.testhydromate.data.model

data class ReminderSettings(
    val isEnabled: Boolean = true,
    val isVibration: Boolean = true,
    val isSound: Boolean = true,
    val startTime: String = "08:00",
    val endTime: String = "22:00",
    val interval: Int = 60, // Menit
    val repeatDays: Set<String> = setOf("1","2","3","4","5","6","7")
)