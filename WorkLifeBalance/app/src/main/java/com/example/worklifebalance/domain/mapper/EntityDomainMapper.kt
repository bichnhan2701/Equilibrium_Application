package com.example.worklifebalance.domain.mapper

import com.example.worklifebalance.data.local.entity.DomainEntity
import com.example.worklifebalance.data.local.entity.EnergyEntity
import com.example.worklifebalance.data.local.entity.GoalEntity
import com.example.worklifebalance.data.local.entity.TaskEntity
import com.example.worklifebalance.domain.model.Domain
import com.example.worklifebalance.domain.model.Energy
import com.example.worklifebalance.domain.model.Goal
import com.example.worklifebalance.domain.model.Task


fun DomainEntity.toDomain(): Domain = Domain(
    id = id,
    name = name,
    color = color
)

fun Domain.toEntity(): DomainEntity = DomainEntity(
    id = id,
    name = name,
    color = color
)

fun GoalEntity.toDomain(): Goal = Goal(
    id = id,
    domainId = domainId,
    name = name,
    description = description,
    startDate = startDate,
    endDate = endDate,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Goal.toEntity(): GoalEntity = GoalEntity(
    id = id,
    domainId = domainId,
    name = name,
    description = description,
    startDate = startDate,
    endDate = endDate,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun TaskEntity.toDomain(): Task = Task(
    id = id,
    domainId = domainId,
    goalId = goalId,
    name = name,
    description = description,
    difficulty = difficulty,
    taskType = taskType,
    plannedDates = plannedDates,
    plannedTime = plannedTime,
    repeatRule = repeatRule,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Task.toEntity(): TaskEntity = TaskEntity(
    id = id,
    domainId = domainId,
    goalId = goalId,
    name = name,
    description = description,
    difficulty = difficulty,
    taskType = taskType,
    plannedDates = plannedDates,
    plannedTime = plannedTime,
    repeatRule = repeatRule,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun EnergyEntity.toDomain(): Energy = Energy(
    id = id,
    energy = energy,
    updatedAt = updatedAt
)

fun Energy.toEntity(): EnergyEntity = EnergyEntity(
    id = id,
    energy = energy,
    updatedAt = updatedAt
)
