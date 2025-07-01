package com.example.worklifebalance.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.worklifebalance.data.local.converter.ListLongConverter
import com.example.worklifebalance.data.local.converter.ULongConverter
import com.example.worklifebalance.data.local.dao.*
import com.example.worklifebalance.data.local.entity.*

@Database(
    entities = [
        DomainEntity::class,
        GoalEntity::class,
        TaskEntity::class,
        TaskExecutionEntity::class,
        EnergyEntity::class,
        RestSessionEntity::class,
        RecoveryActivityEntity::class
    ],
    version = 8,
    exportSchema = false
)
@TypeConverters(ListLongConverter::class, ULongConverter::class)
abstract class WorkLifeBalanceDatabase : RoomDatabase() {
    abstract fun domainDao(): DomainDao
    abstract fun goalDao(): GoalDao
    abstract fun taskDao(): TaskDao
    abstract fun taskExecutionDao(): TaskExecutionDao
    abstract fun energyDao(): EnergyDao
    abstract fun restSessionDao(): RestSessionDao
    abstract fun recoveryActivityDao(): RecoveryActivityDao
}