package com.example.eqx_android.domain.model

data class RegisterRequest(
    val email: String,
    val password: String,
    val captchaToken: String? = null,
    val extraData: Map<String, Any>? = null,
    val useFirebase: Boolean = false
)

data class RegisterResult(
    val success: Boolean,
    val message: String? = null,
    val error: String? = null
)
