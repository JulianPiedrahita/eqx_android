package com.example.eqx_android.data.remote

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import com.example.eqx_android.data.local.JwtDataStore
import com.example.eqx_android.data.repository.TokenRefreshServiceImpl
import com.example.eqx_android.util.CryptoUtils
import kotlinx.coroutines.flow.firstOrNull

class AuthInterceptor(
    private val jwtDataStore: JwtDataStore,
    private val refreshService: TokenRefreshServiceImpl? = null // Puede ser null si no hay refresh
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val encryptedToken = runBlocking { jwtDataStore.getToken().firstOrNull() }
        val token = encryptedToken?.let { CryptoUtils.decrypt(it) } ?: ""
        val requestBuilder = original.newBuilder()
        if (token.isNotEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }
        var response = chain.proceed(requestBuilder.build())
        // Si la respuesta es 401 y hay refreshService, intentar refrescar token
        if (response.code == 401 && refreshService != null) {
            response.close()
            val refreshToken = token // Aquí deberías obtener el refresh token real
            val refreshResult = runBlocking { refreshService.refreshToken(refreshToken) }
            if (refreshResult.success && !refreshResult.token.isNullOrEmpty()) {
                val newEncrypted = CryptoUtils.encrypt(refreshResult.token)
                runBlocking { jwtDataStore.saveToken(newEncrypted) }
                val retryRequest = original.newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer ${refreshResult.token}")
                    .build()
                response = chain.proceed(retryRequest)
            }
        }
        return response
    }
}
