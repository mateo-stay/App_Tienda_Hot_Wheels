package com.example.tiendahotwheels.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompraFallida(
    onIntentarDeNuevo: () -> Unit,
    onVolverCarrito: () -> Unit
) {

    val RojoClaro = Color(0xFFFF1E00)
    val blanco = Color(0xFFFFFFFF)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Error en la compra",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = RojoClaro
                )
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(colors = listOf(RojoClaro, RojoClaro))
                ),
            contentAlignment = Alignment.Center
        ) {

            Card(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = blanco)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "Compra fallida",
                        tint = RojoClaro,
                        modifier = Modifier.size(90.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "¡Compra fallida!",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 25.sp,
                            color = RojoClaro
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Ocurrió un problema con el pago o no hay stock disponible.",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            textAlign = TextAlign.Center,
                            fontSize = 15.sp,
                            color = Color.DarkGray
                        )
                    )

                    Spacer(Modifier.height(24.dp))
                    HorizontalDivider(thickness = 1.dp)
                    Spacer(Modifier.height(24.dp))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(
                            onClick = onIntentarDeNuevo,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = RojoClaro)
                        ) {
                            Text("Intentar nuevamente", color = Color.White, fontSize = 15.sp)
                        }

                        Spacer(Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = onVolverCarrito,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Volver al carrito", color = RojoClaro, fontSize = 15.sp)
                        }
                    }
                }
            }
        }
    }
}
