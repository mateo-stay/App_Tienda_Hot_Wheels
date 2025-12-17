package com.example.tiendahotwheels.model

data class Producto(
    val id: String = "",          // 👈 permite crear productos nuevos sin ID aún
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val categoria: String,
    val imagenUrl: String = "",   // 👈 URL opcional segura
    val stock: Int
)
