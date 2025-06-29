package com.example.worklifebalance.domain.mapper

import com.example.worklifebalance.data.local.entity.TaskExecutionEntity
import com.example.worklifebalance.domain.model.TaskExecution


fun TaskExecutionEntity.toDomain(): TaskExecution = TaskExecution(
    id = id,
    taskId = taskId,
    executionDate = executionDate
)

fun TaskExecution.toEntity(): TaskExecutionEntity = TaskExecutionEntity(
    id = id,
    taskId = taskId,
    executionDate = executionDate
)

