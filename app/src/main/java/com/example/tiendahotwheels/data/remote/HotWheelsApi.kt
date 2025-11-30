package com.example.tiendahotwheels.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface HotWheelsApi {

    @GET("api/productos")
    suspend fun getProductos(): List<ProductoDto>

    @GET("api/productos/{id}")
    suspend fun getProducto(@Path("id") id: Long): ProductoDto
}
