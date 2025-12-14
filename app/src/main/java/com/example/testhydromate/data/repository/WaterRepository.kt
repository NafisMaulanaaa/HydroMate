package com.example.testhydromate.data.repository

import com.example.testhydromate.data.model.DrinkHistory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class WaterRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    private val uid get() = auth.currentUser?.uid ?: ""

    suspend fun addDrink(amount: Int) {
        val drink = DrinkHistory(
            amount = amount,
            timestamp = System.currentTimeMillis()
        )

        firestore.collection("users")
            .document(uid)
            .collection("drinks")
            .add(drink)
            .await()
    }

    suspend fun getTodayTotal(): Int {
        val snapshot = firestore.collection("users")
            .document(uid)
            .collection("drinks")
            .get()
            .await()

        return snapshot.documents.sumOf {
            it.getLong("amount")?.toInt() ?: 0
        }
    }

    suspend fun getHistory(): List<DrinkHistory> {
        val snapshot = firestore.collection("users")
            .document(uid)
            .collection("drinks")
            .get()
            .await()

        return snapshot.toObjects(DrinkHistory::class.java)
    }
}
