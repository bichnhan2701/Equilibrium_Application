package com.example.worklifebalance.domain.repository

import com.example.worklifebalance.domain.model.Energy

interface EnergyRepository {
    suspend fun insertEnergy(energy: Energy)
    suspend fun getAllEnergy(): List<Energy>
    suspend fun deleteAllEnergy()
    suspend fun getLatestEnergy(): Energy?
}
