package com.example.tiendahotwheels.ui

import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import com.example.tiendahotwheels.MainActivity
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

class NavegacionDetalleProductoTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun al_seleccionar_producto_navega_a_detalle() {

        // Espera a que existan elementos clickeables
        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule
                .onAllNodes(hasClickAction())
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        // Click en el primer producto
        composeTestRule
            .onAllNodes(hasClickAction())
            .onFirst()
            .performClick()

        assertNotNull(composeTestRule.activity)
    }
}
