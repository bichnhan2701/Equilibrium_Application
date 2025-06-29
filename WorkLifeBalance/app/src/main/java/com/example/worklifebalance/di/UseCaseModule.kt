package com.example.worklifebalance.di

import com.example.worklifebalance.domain.repository.*
import com.example.worklifebalance.domain.usecase.auth.*
import com.example.worklifebalance.domain.usecase.domain.*
import com.example.worklifebalance.domain.usecase.energy.*
import com.example.worklifebalance.domain.usecase.goal.*
import com.example.worklifebalance.domain.usecase.task.*
import com.example.worklifebalance.domain.usecase.taskexecution.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    // Domain usecases
    @Provides
    @Singleton
    fun provideAddDomainUseCase(repo: DomainRepository) = AddDomainUseCase(repo)

    @Provides
    @Singleton
    fun provideAddDomainsUseCase(repo: DomainRepository) = AddDomainsUseCase(repo)

    @Provides
    @Singleton
    fun provideDeleteDomainByIdUseCase(repo: DomainRepository) = DeleteDomainByIdUseCase(repo)

    @Provides
    @Singleton
    fun provideDeleteAllDomainsUseCase(repo: DomainRepository) = DeleteAllDomainsUseCase(repo)

    @Provides
    @Singleton
    fun provideGetAllDomainsUseCase(repo: DomainRepository) = GetAllDomainsUseCase(repo)

    @Provides
    @Singleton
    fun provideUpdateDomainUseCase(repo: DomainRepository) = UpdateDomainUseCase(repo)

    // Energy usecases
    @Provides
    @Singleton
    fun provideInsertEnergyUseCase(repo: EnergyRepository) = InsertEnergyUseCase(repo)

    @Provides
    @Singleton
    fun provideGetAllEnergyUseCase(repo: EnergyRepository) = GetAllEnergyUseCase(repo)

    @Provides
    @Singleton
    fun provideGetLatestEnergyUseCase(repo: EnergyRepository) = GetLatestEnergyUseCase(repo)

    // Goal usecases
    @Provides
    @Singleton
    fun provideInsertGoalUseCase(repo: GoalRepository) = InsertGoalUseCase(repo)

    @Provides
    @Singleton
    fun provideUpdateGoalUseCase(repo: GoalRepository) = UpdateGoalUseCase(repo)

    @Provides
    @Singleton
    fun provideDeleteGoalUseCase(repo: GoalRepository) = DeleteGoalUseCase(repo)

    @Provides
    @Singleton
    fun provideDeleteAllGoalsUseCase(repo: GoalRepository) = DeleteAllGoalsUseCase(repo)

    @Provides
    @Singleton
    fun provideGetAllGoalsUseCase(repo: GoalRepository) = GetAllGoalsUseCase(repo)

    @Provides
    @Singleton
    fun provideGetGoalByIdUseCase(repo: GoalRepository) = GetGoalByIdUseCase(repo)

    // Task usecases
    @Provides
    @Singleton
    fun provideInsertTaskUseCase(repo: TaskRepository) = InsertTaskUseCase(repo)

    @Provides
    @Singleton
    fun provideUpdateTaskUseCase(repo: TaskRepository) = UpdateTaskUseCase(repo)

    @Provides
    @Singleton
    fun provideDeleteTaskUseCase(repo: TaskRepository) = DeleteTaskUseCase(repo)

    @Provides
    @Singleton
    fun provideDeleteAllTasksUseCase(repo: TaskRepository) = DeleteAllTasksUseCase(repo)

    @Provides
    @Singleton
    fun provideGetAllTasksUseCase(repo: TaskRepository) = GetAllTasksUseCase(repo)

    @Provides
    @Singleton
    fun provideGetTaskByIdUseCase(repo: TaskRepository) = GetTaskByIdUseCase(repo)

    // TaskExecution usecases
    @Provides
    @Singleton
    fun provideInsertTaskExecutionUseCase(repo: TaskExecutionRepository) = InsertTaskExecutionUseCase(repo)

    @Provides
    @Singleton
    fun provideGetExecutionsForTaskUseCase(repo: TaskExecutionRepository) = GetExecutionsForTaskUseCase(repo)

    @Provides
    @Singleton
    fun provideGetAllTaskExecutionsUseCase(repo: TaskExecutionRepository) = GetAllTaskExecutionsUseCase(repo)

    // Auth usecases
    @Provides
    @Singleton
    fun provideSignInWithCredentialUseCase(repo: AuthRepository) = SignInWithCredentialUseCase(repo)

    @Provides
    @Singleton
    fun provideSignOutUseCase(repo: AuthRepository) = SignOutUseCase(repo)

    // RestSession usecases
    @Provides
    @Singleton
    fun provideAddRestSessionUseCase(repo: RestSessionRepository) = com.example.worklifebalance.domain.usecase.rest.AddRestSessionUseCase(repo)

    @Provides
    @Singleton
    fun provideGetRestSessionHistoryUseCase(repo: RestSessionRepository) = com.example.worklifebalance.domain.usecase.rest.GetRestSessionHistoryUseCase(repo)

    @Provides
    @Singleton
    fun provideGetTotalRestMinutesUseCase(repo: RestSessionRepository) = com.example.worklifebalance.domain.usecase.rest.GetTotalRestMinutesUseCase(repo)

}
