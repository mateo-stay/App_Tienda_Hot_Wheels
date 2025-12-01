package com.example.tiendahotwheels.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendahotwheels.data.AuthRepository
import com.example.tiendahotwheels.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repo: AuthRepository
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual = _usuarioActual.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // --------- Validaciones locales ---------

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
        val patronCorreo =
            Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
        return patronCorreo.matches(email)
    }

    // ---------- REGISTRO (API) ----------
    fun registrar(
        nombre: String,
        email: String,
        direccion: String,
        rut: String,
        password: String,
        onResult: (String?) -> Unit
    ) {
        if (nombre.isBlank() || email.isBlank() || direccion.isBlank() || rut.isBlank() || password.isBlank()) {
            val msg = "Por favor completa todos los campos requeridos."
            _error.value = msg
            onResult(msg)
            return
        }

        if (!validarRut(rut)) {
            val msg = "El RUT ingresado no es válido."
            _error.value = msg
            onResult(msg)
            return
        }

        if (!validarCorreo(email)) {
            val msg = "El correo electrónico no es válido."
            _error.value = msg
            onResult(msg)
            return
        }

        if (password.length < 6) {
            val msg = "La contraseña debe tener al menos 6 caracteres."
            _error.value = msg
            onResult(msg)
            return
        }

        val nuevoUsuario = Usuario(nombre, email, direccion, rut, password)

        viewModelScope.launch {
            _cargando.value = true
            _error.value = null

            // El repo tiene: suspend fun registrar(nombre,email,password): Boolean
            val exito = repo.registrar(nombre, email, password)

            _cargando.value = false

            val msgError = if (!exito) "Error al registrar usuario." else null
            if (msgError != null) {
                _error.value = msgError
            } else {
                _usuarioActual.value = nuevoUsuario
            }

            onResult(msgError)   // null = OK, string = error
        }
    }

    // ---------- LOGIN (API) ----------
    fun login(
        email: String,
        password: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null

            // En el repo: suspend fun login(...): String?
            val errorRepo: String? = repo.login(email, password)

            _cargando.value = false

            if (errorRepo != null) {
                _error.value = errorRepo
                onResult(false)
            } else {
                onResult(true)
            }
        }
    }

    fun logout() {
        repo.logout()
        _usuarioActual.value = null
        _error.value = null
    }
}
