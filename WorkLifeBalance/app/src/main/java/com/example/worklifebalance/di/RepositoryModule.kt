package com.example.worklifebalance.di

import com.example.worklifebalance.data.repository.*
import com.example.worklifebalance.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindDomainRepository(
        impl: DomainRepositoryImpl
    ): DomainRepository

    @Binds
    @Singleton
    abstract fun bindGoalRepository(
        impl: GoalRepositoryImpl
    ): GoalRepository

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        impl: TaskRepositoryImpl
    ): TaskRepository


    @Binds
    abstract fun bindTaskExecutionRepository(
        impl: TaskExecutionRepositoryImpl
    ): TaskExecutionRepository

    @Binds
    @Singleton
    abstract fun bindEnergyRepository(
        impl: EnergyRepositoryImpl
    ): EnergyRepository

    @Binds
    @Singleton
    abstract fun bindRestSessionRepository(
        impl: RestSessionRepositoryImpl
    ): RestSessionRepository
}
