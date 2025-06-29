package com.example.worklifebalance.domain.usecase.energy

import com.example.worklifebalance.domain.model.Energy
import com.example.worklifebalance.domain.repository.EnergyRepository
import javax.inject.Inject

class GetAllEnergyUseCase @Inject constructor(
    private val repository: EnergyRepository
) {
    suspend operator fun invoke(): List<Energy> = repository.getAllEnergy()
}

