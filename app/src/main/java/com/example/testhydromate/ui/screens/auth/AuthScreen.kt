package com.example.testhydromate.ui.screens.auth

import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.testhydromate.ui.components.AuthHeader
import com.example.testhydromate.ui.components.AuthTabSelector
import com.example.testhydromate.ui.components.HydroTextField
import com.example.testhydromate.ui.components.HydroPrimaryButton
import com.example.testhydromate.ui.components.HydroPasswordTextField
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testhydromate.util.Resource
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.foundation.background
import android.graphics.RenderEffect
import android.graphics.Shader
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import com.example.testhydromate.ui.components.PrimaryBlue

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {},
    viewModel: AuthViewModel = hiltViewModel()
) {
    var isLogin by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val state = viewModel.authState

    LaunchedEffect(state) {
        when (state) {
            is Resource.Success -> {
                if (isLogin) onLoginSuccess() else onRegisterSuccess()
                viewModel.resetState()
            }
            is Resource.Error -> {
                val msg = state.message ?: "Unknown Error"

                if (!msg.contains("CONFIGURATION_NOT_FOUND")) {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                }

                viewModel.resetState()
            }
            else -> {}
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
                .padding(top = 80.dp, bottom = 24.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthHeader()
            Spacer(Modifier.height(24.dp))
            AuthTabSelector(isLogin = isLogin) { selected -> isLogin = selected }
            Spacer(Modifier.height(24.dp))

            if (isLogin) {
                LoginForm(viewModel)
            } else {
                SignUpForm(viewModel)
            }
        }

        if (state is Resource.Loading) {
            Box(modifier = Modifier.fillMaxSize()) {
                // BLUR BACKGROUND
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .graphicsLayer {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                renderEffect =
                                    RenderEffect.createBlurEffect(
                                        80f,
                                        80f,
                                        Shader.TileMode.CLAMP
                                    ).asComposeRenderEffect()
                            }
                        }
                )

                // OVERLAY + LOADING
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.White.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = PrimaryBlue,
                        strokeWidth = 5.dp
                    )
                }
            }
        }

    }
}

@Composable
fun LoginForm(viewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }

    Column(Modifier.fillMaxWidth()) {
        HydroTextField(
            label = "Email",
            value = email,
            onValueChange = {
                email = it
                emailError = if (!viewModel.isValidEmail(it)) "Invalid email format" else null
            },
            placeholder = "Email",
            errorMessage = emailError
        )

        Spacer(Modifier.height(16.dp))

        HydroPasswordTextField(
            value = password,
            onValueChange = { password = it }
        )

        Spacer(Modifier.height(24.dp))

        HydroPrimaryButton(
            text = "Log In",
            onClick = {
                viewModel.login(email, password)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpForm(viewModel: AuthViewModel) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Validasi state
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    Column(Modifier.fillMaxWidth()) {
        // Baris Nama Depan & Belakang
        Row(Modifier.fillMaxWidth()) {
            HydroTextField(
                label = "First Name",
                value = firstName,
                onValueChange = { firstName = it },
                placeholder = "First Name",
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            )
            HydroTextField(
                label = "Last Name",
                value = lastName,
                onValueChange = { lastName = it },
                placeholder = "Last Name",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(16.dp))

        HydroTextField(
            label = "Email",
            value = email,
            onValueChange = {
                email = it
                emailError = if (!viewModel.isValidEmail(it)) "Invalid email format" else null
            },
            placeholder = "Email",
            errorMessage = emailError
        )

        Spacer(Modifier.height(16.dp))

        Spacer(Modifier.height(16.dp))

        HydroPasswordTextField(
            label = "Set Password",
            value = password,
            onValueChange = {
                password = it
                passwordError = if (!viewModel.isStrongPassword(it)) "Min 8 chars, Upper, Lower, Number, Symbol" else null
            },
            errorMessage = passwordError
        )

        Spacer(Modifier.height(24.dp))

        HydroPrimaryButton(
            text = "Register",
            onClick = {
                viewModel.register(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    pass = password
                )
            }
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "id:pixel_9"
)

@Composable
fun AuthPreview() {
    MaterialTheme {
        LoginScreen(
            onLoginSuccess = {},
            onRegisterSuccess = {}
        )
    }
}

