package com.example.worklifebalance.domain.usecase.domain

import com.example.worklifebalance.domain.repository.DomainRepository
import javax.inject.Inject

class DeleteAllDomainsUseCase @Inject constructor(
    private val repository: DomainRepository
) {
    suspend operator fun invoke() = repository.deleteAllDomains()
}

