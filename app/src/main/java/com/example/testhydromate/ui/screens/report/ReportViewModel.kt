package com.example.testhydromate.ui.screens.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testhydromate.data.model.DailyChartData
import com.example.testhydromate.data.repository.AuthRepository
import com.example.testhydromate.data.repository.WaterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val waterRepository: WaterRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    // Filter state: "Weekly", "Monthly", "Yearly"
    private val _selectedTimeRange = MutableStateFlow("Weekly")
    val selectedTimeRange = _selectedTimeRange

    // State tanggal acuan (Default: Hari ini)
    private val _referenceDate = MutableStateFlow(System.currentTimeMillis())

    // Flow kombinasi: Logs + Filter + Tanggal Acuan
    val chartData: StateFlow<List<DailyChartData>> = combine(
        waterRepository.getAllHistoryRealtime(),
        _selectedTimeRange,
        _referenceDate
    ) { logs, range, refDate ->

        val userProfile = authRepository.getUserProfile()
        val dailyGoal = if ((userProfile?.dailyGoal ?: 0) > 0) userProfile!!.dailyGoal else 2000

        when (range) {
            "Weekly" -> generateWeeklyData(logs, dailyGoal, refDate)
            "Monthly" -> generateMonthlyData(logs, dailyGoal, refDate)
            "Yearly" -> generateYearlyData(logs, dailyGoal, refDate)
            else -> generateWeeklyData(logs, dailyGoal, refDate)
        }

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- NAVIGASI TANGGAL ---

    fun setTimeRange(range: String) {
        _selectedTimeRange.value = range
        // Reset tanggal ke hari ini setiap ganti filter agar UX-nya enak
        _referenceDate.value = System.currentTimeMillis()
    }

    fun previousPeriod() {
        val cal = Calendar.getInstance()
        cal.timeInMillis = _referenceDate.value

        when (_selectedTimeRange.value) {
            "Weekly" -> cal.add(Calendar.WEEK_OF_YEAR, -1)
            "Monthly" -> cal.add(Calendar.MONTH, -1)
            "Yearly" -> cal.add(Calendar.YEAR, -1)
        }
        _referenceDate.value = cal.timeInMillis
    }

    fun nextPeriod() {
        val cal = Calendar.getInstance()
        cal.timeInMillis = _referenceDate.value

        when (_selectedTimeRange.value) {
            "Weekly" -> cal.add(Calendar.WEEK_OF_YEAR, 1)
            "Monthly" -> cal.add(Calendar.MONTH, 1)
            "Yearly" -> cal.add(Calendar.YEAR, 1)
        }
        _referenceDate.value = cal.timeInMillis
    }

    // --- DATA GENERATION LOGIC ---

    private fun generateWeeklyData(logs: List<com.example.testhydromate.data.model.WaterLog>, goal: Int, refDate: Long): List<DailyChartData> {
        val data = ArrayList<DailyChartData>()
        val calendar = Calendar.getInstance()

        // PENTING: Set waktu berdasarkan refDate, bukan hari ini
        calendar.timeInMillis = refDate
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY) // Mundur ke hari Minggu terdekat

        for (i in 0 until 7) {
            val startOfDay = getStartOfDay(calendar)
            val endOfDay = getEndOfDay(calendar)

            val dailyTotal = logs
                .filter { it.timestamp in startOfDay..endOfDay }
                .sumOf { it.amount }

            val dayName = SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.time)

            data.add(DailyChartData(
                dayName = dayName,
                dateFull = calendar.time,
                totalAmount = dailyTotal,
                completionPercent = (dailyTotal.toFloat() / goal.toFloat()) * 100
            ))
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return data
    }

    private fun generateMonthlyData(logs: List<com.example.testhydromate.data.model.WaterLog>, goal: Int, refDate: Long): List<DailyChartData> {
        val data = ArrayList<DailyChartData>()
        val calendar = Calendar.getInstance()

        // Set ke Bulan & Tahun sesuai refDate
        calendar.timeInMillis = refDate
        calendar.set(Calendar.DAY_OF_MONTH, 1) // Mulai tanggal 1

        val maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (i in 1..maxDays) {
            val startOfDay = getStartOfDay(calendar)
            val endOfDay = getEndOfDay(calendar)

            val dailyTotal = logs
                .filter { it.timestamp in startOfDay..endOfDay }
                .sumOf { it.amount }

            val dayLabel = if (i == 1 || i % 5 == 0) i.toString() else ""

            data.add(DailyChartData(
                dayName = dayLabel,
                dateFull = calendar.time,
                totalAmount = dailyTotal,
                completionPercent = (dailyTotal.toFloat() / goal.toFloat()) * 100
            ))
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return data
    }

    private fun generateYearlyData(logs: List<com.example.testhydromate.data.model.WaterLog>, dailyGoal: Int, refDate: Long): List<DailyChartData> {
        val data = ArrayList<DailyChartData>()
        val calendar = Calendar.getInstance()

        // Set ke Tahun sesuai refDate, mulai Januari
        calendar.timeInMillis = refDate
        calendar.set(Calendar.MONTH, Calendar.JANUARY)
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        for (i in 0 until 12) {
            val startOfMonth = getStartOfDay(calendar)
            val maxDayInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            val endCalendar = calendar.clone() as Calendar
            endCalendar.set(Calendar.DAY_OF_MONTH, maxDayInMonth)
            val endOfMonth = getEndOfDay(endCalendar)

            val monthlyTotal = logs
                .filter { it.timestamp in startOfMonth..endOfMonth }
                .sumOf { it.amount }

            val monthlyGoal = dailyGoal * maxDayInMonth
            val monthName = SimpleDateFormat("MMM", Locale.getDefault()).format(calendar.time)

            data.add(DailyChartData(
                dayName = monthName.first().toString(),
                dateFull = calendar.time,
                totalAmount = monthlyTotal,
                completionPercent = (monthlyTotal.toFloat() / monthlyGoal.toFloat()) * 100
            ))
            calendar.add(Calendar.MONTH, 1)
        }
        return data
    }

    private fun getStartOfDay(cal: Calendar): Long {
        val c = cal.clone() as Calendar
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        return c.timeInMillis
    }

    private fun getEndOfDay(cal: Calendar): Long {
        val c = cal.clone() as Calendar
        c.set(Calendar.HOUR_OF_DAY, 23)
        c.set(Calendar.MINUTE, 59)
        c.set(Calendar.SECOND, 59)
        c.set(Calendar.MILLISECOND, 999)
        return c.timeInMillis
    }
}