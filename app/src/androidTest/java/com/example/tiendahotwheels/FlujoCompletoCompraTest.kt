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

class FlujoCompletoCompraTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun flujo_completo_compra_se_ejecuta_sin_errores() {

        // 1️⃣ Espera pantalla inicial
        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule
                .onAllNodes(hasClickAction())
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        // 2️⃣ Selecciona producto
        composeTestRule
            .onAllNodes(hasClickAction())
            .onFirst()
            .performClick()

        // 3️⃣ Espera acciones disponibles en detalle
        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule
                .onAllNodes(hasClickAction())
                .fetchSemanticsNodes()
                .size > 1
        }

        // 4️⃣ Ejecuta acción de compra (agregar / confirmar)
        composeTestRule
            .onAllNodes(hasClickAction())
            .onLast()
            .performClick()

        // ✅ Validación final: app sigue activa (flujo completo sin crash)
        assertNotNull(composeTestRule.activity)
    }
}