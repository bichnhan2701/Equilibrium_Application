package com.example.worklifebalance.domain.usecase.energy

import com.example.worklifebalance.domain.model.Energy
import com.example.worklifebalance.domain.repository.EnergyRepository
import javax.inject.Inject

class GetLatestEnergyUseCase @Inject constructor(
    private val repository: EnergyRepository
) {
    suspend operator fun invoke(): Energy? = repository.getLatestEnergy()
}

