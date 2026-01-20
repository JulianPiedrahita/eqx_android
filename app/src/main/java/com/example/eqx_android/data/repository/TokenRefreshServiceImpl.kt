package com.example.eqx_android.data.repository

import com.example.eqx_android.data.remote.TokenRefreshService
import com.example.eqx_android.domain.model.LoginResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class TokenRefreshServiceImpl(private val baseUrl: String) : TokenRefreshService {
    private val client = OkHttpClient()
    override suspend fun refreshToken(refreshToken: String): LoginResult {
        return try {
            val json = JSONObject()
            json.put("refresh_token", refreshToken)
            val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
            val body = json.toString().toRequestBody(mediaType)
            val request = Request.Builder()
                .url(baseUrl + "/auth/refresh")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build()
            val response = client.newCall(request).execute()
            response.use {
                if (it.isSuccessful) {
                    val responseBody = it.body?.string()
                    val jsonResp = JSONObject(responseBody ?: "")
                    val token = jsonResp.optString("token", null)
                    LoginResult(true, token)
                } else {
                    LoginResult(false, null, "Refresh token inválido")
                }
            }
        } catch (e: Exception) {
            LoginResult(false, null, "Error de red")
        }
    }
}
