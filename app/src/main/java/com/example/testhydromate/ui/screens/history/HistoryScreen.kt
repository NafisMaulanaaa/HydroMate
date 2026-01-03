package com.example.testhydromate.ui.screens.history

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testhydromate.data.model.WaterLog
import com.example.testhydromate.ui.components.EditWaterBottomSheet
import com.example.testhydromate.ui.components.PrimaryBlue
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val logs by viewModel.waterLogs.collectAsState()

    var selectedDelete by remember { mutableStateOf<WaterLog?>(null) }
    var selectedEdit by remember { mutableStateOf<WaterLog?>(null) }

    // State untuk Custom Notification (Mature Green Notification)
    var showNotification by remember { mutableStateOf(false) }
    var notificationMessage by remember { mutableStateOf("") }

    // Memproses data: Urutkan dari yang terbaru, lalu grupkan berdasarkan tanggal
    val groupedLogs = remember(logs) {
        logs.sortedByDescending { it.timestamp }
            .groupBy {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(Date(it.timestamp))
            }
    }

    // DIALOG KONFIRMASI HAPUS
    selectedDelete?.let { log ->
        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(log.timestamp))
        ConfirmDeleteDialog(
            time = time,
            onCancel = { selectedDelete = null },
            onConfirm = {
                viewModel.deleteLog(log)

                // Trigger Notifikasi Custom
                notificationMessage = "Drink record at $time has been deleted"
                showNotification = true

                selectedDelete = null
            }
        )
    }

    // BOTTOM SHEET EDIT
    selectedEdit?.let { log ->
        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(log.timestamp))
        EditWaterBottomSheet(
            log = log,
            onDismiss = { selectedEdit = null },
            onSave = { updatedLog ->
                viewModel.updateLog(updatedLog)

                // Trigger Notifikasi Custom saat Edit (Mature & Specific Message)
                notificationMessage = "Drink record at $time updated to ${updatedLog.amount} mL"
                showNotification = true

                selectedEdit = null
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // HEADER (Statik)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 74.dp, bottom = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "History",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
            }

            // DAFTAR RIWAYAT (Scrollable)
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 100.dp)
            ) {
                if (groupedLogs.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "No drink history found", color = Color.Gray)
                        }
                    }
                } else {
                    groupedLogs.forEach { (date, items) ->
                        item {
                            HistoryDateCard(
                                date = date,
                                logs = items,
                                onEdit = { selectedEdit = it },
                                onDelete = { selectedDelete = it }
                            )
                            Spacer(Modifier.height(20.dp))
                        }
                    }
                }
            }
        }

        // --- CUSTOM NOTIFICATION (HIJAU DI ATAS) ---
        AnimatedVisibility(
            visible = showNotification,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 50.dp) // Jarak dari atas layar agar tidak tertutup notch
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                color = Color(0xFF4CAF50), // Warna Hijau Mature
                shape = RoundedCornerShape(12.dp),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = notificationMessage,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Auto-hide notifikasi setelah 3 detik
            LaunchedEffect(showNotification) {
                if (showNotification) {
                    delay(3000)
                    showNotification = false
                }
            }
        }
    }
}

@Composable
fun HistoryDateCard(
    date: String,
    logs: List<WaterLog>,
    onEdit: (WaterLog) -> Unit,
    onDelete: (WaterLog) -> Unit
) {
    Column {
        Text(
            text = formatDateHeader(date),
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column {
                logs.forEachIndexed { index, log ->
                    HistoryItem(log = log, onEdit = onEdit, onDelete = onDelete)
                    if (index < logs.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 72.dp),
                            color = Color.LightGray.copy(alpha = 0.4f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItem(
    log: WaterLog,
    onEdit: (WaterLog) -> Unit,
    onDelete: (WaterLog) -> Unit
) {
    val time = remember { SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(log.timestamp)) }
    var menuOpen by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(id = com.example.testhydromate.R.drawable.water),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )

        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {
            Text("Water", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(time, fontSize = 12.sp, color = Color.Gray)
        }

        Text("${log.amount} mL", fontWeight = FontWeight.Bold, color = PrimaryBlue, fontSize = 16.sp)

        Box {
            IconButton(onClick = { menuOpen = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.Gray)
            }
            DropdownMenu(
                expanded = menuOpen,
                onDismissRequest = { menuOpen = false },
                shape = RoundedCornerShape(12.dp),
                containerColor = Color.White
            ) {
                DropdownMenuItem(
                    leadingIcon = { Icon(Icons.Outlined.Edit, null) },
                    text = { Text("Edit") },
                    onClick = { menuOpen = false; onEdit(log) }
                )
                DropdownMenuItem(
                    leadingIcon = { Icon(Icons.Outlined.Delete, null, tint = Color.Red) },
                    text = { Text("Delete", color = Color.Red) },
                    onClick = { menuOpen = false; onDelete(log) }
                )
            }
        }
    }
}

@Composable
fun ConfirmDeleteDialog(time: String, onCancel: () -> Unit, onConfirm: () -> Unit) {
    Dialog(onDismissRequest = onCancel) {
        Surface(shape = RoundedCornerShape(16.dp), color = Color.White) {
            Column(modifier = Modifier.padding(24.dp).widthIn(min = 280.dp)) {
                Text(text = "Delete history", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(text = "Are you sure you want to delete the drink record at $time?", fontSize = 14.sp, color = Color.Gray)
                Spacer(Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onCancel) { Text("Cancel", color = Color.Gray) }
                    TextButton(onClick = onConfirm) { Text("Delete", color = Color.Red) }
                }
            }
        }
    }
}

fun formatDateHeader(date: String): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val dateObj = sdf.parse(date) ?: return date
    val today = sdf.format(Date())
    val cal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
    val yesterday = sdf.format(cal.time)

    return when (date) {
        today -> "Today"
        yesterday -> "Yesterday"
        else -> SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(dateObj)
    }
}
