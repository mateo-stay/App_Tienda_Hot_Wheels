package com.example.tiendahotwheels.ui

import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.tiendahotwheels.MainActivity
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

class CarritoVacioTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun app_se_mantiene_estable_con_carrito_vacio() {

        // Espera a que la pantalla cargue
        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule
                .onAllNodes(hasClickAction())
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        // No se agrega ningún producto al carrito
        // Validamos que la app no crashea ni se bloquea

        assertNotNull(composeTestRule.activity)
    }
}
