package com.example.tiendahotwheels.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

private const val BASE_URL = "http://10.0.2.2:8080/"

class AuthRepository(private val context: Context) {

    private val client = OkHttpClient()
    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun getToken(): String? = prefs.getString("token", null)

    private fun guardarToken(token: String) {
        prefs.edit().putString("token", token).apply()
    }

    // 🔹 Lo que tu ViewModel espera: String? (null = OK, mensaje = error)
    suspend fun login(email: String, password: String): String? =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val json = JSONObject().apply {
                    put("email", email)
                    put("password", password)
                }

                val body = json.toString()
                    .toRequestBody("application/json".toMediaType())

                val request = Request.Builder()
                    .url("$BASE_URL/auth/login")
                    .post(body)
                    .build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        // mensaje de error para el ViewModel
                        "Error al iniciar sesión: ${response.code}"
                    } else {
                        val responseBody = response.body?.string() ?: return@use "Respuesta vacía"
                        val obj = JSONObject(responseBody)
                        val token = obj.optString("token", "")

                        if (token.isBlank()) {
                            "La respuesta no contiene token"
                        } else {
                            guardarToken(token)
                            null // null = éxito
                        }
                    }
                }
            } catch (e: Exception) {
                e.message ?: "Error desconocido de red"
            }
        }

    // 🔹 Tu VM lo llama como `registrar(...)`
    suspend fun registrar(nombre: String, email: String, password: String): Boolean =
        withContext(Dispatchers.IO) {
            val json = JSONObject().apply {
                put("nombre", nombre)
                put("email", email)
                put("password", password)
            }

            val body = json.toString()
                .toRequestBody("application/json".toMediaType())

            val request = Request.Builder()
                .url("$BASE_URL/auth/registro")
                .post(body)
                .build()

            client.newCall(request).execute().use { response ->
                response.isSuccessful
            }
        }

    // 🔹 Tu VM lo llama como `logout()`
    fun logout() {
        prefs.edit().clear().apply()
    }
}
