package com.example.worklifebalance.data.repository

import android.content.Context
import com.example.worklifebalance.domain.repository.AuthRepository
import com.example.worklifebalance.domain.repository.DomainRepository
import com.example.worklifebalance.domain.repository.EnergyRepository
import com.example.worklifebalance.domain.repository.GoalRepository
import com.example.worklifebalance.domain.repository.TaskExecutionRepository
import com.example.worklifebalance.domain.repository.TaskRepository
import com.example.worklifebalance.domain.utils.FirstLoginPrefs
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val appContext: Context,
) : AuthRepository {
    override suspend fun signInWithCredential(credential: AuthCredential): Result<AuthResult> {
        return try {
            val result = firebaseAuth.signInWithCredential(credential).await()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
        // Xoá cached Google account để tránh tự động đăng nhập lại
        GoogleSignIn.getClient(
            appContext,
            GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
            ).build()
        ).signOut().await()
    }
}
