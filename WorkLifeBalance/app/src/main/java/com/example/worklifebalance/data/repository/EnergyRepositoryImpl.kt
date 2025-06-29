package com.example.worklifebalance.data.repository

import com.example.worklifebalance.data.local.dao.EnergyDao
import com.example.worklifebalance.domain.mapper.toDomain
import com.example.worklifebalance.domain.mapper.toEntity
import com.example.worklifebalance.domain.model.Energy
import com.example.worklifebalance.domain.repository.EnergyRepository
import javax.inject.Inject

class EnergyRepositoryImpl @Inject constructor(
    private val energyDao: EnergyDao
) : EnergyRepository {
    override suspend fun insertEnergy(energy: Energy) {
        energyDao.insertEnergy(energy.toEntity())
    }

    override suspend fun getAllEnergy(): List<Energy> {
        return energyDao.getAllEnergy().map { it.toDomain() }
    }
    override suspend fun deleteAllEnergy() {
        energyDao.deleteAllEnergy()
    }
    override suspend fun getLatestEnergy(): Energy? {
        return energyDao.getLatestEnergy()?.toDomain()
    }
}
