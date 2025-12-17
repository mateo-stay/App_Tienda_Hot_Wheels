package com.example.tiendahotwheels.utils

object ValidadorRut {

    fun isValid(rut: String): Boolean {
        if (rut.isBlank()) return false

        val clean = rut.replace(".", "").replace("-", "").trim().uppercase()

        if (clean.length < 2) return false

        val cuerpo = clean.dropLast(1)
        val dvIngresado = clean.last()

        if (!cuerpo.all { it.isDigit() }) return false

        var suma = 0
        var multiplicador = 2

        for (c in cuerpo.reversed()) {
            suma += c.digitToInt() * multiplicador
            multiplicador = if (multiplicador < 7) multiplicador + 1 else 2
        }

        val resto = 11 - (suma % 11)

        val dvCorrecto = when (resto) {
            11 -> '0'
            10 -> 'K'
            else -> resto.digitToChar()
        }

        return dvIngresado == dvCorrecto
    }
}
