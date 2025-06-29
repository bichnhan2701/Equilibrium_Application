package com.example.worklifebalance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worklifebalance.domain.usecase.auth.SignInWithCredentialUseCase
import com.example.worklifebalance.domain.usecase.auth.SignOutUseCase
import com.example.worklifebalance.domain.usecase.domain.DeleteAllDomainsUseCase
import com.example.worklifebalance.domain.usecase.energy.DeleteAllEnergyUseCase
import com.example.worklifebalance.domain.usecase.goal.DeleteAllGoalsUseCase
import com.example.worklifebalance.domain.usecase.task.DeleteAllTasksUseCase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInWithCredentialUseCase: SignInWithCredentialUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val deleteAllDomainsUseCase: DeleteAllDomainsUseCase,
    private val deleteAllTasksUseCase: DeleteAllTasksUseCase,
    private val deleteAllGoalsUseCase: DeleteAllGoalsUseCase,
    private val deleteAllEnergyUseCase: DeleteAllEnergyUseCase
) : ViewModel() {
    private val _signInState = MutableStateFlow<Result<AuthResult>?>(null)
    val signInState: StateFlow<Result<AuthResult>?> = _signInState.asStateFlow()

    private val _signOutState = MutableStateFlow<Result<Unit>?>(null)
    val signOutState: StateFlow<Result<Unit>?> = _signOutState.asStateFlow()

    fun signIn(credential: AuthCredential) {
        android.util.Log.d("AuthViewModel", "signIn() được gọi")
        viewModelScope.launch {
            val result = signInWithCredentialUseCase(credential)
            android.util.Log.d("AuthViewModel", "signInWithCredentialUseCase trả về: $result")
            _signInState.value = result
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                signOutUseCase()
                clearAllLocalData() // Xóa toàn bộ dữ liệu local sau khi sign out
                _signOutState.value = Result.success(Unit)
            } catch (e: Exception) {
                _signOutState.value = Result.failure(e)
            }
        }
    }

    fun clearSignInState() {
        _signInState.value = null
    }

    fun clearSignOutState() {
        _signOutState.value = null
    }

    fun clearAllLocalData() {
        viewModelScope.launch {
            try {
                deleteAllDomainsUseCase()
                deleteAllTasksUseCase()
                deleteAllGoalsUseCase()
                deleteAllEnergyUseCase()
            } catch (_: Exception) {}
        }
    }
}
