package com.example.eqx_android.data.remote

// Estructura base para OAuth/social login
interface OAuthService {
    suspend fun loginWithGoogle(token: String): String?
    suspend fun loginWithFacebook(token: String): String?
}
