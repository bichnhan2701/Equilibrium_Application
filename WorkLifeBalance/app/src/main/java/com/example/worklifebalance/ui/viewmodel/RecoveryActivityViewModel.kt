package com.example.worklifebalance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worklifebalance.domain.model.RecoveryActivity
import com.example.worklifebalance.domain.usecase.recovery.GetRecoveryActivitiesUseCase
import com.example.worklifebalance.domain.usecase.recovery.InsertRecoveryActivityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecoveryActivityViewModel @Inject constructor(
    private val getRecoveryActivitiesUseCase: GetRecoveryActivitiesUseCase,
    private val insertRecoveryActivityUseCase: InsertRecoveryActivityUseCase
) : ViewModel() {
    val recoveryActivities: StateFlow<List<RecoveryActivity>> =
        getRecoveryActivitiesUseCase()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun insertRecoveryActivity(activity: RecoveryActivity) {
        viewModelScope.launch {
            insertRecoveryActivityUseCase(activity)
        }
    }
}

