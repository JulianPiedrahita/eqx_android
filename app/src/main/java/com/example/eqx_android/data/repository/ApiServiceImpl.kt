package com.example.eqx_android.data.repository

import com.example.eqx_android.data.remote.ApiService
import com.example.eqx_android.domain.model.UserCredentials
import com.example.eqx_android.domain.model.LoginResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class ApiServiceImpl(private val baseUrl: String) : ApiService {
    private val client = OkHttpClient()

    override suspend fun login(credentials: UserCredentials): LoginResult {
        return try {
            val json = JSONObject()
            json.put("email", credentials.email)
            json.put("password", credentials.password)
            val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
            val body = json.toString().toRequestBody(mediaType)
            val request = Request.Builder()
                .url("") // Se inyecta el endpoint desde el UseCase
                .url(baseUrl + "/api/login")
                .post(body)
                .build()
            val response = client.newCall(request).execute()
            response.use {
                if (it.isSuccessful) {
                    val responseBody = it.body?.string()
                    // Extraer token si existe
                    val token = try { JSONObject(responseBody ?: "").optString("token", null) } catch (_: Exception) { null }
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
