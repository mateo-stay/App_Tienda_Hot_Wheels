package com.example.tiendahotwheels.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PurchaseFailedScreen(
    onRetry: () -> Unit,
    onBackToCart: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("❌ Compra Rechazada", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(12.dp))
            Text("Ocurrió un error con el pago o el stock.")
            Spacer(Modifier.height(24.dp))
            Button(onClick = onBackToCart) {
                Text("Volver al carrito 🛒")
            }
            TextButton(onClick = onRetry) {
                Text("Intentar nuevamente 🔁")
            }
        }
    }
}
