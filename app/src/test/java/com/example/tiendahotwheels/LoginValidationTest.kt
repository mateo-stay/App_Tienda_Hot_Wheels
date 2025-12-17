package com.example.tiendahotwheelsimport org.junit.Assert.*
import org.junit.Test

class LoginValidationTest {

    private fun validarLogin(email: String, password: String): Boolean {
        if (email.isBlank() || password.isBlank()) return false
        if (!email.contains("@")) return false
        if (password.length < 6) return false
        return true
    }

    @Test
    fun login_valido_retorna_true() {
        val resultado = validarLogin(
            email = "mateo@hotwheels.cl",
            password = "123456"
        )
        assertTrue(resultado)
    }

    @Test
    fun login_con_email_invalido_retorna_false() {
        val resultado = validarLogin(
            email = "mateohotwheels.cl",
            password = "123456"
        )
        assertFalse(resultado)
    }

    @Test
    fun login_con_password_corta_retorna_false() {
        val resultado = validarLogin(
            email = "mateo@hotwheels.cl",
            password = "123"
        )
        assertFalse(resultado)
    }

    @Test
    fun login_con_campos_vacios_retorna_false() {
        val resultado = validarLogin(
            email = "",
            password = ""
        )
        assertFalse(resultado)
    }
}
