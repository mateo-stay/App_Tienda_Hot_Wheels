package com.example.tiendahotwheels.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.PUT
import retrofit2.http.DELETE

// ---------- AUTH ----------

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val usuario: UsuarioDto
)

// ---------- USUARIOS ----------

data class CrearUsuarioRequest(
    val nombre: String,
    val email: String,
    val direccion: String,
    val rut: String,
    val password: String
)

data class UsuarioDto(
    val id: Long,
    val nombre: String,
    val email: String,
    val rol: String
)

// ---------- PRODUCTOS ----------

data class ProductoDto(
    val id: Long?,
    val nombre: String,
    val precio: Double,
    val descripcion: String,
    val imagenUrl: String?,
    val categoria: String,
    val stock: Int
)

interface HotWheelsApi {

    // --- AUTH ---

    @POST("api/auth/login")
    suspend fun login(
        @Body body: LoginRequest
    ): LoginResponse

    @GET("api/auth/me")
    suspend fun authMe(
        @Header("Authorization") authHeader: String
    ): UsuarioDto


    // --- USUARIOS ---

    @POST("api/usuarios")
    suspend fun crearUsuario(
        @Body body: CrearUsuarioRequest
    ): UsuarioDto


    // --- PRODUCTOS ---

    @GET("api/productos")
    suspend fun listarProductos(
        @Header("Authorization") authHeader: String
    ): List<ProductoDto>

    @GET("api/productos/{producto_id}")
    suspend fun obtenerProducto(
        @Path("producto_id") id: Long,
        @Header("Authorization") authHeader: String
    ): ProductoDto

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
}
