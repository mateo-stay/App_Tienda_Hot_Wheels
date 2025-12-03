package com.example.tiendahotwheels.data

import android.content.Context
import com.example.tiendahotwheels.model.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

private const val BASE_URL = "http://10.0.2.2:8080/"

class ProductRepository(
    context: Context,
    private val authRepository: AuthRepository
) {

    private val client = OkHttpClient()
    private val jsonMediaType = "application/json".toMediaType()

    // --- Helper para armar request con Authorization ---
    private fun Request.Builder.withAuth(): Request.Builder {
        val token = authRepository.getToken()
        if (!token.isNullOrBlank()) {
            header("Authorization", "Bearer $token")
        }
        return this
    }

    // --- GET /api/productos ---
    suspend fun cargarProductos(): List<Producto> = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("$BASE_URL/productos")
            .withAuth()
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw RuntimeException("Error al cargar productos: ${response.code}")
            }

            val body = response.body?.string() ?: "[]"
            val jsonArray = JSONArray(body)
            val lista = mutableListOf<Producto>()

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                lista.add(
                    Producto(
                        id = obj.getLong("id").toString(),
                        nombre = obj.getString("nombre"),
                        descripcion = obj.optString("descripcion", ""),
                        precio = obj.getDouble("precio"),
                        categoria = obj.optString("categoria", ""),
                        imagenUrl = obj.optString("imagenUrl", ""),
                        stock = obj.optInt("stock", 0)      // 👈 AHORA SÍ
                    )
                )
            }
            lista
        }
    }

    // --- POST /api/productos ---
    suspend fun crearProducto(producto: Producto): Producto = withContext(Dispatchers.IO) {
        val json = JSONObject().apply {
            put("nombre", producto.nombre)
            put("descripcion", producto.descripcion)
            put("precio", producto.precio)
            put("categoria", producto.categoria)
            put("imagenUrl", producto.imagenUrl)
            put("stock", producto.stock)   // 👈 ENVIAMOS STOCK
        }

        val body = json.toString().toRequestBody(jsonMediaType)

        val request = Request.Builder()
            .url("$BASE_URL/productos")
            .withAuth()
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw RuntimeException("Error al crear producto: ${response.code}")
            }

            val resBody = response.body?.string() ?: throw RuntimeException("Respuesta vacía")
            val obj = JSONObject(resBody)

            Producto(
                id = obj.getLong("id").toString(),
                nombre = obj.getString("nombre"),
                descripcion = obj.optString("descripcion", ""),
                precio = obj.getDouble("precio"),
                categoria = obj.optString("categoria", ""),
                imagenUrl = obj.optString("imagenUrl", ""),
                stock = obj.optInt("stock", 0)      // 👈 LEEMOS STOCK
            )
        }
    }

    // --- PUT /api/productos/{id} ---
    suspend fun actualizarProducto(producto: Producto): Producto = withContext(Dispatchers.IO) {
        val json = JSONObject().apply {
            put("nombre", producto.nombre)
            put("descripcion", producto.descripcion)
            put("precio", producto.precio)
            put("categoria", producto.categoria)
            put("imagenUrl", producto.imagenUrl)
            put("stock", producto.stock)   // 👈 ENVIAMOS STOCK TAMBIÉN
        }

        val body = json.toString().toRequestBody(jsonMediaType)

        val request = Request.Builder()
            .url("$BASE_URL/productos/${producto.id}")
            .withAuth()
            .put(body)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw RuntimeException("Error al actualizar producto: ${response.code}")
            }

            val resBody = response.body?.string() ?: throw RuntimeException("Respuesta vacía")
            val obj = JSONObject(resBody)

            Producto(
                id = obj.getLong("id").toString(),
                nombre = obj.getString("nombre"),
                descripcion = obj.optString("descripcion", ""),
                precio = obj.getDouble("precio"),
                categoria = obj.optString("categoria", ""),
                imagenUrl = obj.optString("imagenUrl", ""),
                stock = obj.optInt("stock", 0)      // 👈 Y AQUÍ IGUAL
            )
        }
    }

    // --- DELETE /api/productos/{id} ---
    suspend fun eliminarProducto(idProducto: String) = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("$BASE_URL/productos/$idProducto")
            .withAuth()
            .delete()
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw RuntimeException("Error al eliminar producto: ${response.code}")
            }
        }
    }
}
