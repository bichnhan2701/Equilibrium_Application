package com.example.worklifebalance.ui.component

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import androidx.core.content.edit

@SuppressLint("DefaultLocale")
@Composable
fun ReminderTimeDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onSaved: () -> Unit
) {
    if (!showDialog) return
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("reminder_prefs", Context.MODE_PRIVATE)
    val gson = remember { Gson() }
    var timeList by remember { mutableStateOf(loadTimes(sharedPref, gson)) }
    var showTimePicker by remember { mutableStateOf(false) }
    var timeToAdd by remember { mutableStateOf(Calendar.getInstance()) }

    if (showTimePicker) {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                val cal = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                }
                val timeStr = String.format("%02d:%02d", hour, minute)
                if (!timeList.contains(timeStr)) {
                    timeList = timeList + timeStr
                }
                showTimePicker = false
            },
            timeToAdd.get(Calendar.HOUR_OF_DAY),
            timeToAdd.get(Calendar.MINUTE),
            true
        ).show()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cài đặt nhắc nhở") },
        text = {
            Column {
                if (timeList.isEmpty()) {
                    Text("Chưa có thời gian nhắc nhở nghỉ ngơi nào được thêm.")
                } else {
                    LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                        items(timeList.size) { idx ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) {
                                Text(timeList[idx], modifier = Modifier.weight(1f))
                                IconButton(onClick = {
                                    timeList = timeList.filterIndexed { i, _ -> i != idx }
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Remove")
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Button(onClick = { showTimePicker = true }) {
                    Text("Thêm thời gian")
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                saveTimes(sharedPref, gson, timeList)
                onSaved()
                onDismiss()
            }) {
                Text("Lưu")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}

private fun loadTimes(sharedPref: SharedPreferences, gson: Gson): List<String> {
    val json = sharedPref.getString("reminder_times", null) ?: return emptyList()
    val type = object : TypeToken<List<String>>() {}.type
    return gson.fromJson(json, type)
}

private fun saveTimes(sharedPref: SharedPreferences, gson: Gson, times: List<String>) {
    val json = gson.toJson(times)
    sharedPref.edit { putString("reminder_times", json) }
}

