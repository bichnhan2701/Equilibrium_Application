package com.example.worklifebalance.domain.usecase.auth

import com.example.worklifebalance.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import javax.inject.Inject

class SignInWithCredentialUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(credential: AuthCredential): Result<AuthResult> {
        return authRepository.signInWithCredential(credential)
    }
}