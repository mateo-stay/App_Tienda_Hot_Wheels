package com.example.tiendahotwheels.data

import android.content.Context
import android.net.Uri
import com.example.tiendahotwheels.data.remote.ApiClient
import com.example.tiendahotwheels.data.remote.ProductoDto
import com.example.tiendahotwheels.model.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// 🔥 IMPORTS NECESARIOS PARA SUBIR ARCHIVOS
import okhttp3.MultipartBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class ProductRepository(
    private val authRepository: AuthRepository,
    private val context: Context
) {

    private fun authHeader(): String {
        val token = authRepository.getToken()
        return "Bearer $token"
    }

    // ------------------ LISTAR ------------------
    suspend fun cargarProductos(): List<Producto> =
        withContext(Dispatchers.IO) {
            val listaDto = ApiClient.api.listarProductos(authHeader())
            listaDto.map { dto ->
                Producto(
                    id = dto.id.toString(),
                    nombre = dto.nombre,
                    descripcion = dto.descripcion,
                    precio = dto.precio,
                    categoria = dto.categoria,
                    imagenUrl = dto.imagenUrl ?: "",
                    stock = dto.stock
                )
            }
        }

    // ------------------ CREAR ------------------
    suspend fun crearProducto(producto: Producto): Producto =
        withContext(Dispatchers.IO) {
            val dto = ProductoDto(
                id = null,
                nombre = producto.nombre,
                descripcion = producto.descripcion,
                categoria = producto.categoria,
                precio = producto.precio,
                imagenUrl = producto.imagenUrl,
                stock = producto.stock
            )

            val creado = ApiClient.api.crearProducto(dto, authHeader())

            Producto(
                id = creado.id.toString(),
                nombre = creado.nombre,
                descripcion = creado.descripcion,
                categoria = creado.categoria,
                precio = creado.precio,
                imagenUrl = creado.imagenUrl ?: "",
                stock = creado.stock
            )
        }

    // ------------------ ACTUALIZAR ------------------
    suspend fun actualizarProducto(producto: Producto): Producto =
        withContext(Dispatchers.IO) {
            val dto = ProductoDto(
                id = producto.id.toLong(),
                nombre = producto.nombre,
                descripcion = producto.descripcion,
                categoria = producto.categoria,
                precio = producto.precio,
                imagenUrl = producto.imagenUrl,
                stock = producto.stock
            )

            val actualizado = ApiClient.api.actualizarProducto(
                producto.id.toLong(),
                dto,
                authHeader()
            )

            Producto(
                id = actualizado.id.toString(),
                nombre = actualizado.nombre,
                descripcion = actualizado.descripcion,
                categoria = actualizado.categoria,
                precio = actualizado.precio,
                imagenUrl = actualizado.imagenUrl ?: "",
                stock = actualizado.stock
            )
        }

    // ------------------ ELIMINAR ------------------
    suspend fun eliminarProducto(idProducto: String) =
        withContext(Dispatchers.IO) {
            ApiClient.api.eliminarProducto(idProducto.toLong(), authHeader())
        }

    // ------------------ STOCK ------------------
    suspend fun descontarStock(id: String, cantidad: Int) =
        withContext(Dispatchers.IO) {
            ApiClient.api.descontarStock(id.toLong(), cantidad, authHeader())
        }

    suspend fun sumarStock(id: String, cantidad: Int) =
        withContext(Dispatchers.IO) {
            ApiClient.api.sumarStock(id.toLong(), cantidad, authHeader())
        }

    // ------------------ SUBIR IMAGEN 🔥 ------------------
    suspend fun subirImagen(uri: Uri): String? {
        val stream = context.contentResolver.openInputStream(uri) ?: return null
        val bytes = stream.readBytes()

        val requestFile = bytes.toRequestBody("image/*".toMediaType())
        val part = MultipartBody.Part.createFormData(
            "file",
            "upload.jpg",
            requestFile
        )

        val res = ApiClient.api.uploadImage(part)
        return res.url
    }
}
