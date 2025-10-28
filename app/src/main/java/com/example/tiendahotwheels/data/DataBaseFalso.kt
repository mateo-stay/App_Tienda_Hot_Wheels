package com.example.tiendahotwheels.data

import com.example.tiendahotwheels.model.Usuario

object DataBaseFalso {

    val usuarios = mutableListOf(
        Usuario(
            nombre = "Administrador",
            email = "admin@tienda.cl",
            direccion = "Oficina Central Santiago",
            rut = "11.111.111-1",
            password = "admin123"
        ),
        Usuario(
            nombre = "Mateo Estay",
            email = "mateo@gmail.com",
            direccion = "Av. San Marta 668, San Bernardo",
            rut = "19.912.994-5",
            password = "mateo123"
        ),
        Usuario(
            nombre = "Agustin Varas",
            email = "agustin@gmail.com",
            direccion = "Av. San Marta 668, San Bernardo",
            rut = "19.912.994-5",
            password = "agustin123"
        )
    )
}