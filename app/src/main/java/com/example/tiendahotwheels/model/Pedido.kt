package com.example.tiendahotwheels.model

data class Pedido(
    val id: String,
    val items: List<CartItem>,
    val total: Int,
    val emailUsuario: String
)
