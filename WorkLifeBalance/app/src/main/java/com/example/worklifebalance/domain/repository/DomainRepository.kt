package com.example.worklifebalance.domain.repository

import com.example.worklifebalance.domain.model.Domain

interface DomainRepository {
    suspend fun getAllDomains(): List<Domain>
    suspend fun addDomain(domain: Domain)
    suspend fun addDomains(domains: List<Domain>)
    suspend fun deleteAllDomains()
    suspend fun deleteDomainById(domainId: String)
    suspend fun updateDomain(domainId: String, name: String, color: ULong)
}