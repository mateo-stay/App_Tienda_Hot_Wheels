package com.example.tiendahotwheels.data

import com.example.tiendahotwheels.model.Usuario  // 👈 este import es esencial

class AuthRepository {

    // Base de datos falsa en memoria
    private val usuarios = mutableListOf<Usuario>()
    private var usuarioActual: Usuario? = null

    // Registrar nuevo usuario
    fun registrar(usuario: Usuario): String? {
        // Evita duplicar correos registrados
        if (usuarios.any { it.email.equals(usuario.email, ignoreCase = true) }) {
            return "El correo ya está registrado"
        }
        usuarios.add(usuario)
        return null // null = éxito
    }

    // Iniciar sesión
    fun login(email: String): Usuario? {
        val usuario = usuarios.find { it.email.equals(email, ignoreCase = true) }
        usuarioActual = usuario
        return usuario
    }

    // Obtener usuario actual
    fun getUsuarioActual(): Usuario? = usuarioActual

    // Cerrar sesión
    fun logout() {
        usuarioActual = null
    }
}
