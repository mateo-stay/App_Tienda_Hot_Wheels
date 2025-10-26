package com.example.tiendahotwheels.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseSuccessScreen(
    pedidoId: String,
    total: Double,
    onContinue: () -> Unit,
    onBackHome: () -> Unit
) {
    val fechaActual = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

    val hotRed = Color(0xFFFF1E00)
    val darkRed = Color(0xFF8B0000)
    val pureWhite = Color(0xFFFFFFFF)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Confirmación de Compra",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = hotRed
                )
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(colors = listOf(hotRed, darkRed))
                ),
            contentAlignment = Alignment.Center
        ) {

            Card(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = pureWhite)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Compra exitosa",
                        tint = hotRed,
                        modifier = Modifier.size(90.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = "¡Compra realizada con éxito!",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = hotRed
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Gracias por confiar en Hot Wheels Store ",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            textAlign = TextAlign.Center,
                            color = Color.DarkGray
                        )
                    )

                    Spacer(Modifier.height(24.dp))
                    HorizontalDivider(thickness = 1.dp)
                    Spacer(Modifier.height(16.dp))

                    Text(
                        " Detalles del pedido",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )

                    Spacer(Modifier.height(8.dp))
                    Text(" ID del Pedido: $pedidoId", color = Color.Gray)
                    Text(" Total: $${"%.0f".format(total)}", color = Color.Gray)
                    Text(" Fecha: $fechaActual", color = Color.Gray)
                    Text(" Confirmación enviada a: cliente@demo.cl", color = Color.Gray)

                    Spacer(Modifier.height(24.dp))
                    HorizontalDivider(thickness = 1.dp)
                    Spacer(Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onBackHome,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(" Ir al inicio")
                        }

                        Button(
                            onClick = onContinue,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = hotRed)
                        ) {
                            Text("Seguir comprando", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

