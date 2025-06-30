package com.example.worklifebalance.di

import android.content.Context
import androidx.room.Room
import com.example.worklifebalance.data.local.WorkLifeBalanceDatabase
import com.example.worklifebalance.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WorkLifeBalanceDatabase =
        Room.databaseBuilder(
            context,
            WorkLifeBalanceDatabase::class.java,
            "worklifebalance_db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideTaskDao(db: WorkLifeBalanceDatabase): TaskDao = db.taskDao()

    @Provides
    fun provideTaskExecutionDao(db: WorkLifeBalanceDatabase): TaskExecutionDao = db.taskExecutionDao()

    @Provides
    fun provideGoalDao(db: WorkLifeBalanceDatabase): GoalDao = db.goalDao()

    @Provides
    fun provideDomainDao(db: WorkLifeBalanceDatabase): DomainDao = db.domainDao()

    @Provides
    fun provideEnergyDao(db: WorkLifeBalanceDatabase): EnergyDao = db.energyDao()

    @Provides
    fun provideRestSessionDao(db: WorkLifeBalanceDatabase): RestSessionDao = db.restSessionDao()

    @Provides
    fun provideRecoveryActivityDao(db: WorkLifeBalanceDatabase): RecoveryActivityDao = db.recoveryActivityDao()
}
