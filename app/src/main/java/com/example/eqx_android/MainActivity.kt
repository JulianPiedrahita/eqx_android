package com.example.eqx_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.eqx_android.data.local.JwtDataStore
import com.example.eqx_android.domain.usecase.LogoutUseCase
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var jwtDataStore: JwtDataStore
    private lateinit var logoutUseCase: LogoutUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        jwtDataStore = JwtDataStore(this)
        logoutUseCase = LogoutUseCase(jwtDataStore)

        val logoutButton = Button(this).apply {
            text = "Cerrar sesión"
            setOnClickListener {
                lifecycleScope.launch {
                    logoutUseCase()
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }
        (findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.main_layout))
            .addView(logoutButton)
    }
}