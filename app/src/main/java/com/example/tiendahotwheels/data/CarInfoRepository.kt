package com.example.tiendahotwheels.data

import com.example.tiendahotwheels.data.remote.CarQueryApiClient
import com.example.tiendahotwheels.data.remote.CarQueryTrim

class CarInfoRepository {

    private val api = CarQueryApiClient.api

    suspend fun obtenerFicha(
        marca: String,
        modelo: String,
        year: String? = null
    ): CarQueryTrim? {
        return try {
            val response = api.getCarInfo(
                make = marca.lowercase(),
                model = modelo.lowercase(),
                year = year
            )
            response.trims?.firstOrNull()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
