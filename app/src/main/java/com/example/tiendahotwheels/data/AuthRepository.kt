package com.example.tiendahotwheels.data

import android.content.Context
import com.example.tiendahotwheels.data.remote.ApiClient
import com.example.tiendahotwheels.data.remote.CrearUsuarioRequest
import com.example.tiendahotwheels.data.remote.LoginRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(private val context: Context) {

    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun getToken(): String? = prefs.getString("token", null)

    private fun guardarToken(token: String) {
        prefs.edit().putString("token", token).apply()
    }

    // ---------------- LOGIN ----------------
    suspend fun login(email: String, password: String): String? =
        withContext(Dispatchers.IO) {
            try {
                val response = ApiClient.api.login(LoginRequest(email, password))

                if (response.token.isBlank())
                    return@withContext "Token vacío"

                guardarToken(response.token)
                null // éxito
            } catch (e: Exception) {
                e.message ?: "Error en login"
            }
        }

    // ---------------- REGISTRO ----------------
    suspend fun registrar(nombre: String, email: String, password: String): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val request = CrearUsuarioRequest(
                    nombre = nombre,
                    email = email,
                    password = password
                )

                ApiClient.api.crearUsuario(request)
                true
            } catch (e: Exception) {
                false
            }
        }

    fun logout() {
        prefs.edit().clear().apply()
    }
}
