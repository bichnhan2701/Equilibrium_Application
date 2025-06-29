package com.example.worklifebalance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worklifebalance.domain.model.RestSession
import com.example.worklifebalance.domain.usecase.rest.AddRestSessionUseCase
import com.example.worklifebalance.domain.usecase.rest.GetRestSessionHistoryUseCase
import com.example.worklifebalance.domain.usecase.rest.GetTotalRestMinutesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestSessionViewModel @Inject constructor(
    private val addRestSessionUseCase: AddRestSessionUseCase,
    private val getRestSessionHistoryUseCase: GetRestSessionHistoryUseCase,
    private val getTotalRestMinutesUseCase: GetTotalRestMinutesUseCase
) : ViewModel() {
    private val _restHistory = MutableStateFlow<List<RestSession>>(emptyList())
    val restHistory: StateFlow<List<RestSession>> = _restHistory

    private val _totalRestMinutes = MutableStateFlow(0)
    val totalRestMinutes: StateFlow<Int> = _totalRestMinutes

    init {
        loadRestHistory()
        loadTotalRestMinutes()
    }

    fun addRestSession(restSession: RestSession) {
        viewModelScope.launch {
            addRestSessionUseCase(restSession)
            loadRestHistory()
            loadTotalRestMinutes()
        }
    }

    fun loadRestHistory() {
        viewModelScope.launch {
            _restHistory.value = getRestSessionHistoryUseCase()
        }
    }

    fun loadTotalRestMinutes() {
        viewModelScope.launch {
            _totalRestMinutes.value = getTotalRestMinutesUseCase()
        }
    }
}
