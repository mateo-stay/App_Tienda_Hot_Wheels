package com.example.tiendahotwheels.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PurchaseSuccessScreen(
    pedidoId: String,
    total: Int,
    onContinue: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("✅ ¡Compra Exitosa!", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))
            Text("ID Pedido: $pedidoId")
            Text("Total: $${total}")
            Spacer(Modifier.height(24.dp))
            Button(onClick = onContinue) {
                Text("Seguir comprando 🏎️")
            }
        }
    }
}
