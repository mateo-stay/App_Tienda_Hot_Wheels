package com.example.tiendahotwheels.viewmodel

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

    // ---------- CARQUERY (FICHA TÉCNICA AUTO REAL) ----------
    private val carInfoRepository = CarInfoRepository()

    private val _carInfo = MutableStateFlow<CarQueryTrim?>(null)
    val carInfo: StateFlow<CarQueryTrim?> = _carInfo

    private val _cargandoCarInfo = MutableStateFlow(false)
    val cargandoCarInfo: StateFlow<Boolean> = _cargandoCarInfo

    init {
        cargarProductos()
    }

    // ------------- PRODUCTOS -------------

    fun cargarProductos() {
        viewModelScope.launch {
            try {
                _cargando.value = true
                _error.value = null
                val lista = repository.cargarProductos()
                _productos.value = lista
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido al cargar productos"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun obtenerProductoPorId(id: String): Producto? {
        return _productos.value.find { it.id == id }
    }

    fun crearProducto(producto: Producto, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                _cargando.value = true
                _error.value = null
                val creado = repository.crearProducto(producto)
                _productos.value = _productos.value + creado
                onResult(true)
            } catch (e: Exception) {
                _error.value = e.message
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
                _error.value = null
                val actualizado = repository.actualizarProducto(producto)
                _productos.value = _productos.value.map {
                    if (it.id == actualizado.id) actualizado else it
                }
                onResult(true)
            } catch (e: Exception) {
                _error.value = e.message
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
                _error.value = null
                repository.eliminarProducto(idProducto)
                _productos.value = _productos.value.filterNot { it.id == idProducto }
                onResult(true)
            } catch (e: Exception) {
                _error.value = e.message
                onResult(false)
            } finally {
                _cargando.value = false
            }
        }
    }

    // ------------- CARRITO -------------

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

    fun total(): Double {
        return _carrito.value.sumOf { it.producto.precio * it.cantidad }
    }

    fun limpiarCarrito() {
        _carrito.value = emptyList()
    }

    // ------------- FICHA TÉCNICA (CarQuery) -------------

    fun cargarFichaTecnica(producto: Producto) {
        // Ej: "Nissan Skyline" -> marca = "nissan", modelo = "skyline"
        val nombreLower = producto.nombre.lowercase().trim()
        val partes = nombreLower.split(" ").filter { it.isNotBlank() }

        if (partes.isEmpty()) {
            _carInfo.value = null
            return
        }

        val marca = partes.first()                    // primera palabra
        val modelo = partes.drop(1).joinToString(" ") // resto

        val modeloFinal = if (modelo.isBlank()) nombreLower else modelo
        val year: String? = null  // más adelante puedes usar un año real

        viewModelScope.launch {
            try {
                _cargandoCarInfo.value = true
                val ficha = carInfoRepository.obtenerFicha(
                    marca = marca,
                    modelo = modeloFinal,
                    year = year
                )
                _carInfo.value = ficha
            } catch (e: Exception) {
                e.printStackTrace()
                _carInfo.value = null
            } finally {
                _cargandoCarInfo.value = false
            }
        }
    }
}
