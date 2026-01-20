package com.example.eqx_android.data.repository

import com.example.eqx_android.data.remote.RegisterService
import com.example.eqx_android.domain.model.RegisterRequest
import com.example.eqx_android.domain.model.RegisterResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class RegisterServiceImpl(private val baseUrl: String) : RegisterService {
    private val client = OkHttpClient()
    override suspend fun register(request: RegisterRequest): RegisterResult {
        return try {
            val json = JSONObject()
            json.put("email", request.email)
            json.put("password", request.password)
            request.captchaToken?.let { json.put("captcha_token", it) }
            request.extraData?.let { json.put("extra_data", JSONObject(it)) }
            json.put("use_firebase", request.useFirebase)
            val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
            val body = json.toString().toRequestBody(mediaType)
            val httpRequest = Request.Builder()
                .url(baseUrl + "/auth/register")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("X-Content-Type-Options", "nosniff")
                .addHeader("X-Frame-Options", "DENY")
                .addHeader("Cache-Control", "no-store")
                .build()
            val response = client.newCall(httpRequest).execute()
            response.use {
                if (it.isSuccessful) {
                    val responseBody = it.body?.string()
                    val message = try { JSONObject(responseBody ?: "").optString("message", null) } catch (_: Exception) { null }
                    RegisterResult(true, message)
                } else {
                    val errorBody = it.body?.string()
                    val errorMsg = try { JSONObject(errorBody ?: "").optString("detail", "Error de registro") } catch (_: Exception) { "Error de registro" }
                    RegisterResult(false, null, errorMsg)
                }
            }
        } catch (e: Exception) {
            RegisterResult(false, null, "Error de red")
        }
    }
}
