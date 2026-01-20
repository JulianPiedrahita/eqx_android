package com.example.eqx_android.data.remote

// Simulación de servicio de captcha (puede integrarse con reCAPTCHA u otro)
interface CaptchaService {
    suspend fun verifyCaptcha(response: String): Boolean
}
