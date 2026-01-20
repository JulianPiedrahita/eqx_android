package com.example.eqx_android.domain.usecase

import com.example.eqx_android.data.local.JwtDataStore

class LogoutUseCase(private val jwtDataStore: JwtDataStore) {
    suspend operator fun invoke() {
        jwtDataStore.clearToken()
    }
}
