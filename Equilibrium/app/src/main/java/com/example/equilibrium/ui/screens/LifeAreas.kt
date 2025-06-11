package com.example.equilibrium.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.equilibrium.ui.components.TopBar
import com.example.equilibrium.ui.components.LifeAreaItem
import com.example.equilibrium.ui.components.LifeArea
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LifeAreasScreen(onBackClick: () -> Unit = {}, onEditClick: () -> Unit = {}) {
    val lifeAreas = listOf(
        LifeArea("Giáo dục và tri thức", "2 mục tiêu", Icons.Default.School),
        LifeArea("Công việc và sự nghiệp", "0 mục tiêu", Icons.Default.Work),
        LifeArea("Sức khỏe thể chất", "0 mục tiêu", Icons.Default.Favorite),
        LifeArea("Gia đình", "1 mục tiêu", Icons.Default.Home),
        LifeArea("Phát triển bản thân", "1 mục tiêu", Icons.Default.AutoAwesome),
        LifeArea("Tình yêu", "1 mục tiêu", Icons.Default.LocationOn)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFEFF8F5), Color(0xFFA0E9D9))
                )
            )
            .padding(horizontal = 16.dp)
    ) {
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            TopBar(
                title = "Lĩnh vực cuộc sống",
                showEdit = true,
                onBackClick = onBackClick,
                onEditClick = onEditClick
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(lifeAreas) { area ->
                    LifeAreaItem(area)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LifeAreasScreenPreview() {
    MaterialTheme {
        Surface {
            LifeAreasScreen()
        }
    }
}
