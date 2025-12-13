import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testhydromate.ui.components.PrimaryBlue
import com.example.testhydromate.ui.components.TextGray
val InputBgGray = Color(0xFFF5F5F5)
val ButtonCancelBg = Color(0xFFE3F2FD) // Biru sangat muda untuk tombol cancel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDailyGoalContainer() {
    // State untuk mengontrol visibilitas Bottom Sheet
    var showBottomSheet by remember { mutableStateOf(false) }

    // State untuk menyimpan nilai goal.
    // Kita gunakan String agar mudah diedit di TextField, nanti dikonversi jika perlu disimpan.
    var goalValue by remember { mutableStateOf("2000") }

    // State sementara untuk di dalam bottom sheet (agar nilai asli tidak berubah sebelum klik Save)
    var tempGoalValue by remember { mutableStateOf(goalValue) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // --- Konten Utama ---
    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            // Tombol "Let's hydrate!" di paling bawah layar utama
            Button(
                onClick = { /* Aksi selanjutnya */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text("Let's hydrate!", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { innerPadding ->
        // Memanggil tampilan layar utama
        DailyGoalMainScreen(
            modifier = Modifier.padding(innerPadding),
            currentGoal = goalValue,
            onAdjustClick = {
                // Saat klik adjust, reset nilai sementara dan tampilkan sheet
                tempGoalValue = goalValue
                showBottomSheet = true
            }
        )
    }

    // --- Logika Bottom Sheet ---
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = Color.White, // Latar belakang sheet putih
            dragHandle = { BottomSheetDefaults.DragHandle() }, // Garis pegangan di atas
        ) {
            // Isi konten bottom sheet
            EditGoalSheetContent(
                tempValue = tempGoalValue,
                onValueChange = { tempGoalValue = it },
                onCancel = { showBottomSheet = false },
                onSave = {
                    // Simpan nilai sementara ke nilai utama, lalu tutup sheet
                    goalValue = tempGoalValue
                    showBottomSheet = false
                }
            )
            // Spacer untuk menghindari konten tertutup navigasi gesture HP di paling bawah
            Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ==============================
// Komponen Layar Utama (Gambar 2)
// ==============================
@Composable
fun DailyGoalMainScreen(
    modifier: Modifier = Modifier,
    currentGoal: String,
    onAdjustClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f)) // Spacer atas agar konten di tengah vertikal

        Text(
            text = "Your daily goal is",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryBlue
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- Placeholder Icon Gelas ---
        // Catatan: Gambar di desain adalah ilustrasi khusus.
        // Gunakan Image(painterResource(R.drawable.your_glass_icon)...) di aplikasi nyata.
        Icon(
            imageVector = Icons.Default.LocalDrink, // Icon bawaan sebagai contoh
            contentDescription = "Glass Icon",
            modifier = Modifier.size(120.dp),
            tint = PrimaryBlue.copy(alpha = 0.6f) // Warna biru transparan
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Teks Nilai Goal
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = currentGoal,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "mL",
                fontSize = 16.sp,
                color = TextGray
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tombol Adjust
        OutlinedButton(
            onClick = onAdjustClick,
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray)
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = TextGray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Adjust", color = TextGray)
        }

        Spacer(modifier = Modifier.weight(1f)) // Spacer bawah
    }
}

// ==============================
// Komponen Isi Bottom Sheet (Gambar 1)
// ==============================
@Composable
fun EditGoalSheetContent(
    tempValue: String,
    onValueChange: (String) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Edit Your Daily Goal",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Icon Tetesan Air
        Icon(
            imageVector = Icons.Default.WaterDrop,
            contentDescription = null,
            modifier = Modifier.size(60.dp),
            tint = PrimaryBlue
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- Custom Input Field ---
        // Kita membuat kotak abu-abu manual agar mirip desain
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(InputBgGray, RoundedCornerShape(16.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Input Angka
            BasicTextField(
                value = tempValue,
                onValueChange = {
                    // Validasi sederhana: hanya menerima angka dan maksimal 5 digit
                    if (it.length <= 5 && it.all { char -> char.isDigit() }) {
                        onValueChange(it)
                    }
                },
                textStyle = TextStyle(
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue,
                    textAlign = TextAlign.End
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.width(IntrinsicSize.Min) // Lebar menyesuaikan konten
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Satuan mL
            Text(
                text = "mL",
                fontSize = 18.sp,
                color = TextGray,
                modifier = Modifier.padding(top = 12.dp) // Sedikit turun agar sejajar baseline
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- Tombol Cancel & Save ---
        Row(modifier = Modifier.fillMaxWidth()) {
            // Tombol Cancel (Warna biru muda)
            Button(
                onClick = onCancel,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonCancelBg,
                    contentColor = PrimaryBlue
                ),
                shape = RoundedCornerShape(30.dp),
                elevation = null
            ) {
                Text("Cancel", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Tombol Save (Warna biru utama)
            Button(
                onClick = onSave,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue
                ),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text("Save", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun MainContainerPreview() {
    MainDailyGoalContainer()
}