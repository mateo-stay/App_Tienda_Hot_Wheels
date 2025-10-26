package com.example.tiendahotwheels.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import java.text.NumberFormat
import java.util.Locale

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
    val darkRed = Color(0xFFFF1E00)
    val white = Color.White

    val formatoPesos = remember {
        NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CL")).apply {
            maximumFractionDigits = 0
        }
    }

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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = hotRed)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        producto?.let { p ->

            val opiniones = listOf(
                "Luis" to "Excelente modelo, muy detallado.",
                "Camila" to "Buena calidad, llegó rápido.",
                "Andrés" to "Perfecto para mi colección Hot Wheels."
            )

            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(Brush.verticalGradient(listOf(hotRed, darkRed)))
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
                                .height(280.dp)
                                .shadow(10.dp, RoundedCornerShape(20.dp)),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = white)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(p.imagen),
                                    contentDescription = p.nombre,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                        .padding(12.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                )
                            }
                        }
                    }

                    item {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = p.nombre,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = white,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = formatoPesos.format(p.precio), // 💰 $2.500
                                fontSize = 24.sp,
                                color = Color.White,
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
                            Text("4.9 / 5", color = white)
                        }
                    }

                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(6.dp, RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(containerColor = white),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = p.descripcion,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = Color.DarkGray,
                                    lineHeight = 22.sp
                                ),
                                textAlign = TextAlign.Justify,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(6.dp, RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(containerColor = white),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(
                                    "Opiniones de clientes",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                )
                                Spacer(Modifier.height(8.dp))
                                opiniones.forEach { (nombre, comentario) ->
                                    Text(
                                        text = "• $nombre: $comentario",
                                        color = Color.DarkGray,
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth(0.9f)
                        ) {
                            Button(
                                onClick = {
                                    vm.agregarAlCarrito(p)
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Producto agregado al carrito 🛒")
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = white)
                            ) {
                                Text(
                                    "Agregar",
                                    color = hotRed,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            OutlinedButton(
                                onClick = onGoCart,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = white)
                            ) {
                                Text(
                                    "Ver carrito",
                                    color = hotRed,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    item { Spacer(Modifier.height(32.dp)) }
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
