package com.example.worklifebalance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worklifebalance.domain.model.Goal
import com.example.worklifebalance.domain.usecase.goal.DeleteAllGoalsUseCase
import com.example.worklifebalance.domain.usecase.goal.DeleteGoalUseCase
import com.example.worklifebalance.domain.usecase.goal.DeleteGoalsByDomainIdUseCase
import com.example.worklifebalance.domain.usecase.goal.GetAllGoalsUseCase
import com.example.worklifebalance.domain.usecase.goal.GetGoalByIdUseCase
import com.example.worklifebalance.domain.usecase.goal.InsertGoalUseCase
import com.example.worklifebalance.domain.usecase.goal.UpdateGoalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalViewModel @Inject constructor(
    private val getAllGoalsUseCase: GetAllGoalsUseCase,
    private val getGoalByIdUseCase: GetGoalByIdUseCase,
    private val insertGoalUseCase: InsertGoalUseCase,
    private val updateGoalUseCase: UpdateGoalUseCase,
    private val deleteGoalUseCase: DeleteGoalUseCase,
    private val deleteAllGoalsUseCase: DeleteAllGoalsUseCase,
    private val deleteGoalsByDomainIdUseCase: DeleteGoalsByDomainIdUseCase
) : ViewModel() {
    private val _goals = MutableStateFlow<List<Goal>>(emptyList())
    val goals: StateFlow<List<Goal>> = _goals

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _goalById = MutableStateFlow<Goal?>(null)
    val goalById: StateFlow<Goal?> = _goalById

    fun loadGoals() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _goals.value = getAllGoalsUseCase()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun insertGoal(goal: Goal) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                insertGoalUseCase(goal)
                loadGoals()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateGoal(goal: Goal) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                updateGoalUseCase(goal)
                loadGoals()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteGoal(goal: Goal) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                deleteGoalUseCase(goal)
                loadGoals()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteAllGoals() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                deleteAllGoalsUseCase()
                loadGoals()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getGoalById(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _goalById.value = getGoalByIdUseCase(id)
            } catch (e: Exception) {
                _error.value = e.message
                _goalById.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteGoalsByDomainId(domainId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                deleteGoalsByDomainIdUseCase(domainId)
                loadGoals()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
