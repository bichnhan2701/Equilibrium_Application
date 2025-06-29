package com.example.worklifebalance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worklifebalance.domain.model.TaskExecution
import com.example.worklifebalance.domain.usecase.taskexecution.GetAllTaskExecutionsUseCase
import com.example.worklifebalance.domain.usecase.taskexecution.GetExecutionsForTaskUseCase
import com.example.worklifebalance.domain.usecase.taskexecution.InsertTaskExecutionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskExecutionViewModel @Inject constructor(
    private val getAllTaskExecutionsUseCase: GetAllTaskExecutionsUseCase,
    private val getExecutionsForTaskUseCase: GetExecutionsForTaskUseCase,
    private val insertTaskExecutionUseCase: InsertTaskExecutionUseCase
) : ViewModel() {
    private val _executions = MutableStateFlow<List<TaskExecution>>(emptyList())
    val executions: StateFlow<List<TaskExecution>> = _executions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadAllExecutions() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _executions.value = getAllTaskExecutionsUseCase()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadExecutionsForTask(taskId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _executions.value = getExecutionsForTaskUseCase(taskId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun insertTaskExecution(execution: TaskExecution) {
        viewModelScope.launch {
            try {
                // Chỉ lấy ngày (bỏ giờ phút giây) để tránh trùng ngày
                val calendar = java.util.Calendar.getInstance().apply {
                    timeInMillis = execution.executionDate
                    set(java.util.Calendar.HOUR_OF_DAY, 0)
                    set(java.util.Calendar.MINUTE, 0)
                    set(java.util.Calendar.SECOND, 0)
                    set(java.util.Calendar.MILLISECOND, 0)
                }
                val normalizedExecution = execution.copy(executionDate = calendar.timeInMillis)
                // Kiểm tra đã có execution cho ngày này chưa
                val executionsForTask = getExecutionsForTaskUseCase(execution.taskId)
                val isDuplicate = executionsForTask.any { it.executionDate == normalizedExecution.executionDate }
                if (!isDuplicate) {
                    insertTaskExecutionUseCase(normalizedExecution)
                    loadAllExecutions()
                } else {
                    _error.value = "Task đã được hoàn thành trong ngày này."
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
