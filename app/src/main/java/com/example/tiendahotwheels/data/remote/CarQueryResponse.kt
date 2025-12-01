package com.example.tiendahotwheels.data.remote

import com.google.gson.annotations.SerializedName

data class CarQueryResponse(
    @SerializedName("Trims")
    val trims: List<CarQueryTrim>?
)

data class CarQueryTrim(
    val model_make_id: String?,
    val model_name: String?,
    val model_year: String?,
    val model_engine_power_ps: String?,
    val model_engine_type: String?,
    val model_engine_fuel: String?,
    val model_body: String?,
    val model_drive: String?
)
