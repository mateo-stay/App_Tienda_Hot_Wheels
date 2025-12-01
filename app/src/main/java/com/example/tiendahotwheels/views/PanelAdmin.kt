package com.example.tiendahotwheels.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tiendahotwheels.model.Producto
import com.example.tiendahotwheels.viewmodel.ProductViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelAdministracion(
    navController: NavController,
    productoVM: ProductViewModel
) {
    val productos by productoVM.productos.collectAsState()
    val cargando by productoVM.cargando.collectAsState()
    val error by productoVM.error.collectAsState()

    // Colores
    val rojoHot = Color(0xFFFF1E00)
    val rojoOscuro = Color(0xFFD90000)
    val blanco = Color.White
    val grisClaro = Color(0xFFF5F5F5)
    val fondoGradiente = Brush.verticalGradient(
        colors = listOf(rojoHot, rojoOscuro)
    )

    // Formato de pesos chilenos
    val formatoPesos = remember {
        NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CL")).apply {
            maximumFractionDigits = 0
        }
    }

    // Snackbar y corrutina
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Producto seleccionado para eliminar
    var productoAEliminar by remember { mutableStateOf<Producto?>(null) }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Panel de Administración",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = rojoHot
                ),
                actions = {
                    Row {
                        IconButton(onClick = {
                            navController.navigate("agregar_producto")
                        }) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Agregar producto",
                                tint = Color.Black
                            )
                        }

                        IconButton(onClick = {
                            navController.navigate("inicio_sesion") {
                                popUpTo("inicio_sesion") { inclusive = true }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Cerrar sesión",
                                tint = Color.Black
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(fondoGradiente)
                .padding(padding)
        ) {

            when {
                cargando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }

                productos.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay productos cargados aún.",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }

                else -> {
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
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                                colors = CardDefaults.cardColors(containerColor = blanco)
                            ) {
                                Column(Modifier.padding(16.dp)) {

                                    Text(
                                        text = p.nombre,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                    )

                                    Spacer(Modifier.height(6.dp))

                                    Text(
                                        text = "Precio: ${formatoPesos.format(p.precio)}",
                                        color = rojoHot,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(Modifier.height(4.dp))

                                    Text(
                                        text = p.descripcion.take(120) +
                                                if (p.descripcion.length > 120) "..." else "",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = Color.DarkGray
                                        )
                                    )

                                    Spacer(Modifier.height(12.dp))
                                    HorizontalDivider(color = grisClaro, thickness = 1.dp)
                                    Spacer(Modifier.height(12.dp))

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // EDITAR
                                        OutlinedButton(
                                            onClick = {
                                                navController.navigate("editar_producto/${p.id}")
                                            },
                                            modifier = Modifier.weight(1f),
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                contentColor = Color.Black
                                            ),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Edit,
                                                contentDescription = "Editar"
                                            )
                                            Spacer(Modifier.width(4.dp))
                                            Text("Editar")
                                        }

                                        // ELIMINAR -> abre diálogo
                                        OutlinedButton(
                                            onClick = {
                                                productoAEliminar = p
                                            },
                                            modifier = Modifier.weight(1f),
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                contentColor = Color.Black
                                            ),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Eliminar"
                                            )
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

            // Mensaje de error general
            error?.let {
                Text(
                    text = it,
                    color = Color.Yellow,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(8.dp)
                )
            }

            // Diálogo de confirmación
            productoAEliminar?.let { producto ->
                AlertDialog(
                    onDismissRequest = { productoAEliminar = null },
                    title = {
                        Text(text = "Eliminar producto")
                    },
                    text = {
                        Text(text = "¿Seguro que deseas eliminar \"${producto.nombre}\"?")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                productoVM.eliminarProducto(producto.id) { exito ->
                                    productoAEliminar = null
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            if (exito)
                                                "Producto eliminado correctamente"
                                            else
                                                "Error al eliminar el producto"
                                        )
                                    }
                                }
                            }
                        ) {
                            Text(text = "Eliminar", color = rojoHot)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { productoAEliminar = null }
                        ) {
                            Text(text = "Cancelar")
                        }
                    }
                )
            }
        }
    }
}
