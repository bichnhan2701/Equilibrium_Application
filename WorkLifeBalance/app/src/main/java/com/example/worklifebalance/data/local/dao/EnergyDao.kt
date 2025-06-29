package com.example.worklifebalance.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.worklifebalance.data.local.entity.EnergyEntity

@Dao
interface EnergyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnergy(energy: EnergyEntity)

    @Query("SELECT * FROM energy ORDER BY updatedAt DESC")
    suspend fun getAllEnergy(): List<EnergyEntity>

    @Query("DELETE FROM energy")
    suspend fun deleteAllEnergy()

    @Query("SELECT * FROM energy ORDER BY updatedAt DESC LIMIT 1")
    suspend fun getLatestEnergy(): EnergyEntity?
}
