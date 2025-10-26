package com.example.tiendahotwheels.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.tiendahotwheels.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    id: String,
    vm: ProductViewModel,
    onBack: () -> Unit,
    onGoCart: () -> Unit
) {
    val producto = vm.catalogo.collectAsState().value.find { it.id == id }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val hotRed = Color(0xFFFF1E00)
    val darkRed = Color(0xFF8B0000)
    val white = Color(0xFFFFFFFF)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        producto?.nombre ?: "Detalle del Producto",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = hotRed
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        producto?.let { p ->
            val imagenesAdicionales = listOf(
                p.imagen,
                "https://i.imgur.com/Bc5tPq1.jpeg",
                "https://i.imgur.com/ujFz2bO.jpeg",
                "https://i.imgur.com/J3D1Z5O.jpeg"
            )

            val opiniones = listOf(
                "Luis" to "Excelente modelo, muy detallado ",
                "Camila" to "Buena calidad, llegó rápido.",
                "Andrés" to "Perfecto para mi colección Hot Wheels "
            )

            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(listOf(hotRed, darkRed))
                    )
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(6.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(p.imagen),
                                contentDescription = p.nombre,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    item {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(imagenesAdicionales) { img ->
                                Image(
                                    painter = rememberAsyncImagePainter(img),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(90.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                )
                            }
                        }
                    }

                    item {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = p.nombre,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = white,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "$${p.precio}",
                                fontSize = 24.sp,
                                color = Color.Yellow,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            repeat(5) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "Valoración",
                                    tint = Color.Yellow
                                )
                            }
                            Spacer(Modifier.width(8.dp))
                            Text("4.9 / 5 (123 opiniones)", color = white)
                        }
                    }

                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = white),
                            elevation = CardDefaults.cardElevation(4.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = p.descripcion,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Justify,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = white),
                            elevation = CardDefaults.cardElevation(4.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(
                                    "Opiniones de clientes",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Spacer(Modifier.height(8.dp))
                                opiniones.forEach { (nombre, comentario) ->
                                    Text("⭐ $nombre: $comentario")
                                }
                            }
                        }
                    }

                    item {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth(0.85f)
                        ) {
                            Button(
                                onClick = {
                                    vm.agregarAlCarrito(p)
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Producto agregado al carrito 🛒")
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = white)
                            ) {
                                Text("Agregar", color = hotRed, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = onGoCart,
                                modifier = Modifier.weight(1f),
                                border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(Color.White, Color.Yellow)
                                    )
                                )
                            ) {
                                Text("Ver carrito", color = Color.White)
                            }
                        }
                    }
                }
            }
        } ?: run {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Producto no encontrado",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
        }
    }
}
