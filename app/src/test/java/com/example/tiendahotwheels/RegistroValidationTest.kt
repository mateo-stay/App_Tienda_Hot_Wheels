package com.example.tiendahotwheelsimport org.junit.Assert.*
import org.junit.Test

class RegistroValidationTest {

    private fun validarRegistro(
        nombre: String,
        email: String,
        password: String,
        confirmarPassword: String
    ): Boolean {
        if (nombre.isBlank()) return false
        if (!email.contains("@")) return false
        if (password.length < 6) return false
        if (password != confirmarPassword) return false
        return true
    }

    @Test
    fun registro_valido_retorna_true() {
        val resultado = validarRegistro(
            nombre = "Mateo",
            email = "mateo@hotwheels.cl",
            password = "123456",
            confirmarPassword = "123456"
        )
        assertTrue(resultado)
    }

    @Test
    fun registro_con_password_distintas_retorna_false() {
        val resultado = validarRegistro(
            nombre = "Mateo",
            email = "mateo@hotwheels.cl",
            password = "123456",
            confirmarPassword = "654321"
        )
        assertFalse(resultado)
    }

    @Test
    fun registro_con_email_invalido_retorna_false() {
        val resultado = validarRegistro(
            nombre = "Mateo",
            email = "mateohotwheels.cl",
            password = "123456",
            confirmarPassword = "123456"
        )
        assertFalse(resultado)
    }

    @Test
    fun registro_con_nombre_vacio_retorna_false() {
        val resultado = validarRegistro(
            nombre = "",
            email = "mateo@hotwheels.cl",
            password = "123456",
            confirmarPassword = "123456"
        )
        assertFalse(resultado)
    }
}
