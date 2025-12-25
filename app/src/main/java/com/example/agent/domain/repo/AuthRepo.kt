package com.example.agent.domain.repo


import kotlinx.coroutines.flow.Flow

import com.example.agent.data.local.dataStore.TokenDataStore
import com.example.agent.data.network.dto.LoginRequest
import com.greedygame.brokenandroidcomposeproject.network.api.ApiService


interface AuthRepository {


    suspend fun login(email: String,password: String): Result<Unit>


    fun observeToken(): Flow<String?>


    suspend fun logout()
}


class AuthRepositoryImpl(
    private val api: ApiService,
    private val tokenStore: TokenDataStore
) : AuthRepository {

    override suspend fun login(email: String,password:String): Result<Unit> {
        return try {
            val response = api.login(
                LoginRequest(
                    username = email,
                    password = password
                )
            )
            tokenStore.saveToken(response.auth_token)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeToken(): Flow<String?> {
        return tokenStore.tokenFlow
    }

    override suspend fun logout() {
        tokenStore.clearToken()
    }
}
