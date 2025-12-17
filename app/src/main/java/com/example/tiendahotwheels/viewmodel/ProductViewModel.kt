package com.example.tiendahotwheels.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendahotwheels.data.ProductRepository
import com.example.tiendahotwheels.data.CarInfoRepository
import com.example.tiendahotwheels.data.remote.CarQueryTrim
import com.example.tiendahotwheels.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Modelo interno para items del carrito
data class ItemCarrito(
    val producto: Producto,
    val cantidad: Int
)

class ProductViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    // ---------- CATÁLOGO ----------
    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    // ---------- CARRITO ----------
    private val _carrito = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val carrito: StateFlow<List<ItemCarrito>> = _carrito

    // ---------- ESTADOS GLOBALES ----------
    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // ---------- CARQUERY ----------
    private val carInfoRepository = CarInfoRepository()

    private val _carInfo = MutableStateFlow<CarQueryTrim?>(null)
    val carInfo: StateFlow<CarQueryTrim?> = _carInfo

    private val _cargandoCarInfo = MutableStateFlow(false)
    val cargandoCarInfo: StateFlow<Boolean> = _cargandoCarInfo

    init {
        cargarProductos()
    }

    // ------------------ PRODUCTOS ------------------

    fun cargarProductos() {
        viewModelScope.launch {
            try {
                _cargando.value = true
                _error.value = null
                _productos.value = repository.cargarProductos()
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar productos"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun obtenerProductoPorId(id: String): Producto? =
        _productos.value.find { it.id == id }

    fun crearProducto(producto: Producto, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                _cargando.value = true
                val creado = repository.crearProducto(producto)
                _productos.value = _productos.value + creado
                onResult(true)
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al crear producto"
                onResult(false)
            } finally {
                _cargando.value = false
            }
        }
    }

    fun actualizarProducto(producto: Producto, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                _cargando.value = true
                val actualizado = repository.actualizarProducto(producto)
                _productos.value = _productos.value.map {
                    if (it.id == actualizado.id) actualizado else it
                }
                onResult(true)
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al actualizar producto"
                onResult(false)
            } finally {
                _cargando.value = false
            }
        }
    }

    fun eliminarProducto(idProducto: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                _cargando.value = true
                repository.eliminarProducto(idProducto)
                _productos.value = _productos.value.filterNot { it.id == idProducto }
                onResult(true)
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al eliminar producto"
                onResult(false)
            } finally {
                _cargando.value = false
            }
        }
    }

    // ------------------ CARRITO ------------------

    fun agregarAlCarrito(producto: Producto): Boolean {
        val lista = _carrito.value.toMutableList()
        val index = lista.indexOfFirst { it.producto.id == producto.id }

        if (index >= 0) {
            val item = lista[index]
            if (item.cantidad >= producto.stock) return false
            lista[index] = item.copy(cantidad = item.cantidad + 1)
        } else {
            if (producto.stock <= 0) return false
            lista.add(ItemCarrito(producto, 1))
        }

        _carrito.value = lista
        return true
    }

    fun eliminarDelCarrito(idProducto: String) {
        val lista = _carrito.value.toMutableList()
        val index = lista.indexOfFirst { it.producto.id == idProducto }

        if (index >= 0) {
            val item = lista[index]
            if (item.cantidad > 1) {
                lista[index] = item.copy(cantidad = item.cantidad - 1)
            } else {
                lista.removeAt(index)
            }
            _carrito.value = lista
        }
    }

    fun total(): Double =
        _carrito.value.sumOf { it.producto.precio * it.cantidad }

    fun limpiarCarrito() {
        _carrito.value = emptyList()
    }

    // ------------------ FICHA TÉCNICA ------------------

    fun cargarFichaTecnica(producto: Producto) {
        val partes = producto.nombre.lowercase().trim().split(" ")
        if (partes.isEmpty()) return

        val marca = partes.first()
        val modelo = partes.drop(1).joinToString(" ").ifBlank { marca }

        viewModelScope.launch {
            try {
                _cargandoCarInfo.value = true
                _carInfo.value = carInfoRepository.obtenerFicha(marca, modelo, null)
            } catch (_: Exception) {
                _carInfo.value = null
            } finally {
                _cargandoCarInfo.value = false
            }
        }
    }

    // ------------------ SUBIR IMAGEN ------------------

    fun subirImagen(uri: Uri, onResult: (String?) -> Unit) {
        viewModelScope.launch {
            try {
                val url = repository.subirImagen(uri)  // CORREGIDO
                onResult(url)
            } catch (e: Exception) {
                onResult(null)
            }
        }
    }

    // ------------------ COMPRA ------------------

    fun finalizarCompra(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                _cargando.value = true

                for (item in _carrito.value) {
                    repository.descontarStock(item.producto.id, item.cantidad)
                }

                cargarProductos()
                limpiarCarrito()
                onResult(true)

            } catch (e: Exception) {
                onResult(false)
            } finally {
                _cargando.value = false
            }
        }
    }
}
