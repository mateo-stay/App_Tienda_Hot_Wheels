package com.example.tiendahotwheels.data

import android.content.Context
import com.example.tiendahotwheels.model.Producto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProductRepository(private val context: Context) {

    private val gson = Gson()

    fun cargarProductos(): List<Producto> {
        return try {
            val json = context.assets.open("productos.json").bufferedReader().use { it.readText() }

            val tipoLista = object : TypeToken<List<Producto>>() {}.type
            gson.fromJson(json, tipoLista)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}

