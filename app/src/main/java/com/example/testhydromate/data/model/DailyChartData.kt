package com.example.testhydromate.data.model

import java.util.Date

// Model data khusus untuk Chart
data class DailyChartData(
    val dayName: String,
    val dateFull: Date,
    val totalAmount: Int,
    val completionPercent: Float
)
