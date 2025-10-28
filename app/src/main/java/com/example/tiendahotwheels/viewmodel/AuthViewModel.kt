package com.example.tiendahotwheels.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tiendahotwheels.data.AuthRepository
import com.example.tiendahotwheels.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual = _usuarioActual.asStateFlow()

    private fun validarRut(rut: String): Boolean {
        val rutLimpio = rut.replace(".", "").replace("-", "").uppercase()
        if (rutLimpio.length < 2) return false

        val cuerpo = rutLimpio.dropLast(1)
        val dv = rutLimpio.last()

        if (!cuerpo.all { it.isDigit() }) return false

        var suma = 0
        var multiplicador = 2

        for (i in cuerpo.reversed()) {
            suma += (i.digitToInt()) * multiplicador
            multiplicador = if (multiplicador < 7) multiplicador + 1 else 2
        }

        val resto = 11 - (suma % 11)
        val dvEsperado = when (resto) {
            11 -> '0'
            10 -> 'K'
            else -> resto.digitToChar()
        }

        return dv == dvEsperado
    }

    private fun validarCorreo(email: String): Boolean {
        val patronCorreo = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
        return patronCorreo.matches(email)
    }

    fun registrar(
        nombre: String,
        email: String,
        direccion: String,
        rut: String,
        password: String
    ): String? {
        if (nombre.isBlank() || email.isBlank() || direccion.isBlank() || rut.isBlank() || password.isBlank()) {
            return "Por favor completa todos los campos requeridos."
        }

        if (!validarRut(rut)) {
            return "El RUT ingresado no es válido. Verifica el formato y el dígito verificador."
        }

        if (!validarCorreo(email)) {
            return "El correo electrónico no es válido. Asegúrate de incluir '@' y un dominio."
        }

        if (password.length < 6) {
            return "La contraseña debe tener al menos 6 caracteres."
        }

        val nuevoUsuario = Usuario(nombre, email, direccion, rut, password)
        val error = repo.registrar(nuevoUsuario)
        if (error == null) _usuarioActual.value = nuevoUsuario
        return error
    }

    fun login(email: String, password: String): Boolean {
        val usuario = repo.login(email, password)
        _usuarioActual.value = usuario
        return usuario != null
    }

    fun logout() {
        repo.logout()
        _usuarioActual.value = null
    }
}
