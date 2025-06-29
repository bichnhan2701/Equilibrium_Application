package com.example.worklifebalance.domain.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult

interface AuthRepository {
    suspend fun signInWithCredential(credential: AuthCredential): Result<AuthResult>
    suspend fun signOut()
}
