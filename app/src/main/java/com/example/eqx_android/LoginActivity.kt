package com.example.eqx_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.eqx_android.data.local.JwtDataStore
import com.example.eqx_android.domain.model.UserCredentials
import com.example.eqx_android.domain.usecase.LoginUseCase
import com.example.eqx_android.domain.usecase.LogoutUseCase
import com.example.eqx_android.util.Constants
import com.example.eqx_android.util.LoginAttemptLimiter
import com.example.eqx_android.util.Logger
import kotlinx.coroutines.launch
import com.example.eqx_android.data.remote.CaptchaService
import com.example.eqx_android.data.repository.OAuthServiceImpl

// Simulación de servicio captcha
class DummyCaptchaService : CaptchaService {
    override suspend fun verifyCaptcha(response: String): Boolean {
        // Simula siempre éxito
        return response == "dummy_captcha"
    }
}
class LoginActivity : ComponentActivity() {
    private val loginUseCase by lazy {
        com.example.eqx_android.domain.usecase.SecureLoginUseCase(
            com.example.eqx_android.data.repository.SecureApiServiceImpl(
                com.example.eqx_android.util.Constants.BASE_URL,
                this
            )
        )
    }
    private lateinit var jwtDataStore: JwtDataStore
    private lateinit var logoutUseCase: LogoutUseCase
    private lateinit var loginAttemptLimiter: LoginAttemptLimiter
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        jwtDataStore = JwtDataStore(this)
        logoutUseCase = LogoutUseCase(jwtDataStore)
        loginAttemptLimiter = LoginAttemptLimiter(this)

        val captchaService = DummyCaptchaService()
        var captchaVerified = false

        val oAuthService = OAuthServiceImpl()

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        val emailLabel = findViewById<TextView>(R.id.emailLabel)
        val passwordLabel = findViewById<TextView>(R.id.passwordLabel)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerTextView = findViewById<TextView>(R.id.registerTextView)

        // Botón Captcha
        val captchaButton = Button(this).apply {
            text = "Verificar Captcha"
            setOnClickListener {
                lifecycleScope.launch {
                    val result = captchaService.verifyCaptcha("dummy_captcha")
                    captchaVerified = result
                    if (result) {
                        Toast.makeText(this@LoginActivity, "Captcha verificado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@LoginActivity, "Captcha inválido", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        (findViewById<androidx.cardview.widget.CardView>(R.id.loginCardView)).addView(captchaButton)

        // Botón Google
        val googleButton = Button(this).apply {
            text = "Iniciar sesión con Google"
            setOnClickListener {
                // Simulación: token dummy
                val googleToken = "google_dummy_token"
                lifecycleScope.launch {
                    val result = oAuthService.loginWithGoogle(googleToken)
                    if (result != null) {
                        Toast.makeText(this@LoginActivity, "Login Google exitoso", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Error en login Google", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        (findViewById<androidx.cardview.widget.CardView>(R.id.loginCardView)).addView(googleButton)

        // Botón Facebook
        val facebookButton = Button(this).apply {
            text = "Iniciar sesión con Facebook"
            setOnClickListener {
                val fbToken = "facebook_dummy_token"
                lifecycleScope.launch {
                    val result = oAuthService.loginWithFacebook(fbToken)
                    if (result != null) {
                        Toast.makeText(this@LoginActivity, "Login Facebook exitoso", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Error en login Facebook", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        (findViewById<androidx.cardview.widget.CardView>(R.id.loginCardView)).addView(facebookButton)

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
            if (!captchaVerified) {
                Toast.makeText(this, "Verifica el captcha antes de continuar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            if (loginAttemptLimiter.isBlocked()) {
                Toast.makeText(this, "Demasiados intentos. Intenta más tarde.", Toast.LENGTH_LONG).show()
                Logger.e("LoginActivity", "Intentos bloqueados por seguridad")
                return@setOnClickListener
            }
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
                    if (result.success && result.token != null) {
                        // Guardar token cifrado
                        val encryptedToken = com.example.eqx_android.util.CryptoUtils.encrypt(result.token)
                        jwtDataStore.saveToken(encryptedToken)
                        loginAttemptLimiter.reset()
                        Logger.i("LoginActivity", "Login exitoso")
                        Toast.makeText(this@LoginActivity, "Inicio de sesión exitoso", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        loginAttemptLimiter.registerAttempt()
                        Logger.e("LoginActivity", "Login fallido: ${result.error}")
                        Toast.makeText(this@LoginActivity, result.error ?: "Credenciales incorrectas o error de red", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    loginAttemptLimiter.registerAttempt()
                    Logger.e("LoginActivity", "Error de red", e)
                    Toast.makeText(this@LoginActivity, "No se pudo conectar al servidor", Toast.LENGTH_LONG).show()
                }
            }
        }

        registerTextView.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }
}
