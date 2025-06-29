package com.example.worklifebalance.domain.usecase.domain

import com.example.worklifebalance.domain.repository.DomainRepository
import javax.inject.Inject

class UpdateDomainUseCase @Inject constructor(
    private val repository: DomainRepository
) {
    suspend operator fun invoke(domainId: String, name: String, color: ULong) =
        repository.updateDomain(domainId, name, color)
}

