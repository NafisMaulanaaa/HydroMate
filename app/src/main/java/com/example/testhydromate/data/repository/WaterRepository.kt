package com.example.testhydromate.data.repository

import com.example.testhydromate.data.model.WaterLog
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

    suspend fun addDrink(amount: Int) {
        val uid = userId ?: return

        val data = hashMapOf(
            "amount" to amount,
            "timestamp" to System.currentTimeMillis(),
            "userId" to uid
        )

        firestore.collection("water_logs")
            .add(data)
            .await()
    }

    suspend fun getTodayTotal(): Int {
        val uid = userId ?: return 0

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val snapshot = firestore.collection("water_logs")
            .whereEqualTo("userId", uid)
            .whereGreaterThanOrEqualTo("timestamp", calendar.timeInMillis)
            .get()
            .await()

        return snapshot.documents.sumOf {
            it.getLong("amount")?.toInt() ?: 0
        }
    }

    suspend fun getAllHistory(): List<WaterLog> {
        val uid = userId ?: return emptyList()

        val snapshot = firestore.collection("water_logs")
            .whereEqualTo("userId", uid)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .await()

        return snapshot.documents.map {
            WaterLog(
                id = it.id,
                amount = it.getLong("amount")?.toInt() ?: 0,
                timestamp = it.getLong("timestamp") ?: 0L
            )
        }
    }
}
