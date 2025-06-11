package com.example.equilibrium.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import com.example.equilibrium.R
import androidx.compose.ui.draw.clip
import com.example.equilibrium.ui.theme.EquilibriumTheme
import com.example.equilibrium.ui.components.BottomNavBar
import com.example.equilibrium.ui.components.EnergyStatus

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding)
                .background(Color(0xFFECF5FD))
                .padding(bottom = 72.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item { HomeHeaderSection() }
            item { LifeAreasSection() }
            item { TaskSection() }
            item { QuoteSection() }
        }
    }
}

@Composable
fun HomeHeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFDEEFFF), Color(0xFFB8AFFF))
                ),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .padding(16.dp)
    ) {
        Column {
            // Dòng đầu tiên: Chào hỏi + EnergyStatus bên phải
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f) // chiếm phần còn lại
                ) {
                    Text(text = "Equilibrium", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(text = "Xin chào, Bích Nhân!", fontSize = 16.sp)
                    Text(
                        text = "Thứ 2,\n03 - 06 - 2025",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TimeCard("04")
                        TimeCard("04")
                        TimeCard("04")
                        Text(
                            text = "PM",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Năng lượng
                EnergyStatus()
            }
        }
    }
}

@Composable
fun TimeCard(time: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(end = 4.dp)
    ) {
        Text(
            text = time,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LifeAreasSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Explore, contentDescription = null)
            Text(
                "Lĩnh vực bạn quan tâm",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            LifeAreaCard("Học tập", R.drawable.learning)
            LifeAreaCard("Sự nghiệp", R.drawable.career)
            LifeAreaCard("Gia đình", R.drawable.family)
        }
    }
}


@Composable
fun LifeAreaCard(title: String, imageRes: Int) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .padding(end = 8.dp)
            .width(120.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
            Image(painter = painterResource(id = imageRes), contentDescription = title)
            Text(title, fontWeight = FontWeight.SemiBold)
            LinearProgressIndicator(progress = 0.25f,
                modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun TaskSection() {
    val tasks = listOf(
        "Công việc hôm nay" to "7:00 AM",
        "Công việc hôm nay" to "11:00 AM",
        "Công việc hôm nay" to "12:00 PM",
        "Công việc hôm nay" to "3:00 PM",
        "Công việc hôm nay" to "5:00 PM"
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Checklist, contentDescription = null)
            Text(
                "Gợi ý các nhiệm vụ",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                tasks.forEachIndexed { index, (title, time) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = index == 0,
                            onCheckedChange = {},
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            title,
                            modifier = Modifier.weight(1f),
                            fontSize = 14.sp
                        )
                        Text(
                            time,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuoteSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("“Quote”", fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.meditate),
                contentDescription = "Quote",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFDBECFD)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Một tâm trí và cơ thể được nghỉ ngơi tốt sẽ có khả năng đạt hiệu suất và khả năng sáng tạo tốt hơn.",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    EquilibriumTheme {
        HomeScreen(navController = navController)
    }
}
