package com.example.testhydromate.data.model

data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",

    val gender: String = "",
    val weight: Int = 0,
    val height: Int = 0,
    val age: Int = 0,

    val activityLevel: String = "",
    val weatherCondition: String = "",
    val dailyGoal: Int = 0,

    val preferredAmount: Int = 100,

    val currentStreak: Int = 0,
    val lastStreakDate: String = ""
)