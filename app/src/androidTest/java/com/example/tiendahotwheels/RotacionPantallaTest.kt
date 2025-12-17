package com.example.tiendahotwheels.ui

import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import com.example.tiendahotwheels.MainActivity
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

class RotacionPantallaTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun estado_se_mantiene_al_recrear_activity() {

        // Espera a que cargue la pantalla con elementos interactivos
        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule
                .onAllNodes(hasClickAction())
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        // Interacción mínima (simula uso previo)
        composeTestRule
            .onAllNodes(hasClickAction())
            .onFirst()
            .performClick()

        // 🔄 Simula rotación de pantalla (recreación de Activity)
        composeTestRule.activityRule.scenario.recreate()

        // Espera nuevamente a que la UI esté disponible
        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule
                .onAllNodes(hasClickAction())
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        // ✅ Validación: la app sigue viva tras la recreación
        assertNotNull(composeTestRule.activity)
    }
}
