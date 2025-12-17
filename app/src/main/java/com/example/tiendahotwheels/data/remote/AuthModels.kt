package com.example.tiendahotwheels.data.remote

// ---------- AUTH ----------
data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val usuario: UsuarioDto
)

// ---------- USUARIOS ----------
data class CrearUsuarioRequest(
    val nombre: String,
    val email: String,
    val password: String
)

data class UsuarioDto(
    val id: Long,
    val nombre: String,
    val email: String,
    val rol: String
)

// ---------- PRODUCTOS ----------
data class ProductoDto(
    val id: Long?,
    val nombre: String,
    val precio: Double,
    val descripcion: String,
    val imagenUrl: String?,
    val categoria: String,
    val stock: Int
)
