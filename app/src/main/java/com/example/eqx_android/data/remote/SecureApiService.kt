package com.example.eqx_android.data.remote

import com.example.eqx_android.domain.model.LoginResult
import com.example.eqx_android.domain.model.UserCredentials

interface SecureApiService {
    suspend fun login(credentials: UserCredentials): LoginResult
    // Puedes agregar más endpoints aquí siguiendo el modelo
}
