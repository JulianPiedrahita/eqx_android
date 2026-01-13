package com.example.eqx_android.domain.model

data class LoginResult(
    val success: Boolean,
    val token: String? = null,
    val error: String? = null
)
