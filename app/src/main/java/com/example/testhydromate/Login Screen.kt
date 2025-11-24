import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourapp.ui.components.fullCountryList

@Composable
fun LoginScreen(onSignUpClick: () -> Unit = {}) {

    var isLogin by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // HEADER
        Text(
            text = "Get Started now",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = Color(0xff0e61d1),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Welcome to HydroMate! Create an account or log in to track your water intake, set reminders, and get hydrate!",
            fontSize = 12.sp,
            color = Color(0xff6c7278),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        // TABBING
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color(0xfff5f6f9), RoundedCornerShape(8.dp))
                .padding(3.dp)
        ) {
            // LOGIN TAB
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(if (isLogin) Color.White else Color.Transparent, RoundedCornerShape(6.dp))
                    .clickable { isLogin = true },
                contentAlignment = Alignment.Center
            ) {
                Text("Log In", fontWeight = FontWeight.Medium, color = if (isLogin) Color.Black else Color(0xff7d7d91))
            }

            // SIGN UP TAB
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(if (!isLogin) Color.White else Color.Transparent, RoundedCornerShape(6.dp))
                    .clickable { isLogin = false },
                contentAlignment = Alignment.Center
            ) {
                Text("Sign Up", fontWeight = FontWeight.Medium, color = if (!isLogin) Color.Black else Color(0xff7d7d91))
            }
        }

        Spacer(Modifier.height(24.dp))

        if (isLogin) LoginForm() else SignUpForm()
    }
}

@Composable
fun LoginForm() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxWidth()) {

        Text("Email", fontSize = 12.sp, color = Color(0xff6c7278))
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = !isValidEmail(email)
                            },
            placeholder = { Text("youremail@gmail.com") },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color(0xff4d81e7),
                unfocusedIndicatorColor = Color(0xffedf1f3),
                errorIndicatorColor = Color.Red
            )
        )

        if (emailError) {
            Text(
                text = "Invalid email format",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        Text("Password", fontSize = 12.sp, color = Color(0xff6c7278))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("******") },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(10.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null, tint = Color.Gray)
                }
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color(0xff4d81e7),
                unfocusedIndicatorColor = Color(0xffedf1f3)
            )
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

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xff1d61e7))
        ) {
            Text("Log In", color = Color.White)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpForm() {

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    // DATE PICKER
    var birthDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // COUNTRY PICKER (USES SCROLLABLE DIALOG)
    val countryList = remember { fullCountryList }
    var selectedCountry by remember { mutableStateOf(countryList.first()) }
    var showCountryDialog by remember { mutableStateOf(false) }

    // PHONE
    var phoneNumber by remember { mutableStateOf("") }

    // PASSWORD
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxWidth()) {

        //FIRST + LAST NAME
        Row(Modifier.fillMaxWidth()) {

            Column(modifier = Modifier.weight(1f)) {
                Text("First Name", fontSize = 12.sp, color = Color(0xff6c7278))
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(end = 8.dp),
                    placeholder = { Text("First Name") },
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color(0xff4d81e7),
                        unfocusedIndicatorColor = Color(0xffedf1f3)
                    )
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text("Last Name", fontSize = 12.sp, color = Color(0xff6c7278))
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    placeholder = { Text("Last Name") },
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color(0xff4d81e7),
                        unfocusedIndicatorColor = Color(0xffedf1f3)
                    )
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        //EMAIL
        Text("Email", fontSize = 12.sp, color = Color(0xff6c7278))
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = !isValidEmail(email)
                            },
            placeholder = { Text("youremail@gmail.com") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color(0xff4d81e7),
                unfocusedIndicatorColor = Color(0xffedf1f3),
                errorIndicatorColor = Color.Red
            )
        )

        if (emailError) {
            Text(
                text = "Invalid email format",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        //BIRTH DATE PICKER
        Text("Birth of date", fontSize = 12.sp, color = Color(0xff6c7278))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {

            OutlinedTextField(
                value = birthDate,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxSize(),
                placeholder = { Text("12/12/2000") },
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xff4d81e7),
                    unfocusedIndicatorColor = Color(0xffedf1f3)
                )
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { showDatePicker = true }
            )
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val format = java.text.SimpleDateFormat("dd/MM/yyyy")
                            birthDate = format.format(java.util.Date(millis))
                        }
                        showDatePicker = false
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        Spacer(Modifier.height(16.dp))

        //PHONE NUMBER + COUNTRY PICKER
        Text("Phone Number", fontSize = 12.sp, color = Color(0xff6c7278))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            placeholder = { Text("812 3456 7890") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(10.dp),
            leadingIcon = {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .clickable { showCountryDialog = true }
                        .padding(start = 16.dp, end = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(selectedCountry.flag, fontSize = 18.sp)
                    Spacer(Modifier.width(6.dp))
                    Text(selectedCountry.code, fontSize = 14.sp)
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color(0xff4d81e7),
                unfocusedIndicatorColor = Color(0xffedf1f3)
            )
        )

        //SCROLLABLE COUNTRY PICKER
        CountryPickerDialog(
            isOpen = showCountryDialog,
            countryList = countryList,
            onDismiss = { showCountryDialog = false },
            onCountrySelected = {
                selectedCountry = it
                showCountryDialog = false
            }
        )

        Spacer(Modifier.height(16.dp))

        //PASSWORD
        Text("Set Password", fontSize = 12.sp, color = Color(0xff6c7278))
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = !isStrongPassword(password)
            },
            placeholder = { Text("******") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(10.dp),
            visualTransformation =
                if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        null,
                        tint = Color.Gray
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color(0xff4d81e7),
                unfocusedIndicatorColor = Color(0xffedf1f3),
                errorIndicatorColor = Color.Red
            )
        )

        if (passwordError) {
            Text(
                text = "Min contain 8 chars, Uppercase, Lowercase, Number, Symbol",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        //REGISTER BUTTON
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xff1d61e7))
        ) {
            Text("Register", color = Color.White)
        }
    }
}

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