package com.example.tiendahotwheels.data.remote

import retrofit2.http.*
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface HotWheelsApi {

    @POST("api/auth/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @POST("api/auth/registro")
    suspend fun crearUsuario(@Body body: CrearUsuarioRequest): UsuarioDto

    @GET("api/productos")
    suspend fun listarProductos(
        @Header("Authorization") authHeader: String
    ): List<ProductoDto>

    @POST("api/productos")
    suspend fun crearProducto(
        @Body producto: ProductoDto,
        @Header("Authorization") authHeader: String
    ): ProductoDto

    @PUT("api/productos/{producto_id}")
    suspend fun actualizarProducto(
        @Path("producto_id") id: Long,
        @Body producto: ProductoDto,
        @Header("Authorization") authHeader: String
    ): ProductoDto

    @DELETE("api/productos/{producto_id}")
    suspend fun eliminarProducto(
        @Path("producto_id") id: Long,
        @Header("Authorization") authHeader: String
    )

    @PATCH("api/productos/{producto_id}/descontar/{cantidad}")
    suspend fun descontarStock(
        @Path("producto_id") id: Long,
        @Path("cantidad") cantidad: Int,
        @Header("Authorization") authHeader: String
    ): ProductoDto

    @PATCH("api/productos/{producto_id}/sumar/{cantidad}")
    suspend fun sumarStock(
        @Path("producto_id") id: Long,
        @Path("cantidad") cantidad: Int,
        @Header("Authorization") authHeader: String
    ): ProductoDto

    @Multipart
    @POST("upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): UploadImageResponse

}
