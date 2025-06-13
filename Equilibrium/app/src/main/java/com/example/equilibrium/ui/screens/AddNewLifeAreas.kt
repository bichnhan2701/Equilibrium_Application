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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
@Composable

fun AddNewLifeAreasScreen() {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .width(300.dp)

        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "+ Lĩnh vực mới",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF388E3C),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                val outlineColorFocused = Color(0xFF388E3C)
                val outlineColorUnfocused = Color.Black
                val black = Color.Black
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tên lĩnh vực mới", color = black, fontSize = 12.sp) },
                    placeholder = { Text("VD: Tài chính...", color = black, fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = outlineColorFocused,
                        unfocusedBorderColor = outlineColorUnfocused,
                        focusedLabelColor = outlineColorFocused,
                        unfocusedLabelColor = outlineColorUnfocused,
                        cursorColor = black,
                        focusedTextColor = black,
                        unfocusedTextColor = black,
                        focusedPlaceholderColor = black,
                        unfocusedPlaceholderColor = black
                    )
                )


                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Mô tả", color = black, fontSize = 12.sp) },
                    placeholder = { Text("VD: Liên quan đến chi tiêu, khoản tiết kiệm...", color = black, fontSize = 12.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = outlineColorFocused,
                        unfocusedBorderColor = outlineColorUnfocused,
                        focusedLabelColor = outlineColorFocused,
                        unfocusedLabelColor = outlineColorUnfocused,
                        cursorColor = black,
                        focusedTextColor = black,
                        unfocusedTextColor = black,
                        focusedPlaceholderColor = black,
                        unfocusedPlaceholderColor = black
                    )
                )


                Button(
                    onClick = { /* TODO: Handle Save */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Lưu", color = Color.White)
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun AddNewLifeAreasScreenPreview() {
    AddNewLifeAreasScreen()
}