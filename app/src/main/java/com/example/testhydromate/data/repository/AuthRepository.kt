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

    // --- FUNGSI LOGIN ---
    suspend fun loginUser(email: String, pass: String): Resource<String> {
        return try {
            // Coba login ke Firebase Auth
            auth.signInWithEmailAndPassword(email, pass).await()
            Resource.Success("Login Berhasil")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Login Gagal")
        }
    }

    // --- FUNGSI REGISTER ---
    suspend fun registerUser(email: String, pass: String, user: User): Resource<String> {
        return try {
            // 1. Buat Akun (Email & Pass) di Authentication
            val authResult = auth.createUserWithEmailAndPassword(email, pass).await()
            val userId = authResult.user?.uid ?: throw Exception("Gagal mendapatkan User ID")

            // 2. Siapkan data User dengan ID yang baru didapat
            val newUser = user.copy(id = userId)

            // 3. Simpan Biodata Lengkap ke Firestore di koleksi "users"
            firestore.collection("users")
                .document(userId)
                .set(newUser)
                .await()

            Resource.Success("Registrasi Berhasil")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Registrasi Gagal")
        }
    }

    // --- FUNGSI CEK STATUS (Untuk Splash Screen) ---
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // --- FUNGSI LOGOUT ---
    fun logout() {
        auth.signOut()
    }
}