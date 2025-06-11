package com.example.equilibrium.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.tooling.preview.Preview
import com.example.equilibrium.ui.components.TopBar
import com.example.equilibrium.ui.components.SelectableLifeArea
import com.example.equilibrium.ui.components.SelectableLifeAreaItem

@Composable
fun AddLifeAreasScreen(
    areas: List<SelectableLifeArea>,
    onToggleSelect: (SelectableLifeArea) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFEFF8F5), Color(0xFFA0E9D9))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            TopBar(
                title = "Quản lý mục tiêu",
                showEdit = true,
                onBackClick = onBackClick,
                onSaveClick = onSaveClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Chọn các lĩnh vực bạn muốn tập trung trong thời gian tới. Các mục tiêu và nhiệm vụ sẽ liên quan đến các lĩnh vực này.",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(areas) { area ->
                    SelectableLifeAreaItem(area = area, onToggleSelect = onToggleSelect)
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(64.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF8ECDF6), Color(0xFFB7A7F7)) // xanh - tím gradient
                    ),
                    shape = CircleShape
                )
                .clickable {
                    // TODO: Add new area
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun AddLifeAreasScreenPreview() {
    var areas by remember {
        mutableStateOf(
            listOf(
                SelectableLifeArea("Giáo dục và tri thức", "Học tập, bằng cấp, kiến thức chuyên môn.", Icons.Default.School, true),
                SelectableLifeArea("Công việc và sự nghiệp", "Công việc, định hướng nghề nghiệp và phát triển chuyên môn.", Icons.Default.Work, false),
                SelectableLifeArea("Sức khỏe thể chất", "Thể dục thể thao, hoạt động phát triển thể chất.", Icons.Default.Favorite, true),
                SelectableLifeArea("Bạn bè và mối quan hệ xã hội", "Bạn bè, kết nối cộng đồng, mạng lưới xã hội.", Icons.Default.Groups, false),
                SelectableLifeArea("Gia đình", "Liên quan đến người thân và gia đình.", Icons.Default.Home, true),
                SelectableLifeArea("Phát triển bản thân", "Học hỏi, khám phá, đọc sách, sáng tạo.", Icons.Default.SelfImprovement, false),
                SelectableLifeArea("Tình yêu", "Tình yêu, hẹn hò và mối quan hệ bền vững.", Icons.Default.FavoriteBorder, true),
            )
        )
    }

    AddLifeAreasScreen(
        areas = areas,
        onToggleSelect = { selected ->
            areas = areas.map {
                if (it.title == selected.title) it.copy(isSelected = !it.isSelected) else it
            }
        },
        onBackClick = {},
        onSaveClick = {}
    )
}
