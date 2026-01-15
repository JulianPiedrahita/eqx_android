package com.example.eqx_android.domain.usecase

import com.example.eqx_android.data.remote.SecureApiService
import com.example.eqx_android.domain.model.LoginResult
import com.example.eqx_android.domain.model.UserCredentials

class SecureLoginUseCase(private val apiService: SecureApiService) {
    suspend operator fun invoke(credentials: UserCredentials): LoginResult {
        // Aquí podrías agregar validaciones OWASP antes de enviar la petición
        return apiService.login(credentials)
    }
}
