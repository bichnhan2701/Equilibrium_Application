package com.example.worklifebalance.domain.mapper

import androidx.compose.ui.graphics.Color
import com.example.worklifebalance.data.local.entity.RecoveryActivityEntity
import com.example.worklifebalance.domain.model.RecoveryActivity

fun RecoveryActivityEntity.toDomain(): RecoveryActivity = RecoveryActivity(
    id = id,
    name = name,
    description = description,
    durationInMinutes = durationInMinutes,
    color = Color(colorHex)
)

fun RecoveryActivity.toEntity(): RecoveryActivityEntity = RecoveryActivityEntity(
    id = id,
    name = name,
    description = description,
    durationInMinutes = durationInMinutes,
    colorHex = color.value.toULong()
)




