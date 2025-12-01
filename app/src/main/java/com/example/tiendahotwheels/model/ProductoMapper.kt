package com.example.tiendahotwheels.model

import com.example.tiendahotwheels.data.remote.ProductoDto

// De la API → modelo interno
fun ProductoDto.toProducto(): Producto {
    return Producto(
        id = (this.id ?: 0L).toString(),
        nombre = this.nombre,
        descripcion = this.descripcion,
        precio = this.precio,
        categoria = this.categoria,
        imagenUrl = this.imagenUrl ?: "",  // 👈 solo URL
        stock = this.stock
    )
}

// Del modelo interno → DTO para la API
fun Producto.toDto(): ProductoDto {
    return ProductoDto(
        id = this.id.toLongOrNull(),   // si es nuevo, irá null
        nombre = this.nombre,
        descripcion = this.descripcion,
        precio = this.precio,
        categoria = this.categoria,
        imagenUrl = this.imagenUrl,    // mandamos la URL
        stock = this.stock
    )
}
