package com.example.worklifebalance.domain.usecase.energy

import com.example.worklifebalance.domain.repository.EnergyRepository
import javax.inject.Inject

class DeleteAllEnergyUseCase @Inject constructor(
    private val repository: EnergyRepository
) {
    suspend operator fun invoke() = repository.deleteAllEnergy()
}

