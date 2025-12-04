package com.example.testhydromate.data.model

data class CountryItem(
    val name: String,
    val flag: String,
    val code: String
)

val southeastAsiaCountryList = listOf(
    CountryItem("Indonesia", "🇮🇩", "+62"),
    CountryItem("Brunei", "🇧🇳", "+673"),
    CountryItem("Cambodia", "🇰🇭", "+855"),
    CountryItem("Laos", "🇱🇦", "+856"),
    CountryItem("Malaysia", "🇲🇾", "+60"),
    CountryItem("Myanmar", "🇲🇲", "+95"),
    CountryItem("Philippines", "🇵🇭", "+63"),
    CountryItem("Singapore", "🇸🇬", "+65"),
    CountryItem("Thailand", "🇹🇭", "+66"),
    CountryItem("Timor-Leste", "🇹🇱", "+670"),
    CountryItem("Vietnam", "🇻🇳", "+84")
)