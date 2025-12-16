package com.example.testhydromate.data.repository

import com.example.testhydromate.data.model.User
import com.example.testhydromate.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    // LOGIN
    suspend fun loginUser(email: String, pass: String): Resource<String> {
        return try {
            auth.signInWithEmailAndPassword(email, pass).await()
            Resource.Success("Login Berhasil")
        } catch (e: Exception) {
            Resource.Error("Login Gagal")
        }
    }

    // REGISTES
    suspend fun registerUser(email: String, pass: String, user: User): Resource<String> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, pass).await()
            val userId = authResult.user?.uid ?: throw Exception("Gagal mendapatkan User ID")
            val newUser = user.copy(id = userId)

            firestore.collection("users")
                .document(userId)
                .set(newUser)
                .await()

            Resource.Success("Registrasi Berhasil")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Registrasi Gagal")
        }
    }

    // CEK STATUS LOGIN/GA
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // LOGOUT
    fun logout() {
        auth.signOut()
    }

    suspend fun updateUserProfile(user: User): Resource<Boolean> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("User not found")

            firestore.collection("users").document(userId)
                .update(
                    mapOf(
                        "gender" to user.gender,
                        "height" to user.height,
                        "weight" to user.weight,
                        "age" to user.age,
                        "activityLevel" to user.activityLevel,
                        "weatherCondition" to user.weatherCondition,
                        "dailyGoal" to user.dailyGoal,
//                        "wakeTime" to user.wakeTime,
//                        "bedTime" to user.bedTime
                    )
                ).await()

            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Gagal update profil")
        }
    }

    suspend fun getUserProfile(): User? {
        val userId = auth.currentUser?.uid ?: return null
        return try {
            val snapshot = firestore.collection("users").document(userId).get().await()
            snapshot.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

}