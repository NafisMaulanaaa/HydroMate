package com.example.testhydromate.ui.screens.profile

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth // Inject FirebaseAuth
) : ViewModel() {

    fun logout() {
        auth.signOut()
    }
}
