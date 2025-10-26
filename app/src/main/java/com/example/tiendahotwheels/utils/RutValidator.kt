package com.example.tiendahotwheels.utils

object RutValidator {

    fun isValid(rut: String): Boolean {
        val clean = rut.replace(".", "").replace("-", "").uppercase()
        if (clean.length < 8) return false
        val body = clean.dropLast(1)
        val dv = clean.last()
        var sum = 0
        var mul = 2
        for (c in body.reversed()) {
            if (!c.isDigit()) return false
            sum += c.digitToInt() * mul
            mul = if (mul < 7) mul + 1 else 2
        }
        val rest = 11 - (sum % 11)
        val expected = when (rest) {
            11 -> '0'
            10 -> 'K'
            else -> rest.toString().first()
        }
        return dv == expected
    }
}
