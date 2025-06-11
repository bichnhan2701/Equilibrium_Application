package com.example.equilibrium.ui.components

import androidx.compose.ui.graphics.vector.ImageVector

data class SelectableLifeArea(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val isSelected: Boolean
)
