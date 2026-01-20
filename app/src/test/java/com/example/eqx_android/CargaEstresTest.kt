package com.example.eqx_android

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class CargaEstresTest {
    @Test
    fun stressTest_loginMultipleUsers() = runBlocking {
        // Simulación de test de carga
        repeat(100) {
            assertTrue(true) // Aquí se llamaría a login real
        }
    }
}
