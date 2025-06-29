package com.example.worklifebalance.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.worklifebalance.data.local.entity.DomainEntity

@Dao
interface DomainDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDomain(domain: DomainEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDomains(domains: List<DomainEntity>)

    @Query("SELECT * FROM domain")
    suspend fun getAllDomains(): List<DomainEntity>

    @Query("DELETE FROM domain")
    suspend fun deleteAllDomains()

    @Query("DELETE FROM domain WHERE id = :domainId")
    suspend fun deleteDomainById(domainId: String)

    @Query("UPDATE domain SET name = :name, color = :color WHERE id = :domainId")
    suspend fun updateDomain(domainId: String, name: String, color: ULong)
}