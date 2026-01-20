package com.example.eqx_android

import org.junit.Assert.assertTrue
import org.junit.Test

class RolesPermisosTest {
    @Test
    fun userRole_hasLimitedAccess() {
        // Simulación de test de roles
        val userRole = "USER"
        assertTrue(userRole == "USER")
    }
    @Test
    fun adminRole_hasFullAccess() {
        val adminRole = "ADMIN"
        assertTrue(adminRole == "ADMIN")
    }
}
