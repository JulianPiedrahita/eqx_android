package com.example.eqx_android.data.repository


import android.content.Context
import com.example.eqx_android.data.local.JwtDataStore
import com.example.eqx_android.data.remote.AuthInterceptor
import com.example.eqx_android.data.repository.TokenRefreshServiceImpl
import com.example.eqx_android.data.remote.SecureApiService
import com.example.eqx_android.domain.model.LoginResult
import com.example.eqx_android.domain.model.UserCredentials
import com.example.eqx_android.util.CryptoUtils
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit


class SecureApiServiceImpl(private val baseUrl: String, context: Context) : SecureApiService {
    private val jwtDataStore = JwtDataStore(context)
    private val refreshService = TokenRefreshServiceImpl(baseUrl)
    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(jwtDataStore, refreshService))
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    override suspend fun login(credentials: UserCredentials): LoginResult {
        return try {
            val json = JSONObject()
            json.put("email", credentials.email)
            json.put("password", credentials.password)
            val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
            val body = json.toString().toRequestBody(mediaType)
            val request = Request.Builder()
                .url(baseUrl + "/auth/login")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("X-Content-Type-Options", "nosniff") // OWASP
                .addHeader("X-Frame-Options", "DENY") // OWASP
                .addHeader("Cache-Control", "no-store") // OWASP
                .build()
            val response = client.newCall(request).execute()
            response.use {
                if (it.isSuccessful) {
                    val responseBody = it.body?.string()
                    val jsonResp = JSONObject(responseBody ?: "")
                    val token = jsonResp.optString("token", null)
                    // Guardar token cifrado si existe
                    if (!token.isNullOrEmpty()) {
                        val encrypted = CryptoUtils.encrypt(token)
                        runBlocking { jwtDataStore.saveToken(encrypted) }
                    }
                    LoginResult(true, token)
                } else {
                    LoginResult(false, null, "Credenciales incorrectas")
                }
            }
        } catch (e: Exception) {
            LoginResult(false, null, "Error de red")
        }
    }
}
