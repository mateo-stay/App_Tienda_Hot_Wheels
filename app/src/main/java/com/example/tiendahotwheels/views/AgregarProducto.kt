package com.example.tiendahotwheels.views

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.tiendahotwheels.model.Producto
import com.example.tiendahotwheels.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarProducto(
    navController: NavController,
    productoVM: ProductViewModel,
    productoInicial: Producto? = null
) {
    // 🎨 Colores marca
    val rojoHot = Color(0xFFFF1E00)
    val rojoOscuro = Color(0xFFD90000)
    val blanco = Color.White

    // 🔄 Estados VM
    val cargando by productoVM.cargando.collectAsState()
    val error by productoVM.error.collectAsState()

    // 🧠 Estados formulario
    var nombre by remember { mutableStateOf(productoInicial?.nombre ?: "") }
    var precioText by remember { mutableStateOf(productoInicial?.precio?.toString() ?: "") }
    var descripcion by remember { mutableStateOf(productoInicial?.descripcion ?: "") }
    var imagenUrl by remember { mutableStateOf(productoInicial?.imagenUrl ?: "") }
    var categoria by remember { mutableStateOf(productoInicial?.categoria ?: "") }
    var stockText by remember { mutableStateOf(productoInicial?.stock?.toString() ?: "") }

    val modoEdicion = productoInicial != null
    val tituloPantalla = if (modoEdicion) "Editar Producto" else "Agregar Producto"

    // 📸 Selector de imagen desde galería
    val launcherGaleria = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { imagenUrl = it.toString() }
    }

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

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(rojoHot, rojoOscuro)))
                .padding(padding)
        ) {
            // 📱 Adaptación a pantallas pequeñas
            val isSmallScreen = maxWidth < 360.dp
            val cardPadding = if (isSmallScreen) 12.dp else 24.dp
            val innerPadding = if (isSmallScreen) 16.dp else 24.dp
            val imageHeight = if (isSmallScreen) 140.dp else 180.dp

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Card(
                    modifier = Modifier
                        .padding(cardPadding)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()), // 🔥 SCROLL
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = blanco)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        // 🏷 Título interno
                        Text(
                            if (modoEdicion) "Editar Datos" else "Nuevo Producto",
                            fontSize = if (isSmallScreen) 20.sp else 24.sp,
                            color = rojoHot,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        // Nombre
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre del producto") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Precio
                        OutlinedTextField(
                            value = precioText,
                            onValueChange = {
                                precioText = it.filter { c ->
                                    c.isDigit() || c == '.' || c == ','
                                }
                            },
                            label = { Text("Precio (CLP)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Descripción
                        OutlinedTextField(
                            value = descripcion,
                            onValueChange = { descripcion = it },
                            label = { Text("Descripción") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            minLines = 3
                        )

                        // 🖼 Imagen
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = imagenUrl,
                                onValueChange = { imagenUrl = it },
                                label = { Text("URL imagen (opcional)") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Button(
                                onClick = { launcherGaleria.launch("image/*") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = rojoHot)
                            ) {
                                Text("Seleccionar imagen", color = Color.White)
                            }

                            if (imagenUrl.isNotBlank()) {
                                Image(
                                    painter = rememberAsyncImagePainter(imagenUrl),
                                    contentDescription = "Vista previa",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(imageHeight)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        // Categoría
                        OutlinedTextField(
                            value = categoria,
                            onValueChange = { categoria = it },
                            label = { Text("Categoría") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Stock
                        OutlinedTextField(
                            value = stockText,
                            onValueChange = { stockText = it.filter(Char::isDigit) },
                            label = { Text("Stock disponible") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // 💾 Guardar
                        Button(
                            onClick = {
                                val precio = precioText.replace(",", ".").toDoubleOrNull()
                                val stock = stockText.toIntOrNull()

                                if (nombre.isBlank() || precio == null || stock == null) return@Button

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
                                    productoVM.actualizarProducto(producto) {
                                        if (it) navController.popBackStack()
                                    }
                                } else {
                                    productoVM.crearProducto(producto) {
                                        if (it) navController.popBackStack()
                                    }
                                }
                            },
                            enabled = !cargando,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = rojoHot)
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
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Cancelar
                        TextButton(onClick = { navController.popBackStack() }) {
                            Text("Cancelar")
                        }

                        // Error
                        error?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}
