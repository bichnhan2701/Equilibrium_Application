package com.example.worklifebalance.domain.usecase.domain

import com.example.worklifebalance.domain.model.Domain
import com.example.worklifebalance.domain.repository.DomainRepository
import javax.inject.Inject

class GetAllDomainsUseCase @Inject constructor(
    private val repository: DomainRepository
) {
    suspend operator fun invoke(): List<Domain> = repository.getAllDomains()
}

