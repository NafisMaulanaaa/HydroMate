package com.example.testhydromate.data.repository

import com.example.testhydromate.data.model.WaterLog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class WaterRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    private val userId get() = auth.currentUser?.uid

    suspend fun addDrink(amount: Int) {
        val uid = userId ?: return
        val data = hashMapOf(
            "amount" to amount.toInt(),
            "timestamp" to System.currentTimeMillis(),
            "userId" to uid
        )

        println("REPOS_CHECK: Menulis ke Firestore amount = $amount")

        firestore.collection("water_logs").add(data).await()
    }

    fun getAllHistoryRealtime(): Flow<List<WaterLog>> = callbackFlow {
        val uid = userId
        if (uid == null) {
            trySend(emptyList())
            return@callbackFlow
        }

        val subscription = firestore.collection("water_logs")
            .whereEqualTo("userId", uid)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener

                val logs = snapshot?.documents?.mapNotNull { doc ->
                    val log = doc.toObject(WaterLog::class.java)
                    log?.copy(id = doc.id)
                } ?: emptyList()

                trySend(logs)
            }

        awaitClose { subscription.remove() }
    }

    suspend fun deleteLog(logId: String) {
        firestore.collection("water_logs").document(logId).delete().await()
    }

    suspend fun updateLog(log: WaterLog) {
        firestore.collection("water_logs").document(log.id)
            .update(mapOf("amount" to log.amount))
            .await()
    }
}