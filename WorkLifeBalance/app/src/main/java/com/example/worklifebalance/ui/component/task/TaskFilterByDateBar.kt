package com.example.worklifebalance.ui.component.task

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.worklifebalance.ui.theme.LightBlue
import com.example.worklifebalance.ui.theme.PastelBlue
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun TaskFilterByDateBar(
    onDateSelected: (String) -> Unit,
    selectedDate: String,
    onClearDate: () -> Unit
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    val dateFormat = remember { SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()) }

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Nhiệm vụ theo ngày",
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                text = "Bỏ lọc",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.clickable { onClearDate() }
            )
        }

        Button(
            onClick = {
                val dateParts = selectedDate.split("-")
                if (dateParts.size == 3 && selectedDate.isNotEmpty()) {
                    calendar.set(Calendar.DAY_OF_MONTH, dateParts[0].toInt())
                    calendar.set(Calendar.MONTH, dateParts[1].toInt() - 1)
                    calendar.set(Calendar.YEAR, dateParts[2].toInt())
                } else {
                    val now = Calendar.getInstance()
                    calendar.set(Calendar.YEAR, now.get(Calendar.YEAR))
                    calendar.set(Calendar.MONTH, now.get(Calendar.MONTH))
                    calendar.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH))
                }
                DatePickerDialog(
                    context,
                    { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                        val pickedCal = Calendar.getInstance().apply {
                            set(year, month, dayOfMonth)
                        }
                        val formattedDate = dateFormat.format(pickedCal.time)
                        onDateSelected(formattedDate)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.onSurface,
                containerColor = PastelBlue.copy(alpha = 0.2f)
            ),
            border = ButtonDefaults.outlinedButtonBorder
        ) {
            Text(
                text = if (selectedDate.isNotEmpty()) selectedDate else "Chọn ngày",
                style = MaterialTheme.typography.bodyLarge
            )
        }

    }
}