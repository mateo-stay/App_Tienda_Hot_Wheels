package com.example.tiendahotwheels.data

import com.example.tiendahotwheels.model.Usuario

class AuthRepository {

    private val usuarios = mutableListOf<Usuario>()
    private var usuarioActual: Usuario? = null

    // Registrar nuevo usuario
    fun registrar(usuario: Usuario): String? {
        if (usuarios.any { it.email.equals(usuario.email, ignoreCase = true) }) {
            return "El correo ya está registrado"
        }
        usuarios.add(usuario)
        return null
    }

    // Iniciar sesión (email + password)
    fun login(email: String, password: String): Usuario? {
        val usuario = usuarios.find {
            it.email.equals(email, ignoreCase = true) && it.password == password
        }
        usuarioActual = usuario
        return usuario
    }

    fun getUsuarioActual(): Usuario? = usuarioActual

    fun logout() {
        usuarioActual = null
    }
}
