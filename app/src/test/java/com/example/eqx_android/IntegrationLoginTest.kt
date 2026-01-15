package com.example.eqx_android

import com.example.eqx_android.data.repository.SecureApiServiceImpl
import com.example.eqx_android.domain.model.UserCredentials
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Assert.assertNotNull
import org.junit.Test

class IntegrationLoginTest {
    private val baseUrl = "http://localhost:8000" // Cambia si tu backend usa otro puerto
    private val apiService = SecureApiServiceImpl(baseUrl)

    @Test
    fun loginWithValidCredentials_returnsToken() = runBlocking {
        val credentials = UserCredentials("test@example.com", "Password123!")
        val result = apiService.login(credentials)
        assertTrue(result.success)
        assertNotNull(result.token)
    }

    @Test
    fun loginWithInvalidCredentials_returnsError() = runBlocking {
        val credentials = UserCredentials("wrong@example.com", "badpass")
        val result = apiService.login(credentials)
        assertTrue(!result.success)
        assertNotNull(result.error)
    }
}
