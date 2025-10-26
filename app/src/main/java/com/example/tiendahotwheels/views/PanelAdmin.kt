package com.example.tiendahotwheels.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tiendahotwheels.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackOfficeScreen(navController: NavController, productViewModel: ProductViewModel) {
    val productos by productViewModel.catalogo.collectAsState(initial = emptyList())

    val hotRed = Color(0xFFFF1E00)
    val darkRed = Color(0xFF8B0000)
    val white = Color(0xFFFFFFFF)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        " Back Office - Gestión de Productos",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = hotRed
                ),
                actions = {
                    IconButton(onClick = { navController.navigate("add_product") }) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Agregar producto",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(listOf(hotRed, darkRed))
                )
                .padding(padding)
        ) {
            if (productos.isEmpty()) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay productos cargados aún ",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            } else {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(productos) { p ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            colors = CardDefaults.cardColors(containerColor = white)
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(
                                    text = p.nombre,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    "Precio: $${p.precio}",
                                    color = hotRed,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = p.descripcion.take(120) + if (p.descripcion.length > 120) "..." else "",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray)
                                )
                                Spacer(Modifier.height(12.dp))

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedButton(
                                        onClick = { /* Solo visual */ },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = hotRed
                                        )
                                    ) {
                                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                                        Spacer(Modifier.width(4.dp))
                                        Text("Editar")
                                    }

                                    OutlinedButton(
                                        onClick = { /* Solo visual */ },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = Color.Red
                                        )
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                        Spacer(Modifier.width(4.dp))
                                        Text("Eliminar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
