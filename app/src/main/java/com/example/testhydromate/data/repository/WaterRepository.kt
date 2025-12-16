package com.example.testhydromate.data.repository

import com.example.testhydromate.data.model.DrinkHistory
import com.example.testhydromate.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import javax.inject.Inject

class WaterRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val userId get() = auth.currentUser?.uid

    // 1. TAMBAH MINUM
    suspend fun addDrink(amount: Int) {
        if (userId == null) return

        val drink = hashMapOf(
            "amount" to amount,
            "timestamp" to System.currentTimeMillis(),
            "userId" to userId
        )

        try {
            firestore.collection("water_logs").add(drink).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 2. HITUNG TOTAL HARI INI
    suspend fun getTodayTotal(): Int {
        if (userId == null) return 0

        // Tentukan jam 00:00:00 hari ini
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfDay = calendar.timeInMillis

        return try {
            val snapshot = firestore.collection("water_logs")
                .whereEqualTo("userId", userId)
                .whereGreaterThanOrEqualTo("timestamp", startOfDay) // Ambil data dari jam 00:00 hari ini ke atas
                .get()
                .await()

            // Jumlahkan field "amount"
            snapshot.documents.sumOf { it.getLong("amount")?.toInt() ?: 0 }
        } catch (e: Exception) {
            0
        }
    }

    // 3. AMBIL RIWAYAT (Terbaru di atas)
    suspend fun getHistory(): List<DrinkHistory> {
        if (userId == null) return emptyList()

        return try {
            val snapshot = firestore.collection("water_logs")
                .whereEqualTo("userId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING) // Urutkan dari yang terbaru
                .limit(10) // Ambil 10 terakhir saja untuk Home
                .get()
                .await()

            snapshot.documents.map { doc ->
                DrinkHistory(
                    id = doc.id,
                    amount = doc.getLong("amount")?.toInt() ?: 0,
                    timestamp = doc.getLong("timestamp") ?: 0L
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}