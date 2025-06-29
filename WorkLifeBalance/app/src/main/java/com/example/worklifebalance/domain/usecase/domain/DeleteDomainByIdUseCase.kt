package com.example.worklifebalance.domain.usecase.domain

import com.example.worklifebalance.domain.repository.DomainRepository
import javax.inject.Inject

class DeleteDomainByIdUseCase @Inject constructor(
    private val repository: DomainRepository
) {
    suspend operator fun invoke(domainId: String) = repository.deleteDomainById(domainId)
}

