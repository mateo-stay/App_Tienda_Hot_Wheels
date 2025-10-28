package com.example.tiendahotwheels.model

data class Producto(
    val id: String,
    val nombre: String,
    val precio: Double,
    val descripcion: String,
    val imagen: String,
    val categoria: String,
    var stock: Int
)
