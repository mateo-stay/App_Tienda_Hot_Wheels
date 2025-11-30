package com.example.tiendahotwheels.model

import com.example.tiendahotwheels.data.remote.ProductoDto

fun ProductoDto.toProducto(): Producto {
    return Producto(
        id = this.id.toString(),
        nombre = this.nombre,
        precio = this.precio,
        descripcion = this.descripcion,
        imagen = this.imagenUrl,   // <- sin ?:
        categoria = this.categoria,
        stock = this.stock
    )
}

