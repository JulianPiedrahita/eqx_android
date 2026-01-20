package com.example.eqx_android.data.remote

import com.example.eqx_android.domain.model.RegisterRequest
import com.example.eqx_android.domain.model.RegisterResult

interface RegisterService {
    suspend fun register(request: RegisterRequest): RegisterResult
}
