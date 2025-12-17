package com.example.tiendahotwheels.model

import com.example.tiendahotwheels.data.remote.ProductoDto

// ---------- API → Modelo interno ----------
fun ProductoDto.toProducto(): Producto {
    return Producto(
        id = (id ?: 0L).toString(),
        nombre = nombre,
        descripcion = descripcion,
        precio = precio,
        categoria = categoria,
        imagenUrl = imagenUrl ?: "",
        stock = stock
    )
}

// ---------- Modelo interno → DTO para la API ----------
fun Producto.toDto(): ProductoDto {
    return ProductoDto(
        id = id.toLongOrNull(),    // null si es nuevo → lo crea el backend
        nombre = nombre,
        descripcion = descripcion,
        precio = precio,
        categoria = categoria,
        imagenUrl = imagenUrl,
        stock = stock
    )
}
