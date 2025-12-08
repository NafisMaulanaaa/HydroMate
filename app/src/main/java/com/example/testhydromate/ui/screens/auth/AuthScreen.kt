package com.example.testhydromate.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testhydromate.ui.components.AuthHeader
import com.example.testhydromate.ui.components.AuthTabSelector
import com.example.testhydromate.ui.components.HydroTextField
import com.example.testhydromate.ui.components.HydroPrimaryButton
import com.example.testhydromate.ui.components.HydroPasswordTextField
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testhydromate.util.Resource
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast

import com.example.testhydromate.data.model.southeastAsiaCountryList


@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterSuccess: () -> Unit,
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
                // FILTER ERRORNYA
                val msg = state.message ?: "Unknown Error"

                // Kalau errornya soal config, CUEKIN AJA (Siapa tau loginnya tetep berhasil)
                if (!msg.contains("CONFIGURATION_NOT_FOUND")) {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                }

                viewModel.resetState()
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
                .padding(top = 80.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthHeader()
            Spacer(Modifier.height(24.dp))
            AuthTabSelector(isLogin = isLogin) { selected -> isLogin = selected }
            Spacer(Modifier.height(24.dp))

            if (isLogin) {
                // 4. Oper ViewModel ke Form
                LoginForm(viewModel)
            } else {
                SignUpForm(viewModel)
            }
        }
        if (state is Resource.Loading){
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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
                emailError = if (!isValidEmail(it)) "Invalid email format" else null
            },
            placeholder = "youremail@gmail.com",
            errorMessage = emailError
        )

        Spacer(Modifier.height(16.dp))

        HydroPasswordTextField(
            value = password,
            onValueChange = { password = it }
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it })
                Text("Remember me", fontSize = 12.sp)
            }
            Text("Forgot Password?", color = Color(0xff1d61e7), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }

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

    // Date & Country
    var birthDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val countryList = remember { southeastAsiaCountryList } // Pastikan variable ini ada
    var selectedCountry by remember { mutableStateOf(countryList.first()) }
    var showCountryDialog by remember { mutableStateOf(false) }
    var phoneNumber by remember { mutableStateOf("") }

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
                emailError = if (!isValidEmail(it)) "Invalid email format" else null
            },
            placeholder = "youremail@gmail.com",
            errorMessage = emailError
        )

        Spacer(Modifier.height(16.dp))

//        // Date Picker
//        HydroClickableTextField(
//            label = "Birth of date",
//            value = birthDate,
//            placeholder = "12/12/2000",
//            onClick = { showDatePicker = true }
//        )

//        if (showDatePicker) {
//            DatePickerDialog(
//                onDismissRequest = { showDatePicker = false },
//                confirmButton = {
//                    TextButton(onClick = {
//                        datePickerState.selectedDateMillis?.let { millis ->
//                            val format = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
//                            birthDate = format.format(java.util.Date(millis))
//                        }
//                        showDatePicker = false
//                    }) { Text("OK") }
//                },
//                dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Cancel") } }
//            ) { DatePicker(state = datePickerState) }
//        }
//
//        Spacer(Modifier.height(16.dp))

//        HydroTextField(
//            label = "Phone Number",
//            value = phoneNumber,
//            onValueChange = { phoneNumber = it },
//            placeholder = "",
//            leadingIcon = {
//                Row(
//                    modifier = Modifier
//                        .fillMaxHeight()
//                        .clickable { showCountryDialog = true }
//                        .padding(start = 16.dp, end = 6.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(selectedCountry.flag, fontSize = 18.sp)
//                    Spacer(Modifier.width(6.dp))
//                    Text(selectedCountry.code, fontSize = 14.sp)
//                    Spacer(Modifier.width(4.dp))
//                    Icon(Icons.Default.ArrowDropDown, null, tint = Color.Gray)
//                }
//            }
//        )

//        // Country Picker Dialog (Diasumsikan sudah ada komponen ini)
//        if (showCountryDialog) {
//            CountryPickerDialog(
//                isOpen = showCountryDialog,
//                countryList = countryList,
//                onDismiss = { showCountryDialog = false },
//                onCountrySelected = {
//                    selectedCountry = it
//                    showCountryDialog = false
//                }
//            )
//        }

        Spacer(Modifier.height(16.dp))

        HydroPasswordTextField(
            label = "Set Password",
            value = password,
            onValueChange = {
                password = it
                passwordError = if (!isStrongPassword(it)) "Min 8 chars, Upper, Lower, Number, Symbol" else null
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
//                    birthDate = birthDate,
//                    phoneNumber = phoneNumber,
//                    countryCode = selectedCountry.code
                )
            }
        )
    }
}

// UTILS (Validation)
fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    return emailRegex.matches(email)
}

fun isStrongPassword(password: String): Boolean {
    val hasUpper = password.any { it.isUpperCase() }
    val hasLower = password.any { it.isLowerCase() }
    val hasDigit = password.any { it.isDigit() }
    val hasSymbol = password.any { !it.isLetterOrDigit() }
    return password.length >= 8 && hasUpper && hasLower && hasDigit && hasSymbol
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