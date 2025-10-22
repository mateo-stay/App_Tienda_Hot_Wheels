package com.example.tiendahotwheels.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.tiendahotwheels.data.ProductRepository
import com.example.tiendahotwheels.model.CartItem
import com.example.tiendahotwheels.model.Pedido
import com.example.tiendahotwheels.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class ProductViewModel(
    context: Context
) : ViewModel() {

    private val repo = ProductRepository(context)

    private val _catalogo = MutableStateFlow<List<Producto>>(emptyList())
    val catalogo = _catalogo.asStateFlow()

    private val _carrito = MutableStateFlow<List<CartItem>>(emptyList())
    val carrito = _carrito.asStateFlow()

    init {
        _catalogo.value = repo.cargarProductos()
    }

    fun agregarAlCarrito(producto: Producto) {
        val lista = _carrito.value.toMutableList()
        val index = lista.indexOfFirst { it.producto.id == producto.id }

        if (index >= 0) {
            val existente = lista[index]
            lista[index] = existente.copy(cantidad = existente.cantidad + 1)
        } else {
            lista.add(CartItem(producto, 1))
        }

        _carrito.value = lista
    }

    fun eliminarDelCarrito(id: String) {
        _carrito.value = _carrito.value.filterNot { it.producto.id == id }
    }

    fun cambiarCantidad(id: String, nuevaCantidad: Int) {
        _carrito.value = _carrito.value.map {
            if (it.producto.id == id) it.copy(cantidad = nuevaCantidad.coerceAtLeast(1)) else it
        }
    }

    fun total(): Int {
        return _carrito.value.sumOf { it.producto.precio * it.cantidad }
    }

    fun checkout(simularFallo: Boolean = false): Pedido? {
        if (simularFallo || _carrito.value.isEmpty()) return null

        val pedido = Pedido(
            id = UUID.randomUUID().toString(),
            items = _carrito.value,
            total = total(),
            emailUsuario = "cliente@demo.cl"
        )

        _carrito.value = emptyList()
        return pedido
    }
}