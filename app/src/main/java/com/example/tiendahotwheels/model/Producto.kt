package com.example.tiendahotwheels.model

data class Producto(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val categoria: String,
    val imagenUrl: String,   // 👈 solo URL, nada de Int
    val stock: Int
)