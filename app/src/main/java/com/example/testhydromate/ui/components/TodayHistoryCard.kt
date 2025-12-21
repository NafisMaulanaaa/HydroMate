package com.example.testhydromate.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testhydromate.data.model.WaterLog
import com.example.testhydromate.ui.screens.history.ConfirmDeleteDialog
import com.example.testhydromate.ui.screens.history.showDeleteToast
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TodayHistoryCard(
    history: List<WaterLog>,
    modifier: Modifier = Modifier,
    onUpdate: (WaterLog) -> Unit,
    onDeleteConfirm: (WaterLog) -> Unit
) {
    val context = LocalContext.current
    var selectedEdit by remember { mutableStateOf<WaterLog?>(null) }
    var selectedDelete by remember { mutableStateOf<WaterLog?>(null) }

    val todayLogs = remember(history) {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = sdf.format(Date())
        history.filter { sdf.format(Date(it.timestamp)) == today }
            .sortedByDescending { it.timestamp }
    }

    selectedDelete?.let { log ->
        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(log.timestamp))
        ConfirmDeleteDialog(
            time = time,
            onCancel = { selectedDelete = null },
            onConfirm = {
                onDeleteConfirm(log)
                showDeleteToast(context, time)
                selectedDelete = null
            }
        )
    }

    selectedEdit?.let { log ->
        EditWaterBottomSheet(log = log, onDismiss = { selectedEdit = null }, onSave = { onUpdate(it); selectedEdit = null })
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp), // Sama dengan History Date Card
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Today History",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            if (todayLogs.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No drink history today", color = Color.Gray, fontSize = 14.sp)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = 100.dp) // Ruang untuk Navbar
                ) {
                    itemsIndexed(todayLogs) { index, log ->
                        TodayHistoryItem(
                            log = log,
                            onEdit = { selectedEdit = it },
                            onDelete = { selectedDelete = it }
                        )
                        if (index < todayLogs.lastIndex) {
                            Divider(
                                modifier = Modifier.padding(start = 72.dp),
                                color = Color.LightGray.copy(alpha = 0.4f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TodayHistoryItem(log: WaterLog, onEdit: (WaterLog) -> Unit, onDelete: (WaterLog) -> Unit) {
    val time = remember { SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(log.timestamp)) }
    var menuOpen by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
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
                Icon(Icons.Default.MoreVert, null, tint = Color.Gray)
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