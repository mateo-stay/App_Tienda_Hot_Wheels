package com.example.tiendahotwheels.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendahotwheels.data.ProductRepository
import com.example.tiendahotwheels.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// 👇 Modelo interno para los items del carrito
data class ItemCarrito(
    val producto: Producto,
    val cantidad: Int
)

class ProductViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    // 🎯 Catálogo de productos
    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    // 🛒 Carrito de compras
    private val _carrito = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val carrito: StateFlow<List<ItemCarrito>> = _carrito

    // Estados de carga / error
    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            try {
                _cargando.value = true
                _error.value = null

                val lista = repository.cargarProductos()
                _productos.value = lista
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido"
            } finally {
                _cargando.value = false
            }
        }
    }

    // 🛒 Agregar producto al carrito
    fun agregarAlCarrito(producto: Producto) {
        val listaActual = _carrito.value.toMutableList()
        val index = listaActual.indexOfFirst { it.producto.id == producto.id }

        if (index >= 0) {
            val existente = listaActual[index]
            listaActual[index] = existente.copy(cantidad = existente.cantidad + 1)
        } else {
            listaActual.add(ItemCarrito(producto = producto, cantidad = 1))
        }

        _carrito.value = listaActual
    }

    // ❌ Eliminar producto del carrito (por id)
    fun eliminarDelCarrito(idProducto: String) {
        val listaActual = _carrito.value.toMutableList()
        val index = listaActual.indexOfFirst { it.producto.id == idProducto }

        if (index >= 0) {
            val item = listaActual[index]
            if (item.cantidad > 1) {
                listaActual[index] = item.copy(cantidad = item.cantidad - 1)
            } else {
                listaActual.removeAt(index)
            }
            _carrito.value = listaActual
        }
    }

    // 💰 Total del carrito
    fun total(): Double {
        return _carrito.value.sumOf { it.producto.precio * it.cantidad }
    }

    // Opcional: limpiar carrito después de compra
    fun limpiarCarrito() {
        _carrito.value = emptyList()
    }
}
