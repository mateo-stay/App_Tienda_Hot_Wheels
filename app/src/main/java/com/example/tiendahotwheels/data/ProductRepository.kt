package com.example.tiendahotwheels.data

import android.content.Context
import com.example.tiendahotwheels.model.Producto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProductRepository(private val context: Context) {

    private val gson = Gson()

    fun cargarProductos(): List<Producto> {
        return try {
            // Intenta abrir el archivo productos.json desde la carpeta assets
            val json = context.assets.open("productos.json").bufferedReader().use { it.readText() }

            val tipoLista = object : TypeToken<List<Producto>>() {}.type
            gson.fromJson(json, tipoLista)
        } catch (e: Exception) {
            // Si el archivo no existe o hay error, retorna lista vacía y evita que la app se caiga
            e.printStackTrace()
            emptyList()
        }
    }
}

