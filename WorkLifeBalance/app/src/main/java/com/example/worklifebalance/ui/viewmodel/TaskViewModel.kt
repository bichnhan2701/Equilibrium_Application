package com.example.worklifebalance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worklifebalance.domain.model.Task
import com.example.worklifebalance.domain.usecase.task.DeleteAllTasksUseCase
import com.example.worklifebalance.domain.usecase.task.DeleteTaskUseCase
import com.example.worklifebalance.domain.usecase.task.DeleteTasksByGoalIdUseCase
import com.example.worklifebalance.domain.usecase.task.DeleteTasksByDomainIdUseCase
import com.example.worklifebalance.domain.usecase.task.GetAllTasksUseCase
import com.example.worklifebalance.domain.usecase.task.GetTaskByIdUseCase
import com.example.worklifebalance.domain.usecase.task.GetTasksByGoalIdUseCase
import com.example.worklifebalance.domain.usecase.task.InsertTaskUseCase
import com.example.worklifebalance.domain.usecase.task.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val getTasksByGoalIdUseCase: GetTasksByGoalIdUseCase,
    private val insertTaskUseCase: InsertTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val deleteAllTasksUseCase: DeleteAllTasksUseCase,
    private val deleteTasksByGoalIdUseCase: DeleteTasksByGoalIdUseCase,
    private val deleteTasksByDomainIdUseCase: DeleteTasksByDomainIdUseCase
) : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> = _selectedTask.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadTasks() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _tasks.value = getAllTasksUseCase()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getTaskById(taskId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _selectedTask.value = getTaskByIdUseCase(taskId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getTasksByGoalId(goalId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _tasks.value = getTasksByGoalIdUseCase(goalId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun insertTask(task: Task) {
        viewModelScope.launch {
            try {
                insertTaskUseCase(task)
                loadTasks()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                updateTaskUseCase(task)
                loadTasks()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            try {
                deleteTaskUseCase(task)
                loadTasks()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteAllTasks() {
        viewModelScope.launch {
            try {
                deleteAllTasksUseCase()
                loadTasks()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteTasksByGoalId(goalId: String) {
        viewModelScope.launch {
            try {
                deleteTasksByGoalIdUseCase(goalId)
                loadTasks()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteTasksByDomainId(domainId: String) {
        viewModelScope.launch {
            try {
                deleteTasksByDomainIdUseCase(domainId)
                loadTasks()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
