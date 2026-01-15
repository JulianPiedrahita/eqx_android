package com.example.eqx_android.util

object OwaspUtils {
    fun isValidEmail(email: String): Boolean {
        // Validación básica OWASP para email
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        return email.matches(emailRegex.toRegex())
    }

    fun isValidPassword(password: String): Boolean {
        // Reglas OWASP: mínimo 8 caracteres, mayúscula, minúscula, número, caracter especial
        val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{8,}$"
        return password.matches(passwordRegex.toRegex())
    }
}
