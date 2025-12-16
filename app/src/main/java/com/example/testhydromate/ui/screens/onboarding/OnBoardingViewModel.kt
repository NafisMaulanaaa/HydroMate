package com.example.testhydromate.ui.screens.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testhydromate.data.model.User
import com.example.testhydromate.data.repository.AuthRepository
import com.example.testhydromate.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    // personal detail
    var gender by mutableStateOf("")
    var height by mutableStateOf("")
    var weight by mutableStateOf("")
    var age by mutableStateOf("")

    // habit
//    var wakeHour by mutableStateOf("")
//    var wakeMinute by mutableStateOf("")
//    var bedHour by mutableStateOf("")
//    var bedMinute by mutableStateOf("")
    var activityLevel by mutableStateOf("")

    // weather
    var weather by mutableStateOf("")
    var dailyGoal by mutableStateOf(0)
    var saveState by mutableStateOf<Resource<Boolean>?>(null)
        private set


//     RISET RUMUSNYA LAGI
//    private fun calculateDailyGoal(): Int {
//        val berat = weight.toIntOrNull() ?: 50
//        var goal = berat * 30 // rumusnya 30ml * BB
//
//        // Faktor Aktivitas
//        if (activityLevel == "Very Active") goal += 500
//        else if (activityLevel == "Moderate Active") goal += 300
//
//        // Faktor Cuaca
//        if (weather == "Hot") goal += 500
//
//        return goal
//    }

    private fun calculateWaterGoal (): Int {
        val gender = gender
        // ini data dari user string diubah ke double (biar bisa ngitung desimal) dulu
        // nanti baru ke int (ke int biar data yang disimpen sama yang ditampilin ke user gensp)
        val age = age.toDoubleOrNull() ?: 20.0
        val weight = weight.toDoubleOrNull() ?: 55.0 // dalam kg
        val height = height.toDoubleOrNull() ?: 165.0 // dalam cm

        val heightM = height / 100.0 // dalam meter

        // ini maksudnya, jadi kan user input activity level mereka gimana
        // nah tiap misal mereka milih light activity,
        // itu bakal langsung ngeset nilai pal sama is athlete kaya yang ada di dalam when
        // PAL itu physical activity level
        val (pal, isAthlete) = when (activityLevel){
            "Light Activity" -> 1.4 to 0.0
            "Moderate Active" -> 1.6 to 0.0
            "Very Active" -> 1.8 to 1.0
            else -> 1.6 to 0.0
        }


        // FFM itu fat free mass atau komposisi tubuh tanpa lemak
        val bmi = weight / (heightM * heightM)
        val ffm = if (gender.lowercase() == "male") {
            (9270.0 * weight) / (6680.0 + (216.0 * bmi))
        } else {
            (9270.0 * weight) / (8780.0 + (244.0 * bmi))
        }

        val (temp, humidity) = when (weather) {
            "Hot" ->  35.0 to 70.0
            "Temperate" -> 25.0 to 50.0
            "Cold" -> 15.0 to 40.0
            else -> 25.0 to 50.0
        }

        val altitude = 10.0 // dalam meter. diambil dari rata rata kemungkinan pengguna (perkotaan)

        // hitung water turnovernya atau jumlah total air yang diganti oleh tubuh dalam satu hari (ml/day)
        val wtMl = (861.9 * pal) +
                (37.34 * ffm) +
                (4.228 * humidity) +
                (699.7 * isAthlete) +
                (0.514 * altitude) -
                (0.3625 * (age * age))  +
                (29.42 * age) +
                (1.937 * temp * temp) -
                (23.15 * temp) -
                984.8

        // baru cari water intake nya
        // untuk mengganti kebutuhan air itu, based on penelitian  yosuke yamada
        // - Metabolisme menyumbang ~10-15% (
        // - Makanan menyumbang ~20-30%
        // - Sisa yang harus diminum (Fluids) ~55-70%
        // ini yang kita cari ambil di 65 untuk mastikan kebutuhan air benar benar terpenuhi)

        return (wtMl * 0.70).toInt()
    }

    fun submitOnboarding() {
        if (saveState is Resource.Loading) return // Cegah double submit

        dailyGoal = calculateWaterGoal()
        saveOnboardingData()
    }

    fun updateManualGoal(newGoal: Int) {
        dailyGoal = newGoal
        // Opsional: Simpan ulang ke firestore jika perlu update realtime
         saveOnboardingData()
    }

    fun saveOnboardingData() {
        viewModelScope.launch {
            saveState = Resource.Loading()

            val userUpdate = User(
                gender = gender,
                height = height.toIntOrNull() ?: 0,
                weight = weight.toIntOrNull() ?: 0,
                age = age.toIntOrNull() ?: 0,
//                wakeTime = "$wakeHour:$wakeMinute",
//                bedTime = "$bedHour:$bedMinute",
                activityLevel = activityLevel,
                weatherCondition = weather,
                dailyGoal = dailyGoal
            )

            val result = repository.updateUserProfile(userUpdate)
            saveState = result
        }
    }


}