package com.example.tiendahotwheels.data.remote

data class ProductoDto(
    val id: Long,
    val nombre: String,
    val precio: Double,
    val descripcion: String,
    val imagenUrl: String,
    val categoria: String,
    val stock: Int
)
