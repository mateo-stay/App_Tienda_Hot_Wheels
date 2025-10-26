package com.example.tiendahotwheels.model

data class Pedido(
    val id: String,
    val items: List<ProductoCarrito>,
    val total: Double,
    val emailUsuario: String
)
