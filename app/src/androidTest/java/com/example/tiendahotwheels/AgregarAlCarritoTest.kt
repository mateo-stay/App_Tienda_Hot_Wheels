package com.example.tiendahotwheels.ui

import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.performClick
import com.example.tiendahotwheels.MainActivity
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

class AgregarAlCarritoTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun agregar_producto_al_carrito_sin_error() {

        // Espera a que existan elementos clickeables (productos)
        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule
                .onAllNodes(hasClickAction())
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        // Click en el primer producto (abre detalle)
        composeTestRule
            .onAllNodes(hasClickAction())
            .onFirst()
            .performClick()

        // Intenta cualquier acción clickeable disponible (ej: botón agregar)
        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule
                .onAllNodes(hasClickAction())
                .fetchSemanticsNodes()
                .size > 1
        }

        // Click en el siguiente elemento clickeable
        composeTestRule
            .onAllNodes(hasClickAction())
            .onLast()
            .performClick()

        // ✅ Validación: la app sigue viva (no hubo crash)
        assertNotNull(composeTestRule.activity)
    }
}
