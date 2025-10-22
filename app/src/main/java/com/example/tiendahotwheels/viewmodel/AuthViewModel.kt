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

    fun registrar(nombre: String, email: String, direccion: String, rut: String): String? {
        val nuevoUsuario = Usuario(nombre, email, direccion, rut)
        val error = repo.registrar(nuevoUsuario)
        if (error == null) _usuarioActual.value = nuevoUsuario
        return error
    }

    fun login(email: String): Boolean {
        val usuario = repo.login(email)
        _usuarioActual.value = usuario
        return usuario != null
    }

    fun logout() {
        repo.logout()
        _usuarioActual.value = null
    }
}