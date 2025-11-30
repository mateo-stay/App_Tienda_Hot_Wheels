package com.example.tiendahotwheels.data

import android.content.Context
import com.example.tiendahotwheels.data.remote.ApiClient
import com.example.tiendahotwheels.model.Producto
import com.example.tiendahotwheels.model.toProducto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProductRepository(private val context: Context) {

    private val gson = Gson()
    private val api = ApiClient.api

    // AHORA ES suspend
    suspend fun cargarProductos(): List<Producto> {
        return try {
            // 1) Intentar desde la API
            val dtoList = api.getProductos()
            dtoList.map { it.toProducto() }
        } catch (e: Exception) {
            e.printStackTrace()
            // 2) Fallback a productos.json
            cargarProductosDesdeAssets()
        }
    }

    private fun cargarProductosDesdeAssets(): List<Producto> {
        return try {
            val json = context.assets
                .open("productos.json")
                .bufferedReader()
                .use { it.readText() }

            val tipoLista = object : TypeToken<List<Producto>>() {}.type
            gson.fromJson(json, tipoLista)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
