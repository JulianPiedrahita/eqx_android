package com.example.eqx_android.data.repository

import com.example.eqx_android.data.remote.OAuthService

class OAuthServiceImpl : OAuthService {
    override suspend fun loginWithGoogle(token: String): String? {
        // Aquí iría la integración real con Google Sign-In
        // Simulación: retorna el token recibido
        return token
    }
    override suspend fun loginWithFacebook(token: String): String? {
        // Aquí iría la integración real con Facebook Login
        return token
    }
}
