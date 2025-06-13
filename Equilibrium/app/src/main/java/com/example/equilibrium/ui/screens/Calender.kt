package com.example.equilibrium.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*
import androidx.compose.foundation.shape.CircleShape

@Composable
fun CalendarScreen(navController: NavController = rememberNavController()) {
    val currentDate = remember { mutableStateOf(LocalDate.now()) }
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }

    Scaffold(
        bottomBar = { com.example.equilibrium.ui.components.BottomNavBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF9FBFD))
        ) {
            CalendarHeader(currentDate.value)
            CalendarView(currentDate.value, selectedDate)
            Divider(
                color = Color(0xFFCCCCCC),
                thickness = 1.dp,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            TodayTaskSection()
        }
    }
}

@Composable
fun CalendarHeader(date: LocalDate) {
    val monthYear = "${date.month.getDisplayName(TextStyle.FULL, Locale("vi")).replaceFirstChar { it.uppercaseChar() }}, ${date.year}"
    Text(
        text = "Lịch biểu",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        textAlign = TextAlign.Center
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { }) {
            Icon(Icons.Default.ArrowBack, contentDescription = null)
        }
        Text(
            text = monthYear,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        IconButton(onClick = { }) {
            Icon(Icons.Default.ArrowForward, contentDescription = null)
        }
    }
}

@Composable
fun CalendarView(currentDate: LocalDate, selectedDate: MutableState<LocalDate>) {
    val yearMonth = YearMonth.from(currentDate)
    val firstDay = yearMonth.atDay(1)
    val lastDay = yearMonth.atEndOfMonth()
    val firstDayOfWeek = firstDay.dayOfWeek.value % 7
    val totalDays = firstDayOfWeek + lastDay.dayOfMonth
    val today = LocalDate.now()

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            listOf("CN", "T2", "T3", "T4", "T5", "T6", "T7").forEach {
                Text(it, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 12.sp)
            }
        }

        for (week in 0 until ((totalDays + 6) / 7)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (day in 0..6) {
                    val index = week * 7 + day
                    val dayNumber = index - firstDayOfWeek + 1
                    val date = firstDay.plusDays((dayNumber - 1).toLong())

                    val isThisMonth = dayNumber in 1..lastDay.dayOfMonth
                    val isSelected = selectedDate.value == date
                    val isToday = today == date

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(2.dp)
                            .clickable(enabled = isThisMonth) { selectedDate.value = date },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .background(Color(0xFF388E3C), RoundedCornerShape(50))
                            )
                        }
                        Text(
                            text = if (isThisMonth) "$dayNumber" else "${date.dayOfMonth}",
                            color = when {
                                isSelected -> Color.White
                                isThisMonth -> Color.Black
                                else -> Color.Gray
                            },
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TodayTaskSection() {
    val tasks = listOf(
        Task("Ăn sáng", "7:00 AM - 7:30 AM", Color(0xFFD0EFFF)),
        Task("Đi học", "8:00 AM - 12:00 PM", Color(0xFFC9F7DC))
    )

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text("Hôm nay", fontSize = 16.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 8.dp))

        tasks.forEach {
            TaskItem(it)
        }
    }
}

data class Task(val title: String, val time: String, val backgroundColor: Color)

@Composable
fun TaskItem(task: Task) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = task.time.substringBefore(" "),
            modifier = Modifier.width(60.dp),
            fontSize = 12.sp,
            color = Color.Gray
        )
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = task.backgroundColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(12.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(task.title, fontWeight = FontWeight.Bold)
                    Text(task.time, fontSize = 12.sp)
                }
                Checkbox(checked = false, onCheckedChange = {})
            }
        }
    }
}

@Composable
fun BottomNavBar() {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.CalendarToday, contentDescription = "Calendar") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF388E3C), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                }
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.BarChart, contentDescription = "Stats") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCalendarScreen() {
    CalendarScreen()
}
