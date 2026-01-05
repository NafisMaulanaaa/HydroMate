package com.example.testhydromate.ui.screens.reminder

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testhydromate.ui.components.PrimaryBlue
import androidx.compose.ui.graphics.graphicsLayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderSettingScreen(
    onBackClick: () -> Unit,
    viewModel: ReminderViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsState()
    val context = LocalContext.current

    // Helper untuk TimePicker Dialog
    fun showTimePicker(current: String, onTimeSelected: (Int, Int) -> Unit) {
        val parts = current.split(":")
        val h = parts[0].toInt()
        val m = parts[1].toInt()
        TimePickerDialog(context, { _, hour, minute ->
            onTimeSelected(hour, minute)
        }, h, m, true).show()
    }

    var showIntervalSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reminder Settings", fontWeight = FontWeight.Bold, color = PrimaryBlue) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = PrimaryBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // 1. MASTER SWITCH
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Enable Reminder", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Switch(
                    checked = settings.isEnabled,
                    onCheckedChange = { viewModel.toggleSwitch("enabled", settings.isEnabled) },
                    colors = SwitchDefaults.colors(checkedTrackColor = PrimaryBlue)
                )
            }

            HorizontalDivider(Modifier.padding(vertical = 16.dp))

            // Disable section below if toggle is off
            Column(modifier = Modifier.fillMaxWidth().alpha(if(settings.isEnabled) 1f else 0.5f)) {

                // 2. SOUND & VIBRATION
                SettingToggleItem("Vibration", settings.isVibration) { viewModel.toggleSwitch("vibration", settings.isVibration) }
                SettingToggleItem("Sound", settings.isSound) { viewModel.toggleSwitch("sound", settings.isSound) }

                Spacer(Modifier.height(24.dp))

                // 3. SCHEDULE (First - Last - Interval)
                Text("Schedule", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Spacer(Modifier.height(12.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    TimeCard("Start", settings.startTime) {
                        if(settings.isEnabled) showTimePicker(settings.startTime) { h, m -> viewModel.setTime(true, h, m) }
                    }
                    TimeCard("End", settings.endTime) {
                        if(settings.isEnabled) showTimePicker(settings.endTime) { h, m -> viewModel.setTime(false, h, m) }
                    }
                    TimeCard("Interval", "${settings.interval} min") {
                        if(settings.isEnabled) showIntervalSheet = true
                    }
                }

                Spacer(Modifier.height(24.dp))

                // 4. REPEAT DAYS
                Text("Repeat", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Spacer(Modifier.height(12.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    // 1=Sun, 2=Mon... Java Calendar standard
                    val days = listOf("2" to "M", "3" to "T", "4" to "W", "5" to "T", "6" to "F", "7" to "S", "1" to "S")
                    days.forEach { (id, label) ->
                        DayCircle(
                            label = label,
                            isSelected = settings.repeatDays.contains(id),
                            enabled = settings.isEnabled
                        ) { viewModel.toggleDay(id) }
                    }
                }
            }
        }
    }

    // Bottom Sheet untuk Interval
    if (showIntervalSheet) {
        ModalBottomSheet(onDismissRequest = { showIntervalSheet = false }, containerColor = Color.White) {
            Column(Modifier.padding(bottom = 32.dp)) {
                listOf(15, 30, 45, 60, 90, 120, 180).forEach { min ->
                    TextButton(
                        onClick = { viewModel.setInterval(min); showIntervalSheet = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if(min < 60) "$min Minutes" else "${min/60} Hours",
                            fontSize = 18.sp,
                            color = if(settings.interval == min) PrimaryBlue else Color.Black
                        )
                    }
                }
            }
        }
    }
}

// Sub-components
@Composable
fun SettingToggleItem(label: String, checked: Boolean, onCheck: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 16.sp)
        Switch(checked = checked, onCheckedChange = onCheck,
            colors = SwitchDefaults.colors(checkedTrackColor = PrimaryBlue))
    }
}

@Composable
fun TimeCard(label: String, value: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF5F5F5))
            .clickable { onClick() }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Spacer(Modifier.height(4.dp))
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
    }
}

@Composable
fun DayCircle(label: String, isSelected: Boolean, enabled: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(if (isSelected && enabled) PrimaryBlue else Color(0xFFF0F0F0))
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected && enabled) Color.White else Color.Gray,
            fontWeight = FontWeight.Bold
        )
    }
}

fun Modifier.alpha(alpha: Float) = this.then(Modifier.graphicsLayer(alpha = alpha))
