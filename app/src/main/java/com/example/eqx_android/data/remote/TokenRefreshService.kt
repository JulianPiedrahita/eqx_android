package com.example.eqx_android.data.remote

import com.example.eqx_android.domain.model.LoginResult

interface TokenRefreshService {
    suspend fun refreshToken(refreshToken: String): LoginResult
}
