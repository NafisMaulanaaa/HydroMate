package com.example.testhydromate.data.model

import java.util.Date

// Model data khusus untuk Chart
data class DailyChartData(
    val dayName: String, // e.g., "Mon", "16"
    val dateFull: Date,
    val totalAmount: Int, // Total ml
    val completionPercent: Float // 0.0 - 1.0+
)
