package com.example.worklifebalance.data.repository

import com.example.worklifebalance.data.local.dao.DomainDao
import com.example.worklifebalance.domain.mapper.toDomain
import com.example.worklifebalance.domain.mapper.toEntity
import com.example.worklifebalance.domain.model.Domain
import com.example.worklifebalance.domain.repository.DomainRepository
import javax.inject.Inject

class DomainRepositoryImpl @Inject constructor(
    private val domainDao: DomainDao,
) : DomainRepository {

    override suspend fun getAllDomains(): List<Domain> {
        return domainDao.getAllDomains().map { it.toDomain() }
    }

    override suspend fun addDomain(domain: Domain) {
        domainDao.insertDomain(domain.toEntity())
    }

    override suspend fun addDomains(domains: List<Domain>) {
        domainDao.insertDomains(domains.map { it.toEntity() })
    }

    override suspend fun deleteAllDomains() {
        domainDao.deleteAllDomains()
    }

    override suspend fun deleteDomainById(domainId: String) {
        domainDao.deleteDomainById(domainId)
    }

    override suspend fun updateDomain(domainId: String, name: String, color: ULong) {
        domainDao.updateDomain(domainId, name, color)
    }
}