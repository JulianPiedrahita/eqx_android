package com.example.eqx_android.data.remote

import com.example.eqx_android.domain.model.UserCredentials
import com.example.eqx_android.domain.model.LoginResult

interface ApiService {
    suspend fun login(credentials: UserCredentials): LoginResult
}
