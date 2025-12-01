package com.example.tiendahotwheels.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface CarQueryApiService {

    @GET("api/0.3/")
    suspend fun getCarInfo(
        @Query("cmd") cmd: String = "getTrims",
        @Query("make") make: String,
        @Query("model") model: String,
        @Query("year") year: String? = null
    ): CarQueryResponse
}
