package com.example.eqx_android.domain.usecase

import com.example.eqx_android.data.remote.ApiService
import com.example.eqx_android.domain.model.UserCredentials
import com.example.eqx_android.domain.model.LoginResult

class LoginUseCase(private val apiService: ApiService) {
    suspend operator fun invoke(credentials: UserCredentials): LoginResult {
        return apiService.login(credentials)
    }
}
