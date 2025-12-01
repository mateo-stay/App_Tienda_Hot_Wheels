package com.example.tiendahotwheels.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tiendahotwheels.model.Producto
import com.example.tiendahotwheels.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarProducto(
    navController: NavController,
    productoVM: ProductViewModel,
    productoInicial: Producto? = null   // null = crear, != null = editar
) {
    val rojoHot = Color(0xFFFF1E00)
    val rojoOscuro = Color(0xFFD90000)
    val blanco = Color.White

    val cargando by productoVM.cargando.collectAsState()
    val error by productoVM.error.collectAsState()

    var nombre by remember { mutableStateOf(productoInicial?.nombre ?: "") }
    var precioText by remember { mutableStateOf(productoInicial?.precio?.toString() ?: "") }
    var descripcion by remember { mutableStateOf(productoInicial?.descripcion ?: "") }
    var imagenUrl by remember { mutableStateOf(productoInicial?.imagenUrl ?: "") }
    var categoria by remember { mutableStateOf(productoInicial?.categoria ?: "") }
    var stockText by remember { mutableStateOf(productoInicial?.stock?.toString() ?: "") }

    val modoEdicion = productoInicial != null
    val tituloPantalla = if (modoEdicion) "Editar Producto" else "Agregar Producto"

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        tituloPantalla,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = rojoHot
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(rojoHot, rojoOscuro)))
                .padding(padding),
            contentAlignment = Alignment.TopCenter
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
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        if (modoEdicion) "Editar Datos" else "Nuevo Producto",
                        fontSize = 24.sp,
                        color = rojoHot,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre del producto") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = precioText,
                        onValueChange = { valor ->
                            precioText = valor.filter { it.isDigit() || it == '.' || it == ',' }
                        },
                        label = { Text("Precio (en CLP)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        minLines = 3
                    )

                    OutlinedTextField(
                        value = imagenUrl,
                        onValueChange = { imagenUrl = it },
                        label = { Text("URL de la imagen (opcional)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = categoria,
                        onValueChange = { categoria = it },
                        label = { Text("Categoría") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = stockText,
                        onValueChange = { valor ->
                            stockText = valor.filter { it.isDigit() }
                        },
                        label = { Text("Stock disponible") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val precio = precioText.replace(",", ".").toDoubleOrNull()
                            val stock = stockText.toIntOrNull()

                            if (nombre.isBlank() || precio == null || stock == null) {
                                // podrías mostrar un snackbar o estado de error local
                                return@Button
                            }

                            val producto = Producto(
                                id = productoInicial?.id ?: "",
                                nombre = nombre,
                                descripcion = descripcion,
                                precio = precio,
                                categoria = categoria,
                                imagenUrl = imagenUrl,
                                stock = stock
                            )

                            if (modoEdicion) {
                                productoVM.actualizarProducto(producto) { ok ->
                                    if (ok) navController.popBackStack()
                                }
                            } else {
                                productoVM.crearProducto(producto) { ok ->
                                    if (ok) navController.popBackStack()
                                }
                            }
                        },
                        enabled = !cargando,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = rojoHot),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        if (cargando) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(22.dp)
                            )
                        } else {
                            Text(
                                if (modoEdicion) "Guardar cambios" else "Guardar",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }

                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("Cancelar")
                    }

                    error?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}
