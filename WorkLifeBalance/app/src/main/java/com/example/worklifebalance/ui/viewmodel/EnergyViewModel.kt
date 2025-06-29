package com.example.worklifebalance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worklifebalance.domain.model.Energy
import com.example.worklifebalance.domain.usecase.energy.GetAllEnergyUseCase
import com.example.worklifebalance.domain.usecase.energy.GetLatestEnergyUseCase
import com.example.worklifebalance.domain.usecase.energy.InsertEnergyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EnergyViewModel @Inject constructor(
    private val getAllEnergyUseCase: GetAllEnergyUseCase,
    private val insertEnergyUseCase: InsertEnergyUseCase,
    private val getLatestEnergyUseCase: GetLatestEnergyUseCase
) : ViewModel() {

    private val _energies = MutableStateFlow<List<Energy>>(emptyList())
    val energies: StateFlow<List<Energy>> = _energies

    private val _latestEnergy = MutableStateFlow<Energy?>(null)
    val latestEnergy: StateFlow<Energy?> = _latestEnergy

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadAllEnergy() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _energies.value = getAllEnergyUseCase()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadLatestEnergy() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _latestEnergy.value = getLatestEnergyUseCase()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun insertEnergy(energy: Energy) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val newEnergy = energy.copy(id = java.util.UUID.randomUUID().toString(), updatedAt = System.currentTimeMillis())
                insertEnergyUseCase(newEnergy)
                loadAllEnergy()
                loadLatestEnergy() // Đảm bảo cập nhật latestEnergy ngay sau khi insert
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
