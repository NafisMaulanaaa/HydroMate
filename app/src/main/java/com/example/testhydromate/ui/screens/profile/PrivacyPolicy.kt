package com.example.testhydromate.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testhydromate.R
import com.example.testhydromate.ui.components.PrimaryBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicy(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        // 1. Pastikan seluruh background Scaffold berwarna putih
        containerColor = Color.White,
        topBar = {
            // 2. Custom Top Bar agar konsisten dengan My Profile & About App
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding() // Mencegah mepet ke atas
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = PrimaryBlue
                    )
                }

                Text(
                    text = "Privacy Policy",
                    color = PrimaryBlue,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White) // 3. Pastikan konten juga berlatar putih
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 16.dp)
        ) {
            // Spacer(modifier = Modifier.height(16.dp)) // Bisa dihapus karena sudah ada padding di judul

            // --- Information we collect ---
            Text(
                text = "Information we collect",
                color = PrimaryBlue,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Categories and types of information you share with us or give us permission to obtain",
                color = Color(0xff5c6268),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = "When you create an account on HydroMate, we collect the information you provide through text fields, including contact and identifying information such as your full name, email address, and password. Passwords are stored securely in encrypted form.\n\nAfter signing in, we collect additional personal details that help us calculate your daily hydration needs, including your gender, height, weight, and age.\n\nYou also provide information about your activity level (light activity, moderate active, very active) and the weather condition you select (hot, temperature, or cold). These inputs are essential for generating your recommended daily water intake.\n\nWe may also automatically collect limited technical information, such as device type, app usage logs, and diagnostic data, to help us improve app performance.\n\nWe do not collect photos, contacts, GPS location, or social media data.",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 18.sp
                ),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // --- How we use the information ---
            Text(
                text = "How we use the information we collect, and legal bases for use",
                color = PrimaryBlue,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "We process your information to:",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "To fulfill a contract or provide the core app functionality, including:",
                color = Color(0xff5c6268),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "• Allowing you to create and sign in to your account.\n• Calculating your personalized daily water intake based on the data you provide.\n• Sending hydration reminders and maintaining your app settings.",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 18.sp
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // --- Contact Us ---
            Text(
                text = "Contact Us",
                color = PrimaryBlue,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "If you have any questions about this Privacy Policy, data usage, or wish to request deletion of your account, you may contact our team at:",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 18.sp
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = "📧 Email: cust.service@hydromate.co.id\n\n🏢 Address: Kaliurang St No.Km. 14,5, Krawitan, Umbulmartani, Ngemplak, Sleman Regency, Special Region of Yogyakarta 55584",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 18.sp
                ),
                modifier = Modifier.padding(bottom = 32.dp) // Jarak ekstra di akhir scroll
            )
        }
    }
}


@Preview(widthDp = 390, heightDp = 958)
@Composable
private fun PrivacyPolicyPreview() {
    PrivacyPolicy()
}