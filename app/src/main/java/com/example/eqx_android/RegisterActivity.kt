package com.example.eqx_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.eqx_android.domain.model.RegisterRequest
import com.example.eqx_android.domain.usecase.RegisterUseCase
import com.example.eqx_android.data.repository.RegisterServiceImpl
import com.example.eqx_android.util.Constants
import com.example.eqx_android.util.OwaspUtils
import kotlinx.coroutines.launch

class RegisterActivity : ComponentActivity() {
    private val registerUseCase by lazy {
        RegisterUseCase(RegisterServiceImpl(Constants.BASE_URL))
    }
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var captchaEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var loginTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        captchaEditText = findViewById(R.id.captchaEditText)
        registerButton = findViewById(R.id.registerButton)
        loginTextView = findViewById(R.id.loginTextView)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val captcha = captchaEditText.text.toString().trim()

            if (!OwaspUtils.isValidEmail(email)) {
                Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!OwaspUtils.isValidPassword(password)) {
                Toast.makeText(this, "Contraseña insegura", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (captcha.isEmpty()) {
                Toast.makeText(this, "Completa el captcha", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val request = RegisterRequest(email, password, captcha)
                val result = registerUseCase.execute(request)
                if (result.success) {
                    Toast.makeText(this@RegisterActivity, "Registro exitoso", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@RegisterActivity, result.error ?: "Error de registro", Toast.LENGTH_LONG).show()
                }
            }
        }

        loginTextView.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
