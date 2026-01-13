package com.example.eqx_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.eqx_android.data.repository.ApiServiceImpl
import com.example.eqx_android.domain.model.UserCredentials
import com.example.eqx_android.domain.usecase.LoginUseCase
import com.example.eqx_android.util.Constants
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    private val loginUseCase by lazy {
        LoginUseCase(ApiServiceImpl(Constants.BASE_URL))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerTextView = findViewById<TextView>(R.id.registerTextView)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                val result = loginUseCase(UserCredentials(email, password))
                if (result.success) {
                    Toast.makeText(this@LoginActivity, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, result.error ?: "Error desconocido", Toast.LENGTH_SHORT).show()
                }
            }
        }

        registerTextView.setOnClickListener {
            Toast.makeText(this, "Funcionalidad de registro próximamente", Toast.LENGTH_SHORT).show()
        }
    }
}
