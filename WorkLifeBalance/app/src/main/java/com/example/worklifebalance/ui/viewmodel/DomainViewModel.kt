package com.example.worklifebalance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worklifebalance.domain.model.Domain
import com.example.worklifebalance.domain.usecase.domain.AddDomainUseCase
import com.example.worklifebalance.domain.usecase.domain.AddDomainsUseCase
import com.example.worklifebalance.domain.usecase.domain.DeleteAllDomainsUseCase
import com.example.worklifebalance.domain.usecase.domain.DeleteDomainByIdUseCase
import com.example.worklifebalance.domain.usecase.domain.GetAllDomainsUseCase
import com.example.worklifebalance.domain.usecase.domain.UpdateDomainUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel quản lý các thao tác với Domain (lĩnh vực)
// Sử dụng các usecase: thêm, xóa, cập nhật, lấy danh sách, đồng bộ với Firebase
// Sử dụng StateFlow để cập nhật UI theo trạng thái dữ liệu, loading, error
// Cách dùng:
// - Gọi loadDomains() để lấy danh sách lĩnh vực
// - Gọi addDomain/domain/update/delete... để thao tác dữ liệu, UI sẽ tự động cập nhật qua StateFlow
// - Gọi syncAllDomainsToFirebase/syncAllDomainsFromFirebase để đồng bộ dữ liệu với Firebase (truyền userId)
// - Theo dõi domains, isLoading, error để cập nhật UI
// Ví dụ trong Composable:
// val viewModel: DomainViewModel = hiltViewModel()
// val domains by viewModel.domains.collectAsState()
// val isLoading by viewModel.isLoading.collectAsState()
// val error by viewModel.error.collectAsState()

@HiltViewModel
class DomainViewModel @Inject constructor(
    private val addDomainUseCase: AddDomainUseCase,
    private val addDomainsUseCase: AddDomainsUseCase,
    private val deleteAllDomainsUseCase: DeleteAllDomainsUseCase,
    private val deleteDomainByIdUseCase: DeleteDomainByIdUseCase,
    private val getAllDomainsUseCase: GetAllDomainsUseCase,
    private val updateDomainUseCase: UpdateDomainUseCase,
) : ViewModel() {
    private val _domains = MutableStateFlow<List<Domain>>(emptyList())
    val domains: StateFlow<List<Domain>> = _domains.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadDomains() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _domains.value = getAllDomainsUseCase()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addDomain(domain: Domain) {
        viewModelScope.launch {
            try {
                addDomainUseCase(domain)
                loadDomains()
            } catch (e: Exception) {
                android.util.Log.e("DomainViewModel", "Lỗi khi thêm domain", e)
                _error.value = e.message
            }
        }
    }

    fun addDomains(domains: List<Domain>) {
        viewModelScope.launch {
            try {
                addDomainsUseCase(domains)
                loadDomains()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateDomain(domainId: String, name: String, color: ULong) {
        viewModelScope.launch {
            try {
                updateDomainUseCase(domainId, name, color)
                loadDomains()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteDomainById(domainId: String) {
        viewModelScope.launch {
            try {
                deleteDomainByIdUseCase(domainId)
                loadDomains()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteAllDomains() {
        viewModelScope.launch {
            try {
                deleteAllDomainsUseCase()
                loadDomains()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

}
