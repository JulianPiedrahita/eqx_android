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
        com.example.eqx_android.domain.usecase.SecureLoginUseCase(
            com.example.eqx_android.data.repository.SecureApiServiceImpl(com.example.eqx_android.util.Constants.BASE_URL)
        )
    }

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        val emailLabel = findViewById<TextView>(R.id.emailLabel)
        val passwordLabel = findViewById<TextView>(R.id.passwordLabel)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerTextView = findViewById<TextView>(R.id.registerTextView)

        emailEditText.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                val email = s.toString().trim()
                if (!com.example.eqx_android.util.OwaspUtils.isValidEmail(email)) {
                    emailLabel.error = "Correo inválido"
                } else {
                    emailLabel.error = null
                }
            }
        })

        passwordEditText.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                val password = s.toString().trim()
                if (!com.example.eqx_android.util.OwaspUtils.isValidPassword(password)) {
                    passwordLabel.error = "Mínimo 8 caracteres, mayúscula, minúscula y número"
                } else {
                    passwordLabel.error = null
                }
            }
        })

        emailEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val email = emailEditText.text.toString().trim()
                if (!com.example.eqx_android.util.OwaspUtils.isValidEmail(email)) {
                    emailLabel.error = "Correo inválido"
                } else {
                    emailLabel.error = null
                }
            }
        }

        passwordEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val password = passwordEditText.text.toString().trim()
                if (!com.example.eqx_android.util.OwaspUtils.isValidPassword(password)) {
                    passwordLabel.error = "Mínimo 8 caracteres, mayúscula, minúscula y número"
                } else {
                    passwordLabel.error = null
                }
            }
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!com.example.eqx_android.util.OwaspUtils.isValidEmail(email)) {
                Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!com.example.eqx_android.util.OwaspUtils.isValidPassword(password)) {
                Toast.makeText(this, "Contraseña inválida: mínimo 8 caracteres, mayúscula, minúscula y número", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                try {
                    val result = loginUseCase(com.example.eqx_android.domain.model.UserCredentials(email, password))
                    if (result.success) {
                        Toast.makeText(this@LoginActivity, "Inicio de sesión exitoso. Token: ${result.token}", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, result.error ?: "Credenciales incorrectas o error de red", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@LoginActivity, "No se pudo conectar al servidor", Toast.LENGTH_LONG).show()
                }
            }
        }

        registerTextView.setOnClickListener {
            Toast.makeText(this, "Funcionalidad de registro próximamente", Toast.LENGTH_SHORT).show()
        }
    }
}
