package com.example.tiendahotwheels.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.tiendahotwheels.data.ProductRepository
import com.example.tiendahotwheels.model.ProductoCarrito
import com.example.tiendahotwheels.model.Pedido
import com.example.tiendahotwheels.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class ProductViewModel(context: Context) : ViewModel() {

    private val repo = ProductRepository(context)

    private val _catalogo = MutableStateFlow<List<Producto>>(emptyList())
    val catalogo = _catalogo.asStateFlow()

    private val _carrito = MutableStateFlow<List<ProductoCarrito>>(emptyList())
    val carrito = _carrito.asStateFlow()

    init {
        _catalogo.value = repo.cargarProductos()
    }


    fun agregarAlCarrito(producto: Producto) {
        val lista = _carrito.value.toMutableList()
        val index = lista.indexOfFirst { it.producto.id == producto.id }


        val productoCatalogo = _catalogo.value.find { it.id == producto.id }

        if (productoCatalogo != null && productoCatalogo.stock > 0) {
            if (index >= 0) {
                val existente = lista[index]
                if (existente.cantidad < productoCatalogo.stock) {
                    lista[index] = existente.copy(cantidad = existente.cantidad + 1)
                    productoCatalogo.stock -= 1
                }
            } else {
                lista.add(ProductoCarrito(producto, 1))
                productoCatalogo.stock -= 1
            }
            _carrito.value = lista
            _catalogo.value = _catalogo.value.toList()
        }
    }


    fun eliminarDelCarrito(id: String) {
        val lista = _carrito.value.toMutableList()
        val index = lista.indexOfFirst { it.producto.id == id }

        if (index >= 0) {
            val item = lista[index]
            val productoCatalogo = _catalogo.value.find { it.id == id }

            if (item.cantidad > 1) {
                lista[index] = item.copy(cantidad = item.cantidad - 1)
            } else {
                lista.removeAt(index)
            }


            productoCatalogo?.stock = (productoCatalogo?.stock ?: 0) + 1

            _carrito.value = lista
            _catalogo.value = _catalogo.value.toList()
        }
    }

    fun cambiarCantidad(id: String, nuevaCantidad: Int) {
        _carrito.value = _carrito.value.map {
            if (it.producto.id == id)
                it.copy(cantidad = nuevaCantidad.coerceAtLeast(1))
            else it
        }
    }

    fun total(): Double = _carrito.value.sumOf { it.producto.precio * it.cantidad }

    fun vaciarCarrito() {

        _carrito.value.forEach { item ->
            val productoCatalogo = _catalogo.value.find { it.id == item.producto.id }
            productoCatalogo?.stock = (productoCatalogo?.stock ?: 0) + item.cantidad
        }
        _carrito.value = emptyList()
    }

    fun checkout(simularFallo: Boolean = false): Pedido? {
        if (simularFallo || _carrito.value.isEmpty()) return null

        val pedido = Pedido(
            id = UUID.randomUUID().toString(),
            items = _carrito.value,
            total = total(),
            emailUsuario = "cliente@demo.cl"
        )

        vaciarCarrito()
        return pedido
    }

    fun enCarrito(id: String): Boolean = _carrito.value.any { it.producto.id == id }

    fun cantidadProducto(id: String): Int =
        _carrito.value.find { it.producto.id == id }?.cantidad ?: 0
}
