package com.example.tiendahotwheels.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tiendahotwheels.viewmodel.ProductViewModel
import androidx.compose.foundation.text.KeyboardOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarProducto(
    navController: NavController,
    productoVM: ProductViewModel,
    productoId: String
) {
    val productos by productoVM.productos.collectAsState()
    val producto = productos.find { it.id == productoId }

    var nombre by rememberSaveable { mutableStateOf(producto?.nombre ?: "") }
    var precioText by rememberSaveable { mutableStateOf(producto?.precio?.toString() ?: "") }
    var descripcion by rememberSaveable { mutableStateOf(producto?.descripcion ?: "") }
    var imagenUrl by rememberSaveable { mutableStateOf(producto?.imagenUrl ?: "") }
    var stockText by rememberSaveable { mutableStateOf(producto?.stock?.toString() ?: "") }

    var errorLocal by rememberSaveable { mutableStateOf<String?>(null) }
    val cargando by productoVM.cargando.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (producto == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Producto no encontrado")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = precioText,
                    onValueChange = { valor ->
                        // permitimos números, punto y coma
                        precioText = valor.filter { it.isDigit() || it == '.' || it == ',' }
                    },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp)
                )

                OutlinedTextField(
                    value = imagenUrl,
                    onValueChange = { imagenUrl = it },
                    label = { Text("URL de imagen (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = stockText,
                    onValueChange = { valor ->
                        stockText = valor.filter { it.isDigit() }
                    },
                    label = { Text("Stock disponible") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                errorLocal?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        val precio = precioText.replace(",", ".").toDoubleOrNull()
                        val stock = stockText.toIntOrNull()

                        if (nombre.isBlank() || precio == null || stock == null) {
                            errorLocal = "Revisa nombre, precio y stock."
                            return@Button
                        }

                        val actualizado = producto.copy(
                            nombre = nombre,
                            precio = precio,
                            descripcion = descripcion,
                            imagenUrl = imagenUrl,
                            stock = stock
                            // imagen (Int) lo dejamos tal cual
                        )

                        productoVM.actualizarProducto(actualizado) { ok ->
                            if (ok) {
                                navController.popBackStack()
                            } else {
                                errorLocal = "Error al actualizar el producto."
                            }
                        }
                    },
                    enabled = !cargando,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (cargando) "Guardando..." else "Guardar cambios")
                }
            }
        }
    }
}

