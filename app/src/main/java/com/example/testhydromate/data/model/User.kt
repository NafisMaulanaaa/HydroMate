package com.example.testhydromate.data.model

data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
//    val birthDate: String = "",
//    val phoneNumber: String = "",
//    val countryCode: String = "",

    val gender: String = "",
    val weight: Int = 0,
    val height: Int = 0,
    val age: Int = 0,

//    val wakeTime: String = "",
//    val bedTime: String = "",
    val activityLevel: String = "",
    val weatherCondition: String = "",
    val dailyGoal: Int = 0,

    val preferredAmount: Int = 100,

    // Streak
    val currentStreak: Int = 0,
    val lastStreakDate: String = ""
)