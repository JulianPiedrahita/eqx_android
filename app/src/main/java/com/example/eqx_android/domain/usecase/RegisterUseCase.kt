package com.example.eqx_android.domain.usecase

import com.example.eqx_android.data.remote.RegisterService
import com.example.eqx_android.domain.model.RegisterRequest
import com.example.eqx_android.domain.model.RegisterResult

class RegisterUseCase(private val registerService: RegisterService) {
    suspend fun execute(request: RegisterRequest): RegisterResult {
        return registerService.register(request)
    }
}
