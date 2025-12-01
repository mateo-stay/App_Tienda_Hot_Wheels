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
fun DetalleProducto(
    id: String,
    vm: ProductViewModel,
    onVolver: () -> Unit,
    onVerCarrito: () -> Unit
) {
    val productos by vm.productos.collectAsState()
    val producto = productos.find { it.id == id }

    // 🔥 Estados de la API externa (CarQuery)
    val carInfo by vm.carInfo.collectAsState()
    val cargandoCarInfo by vm.cargandoCarInfo.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val rojoHot = Color(0xFFFF1E00)
    val rojoOscuro = Color(0xFFD90000)
    val blanco = Color.White

    val fondoGradiente = Brush.verticalGradient(
        colors = listOf(rojoHot, rojoOscuro)
    )

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
                        producto?.nombre ?: "Detalle del producto",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = rojoHot)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        producto?.let { p ->

            // 👇 Cuando se carga el producto, disparamos la consulta a CarQuery
            LaunchedEffect(p.id) {
                vm.cargarFichaTecnica(p)
            }

            val opiniones = listOf(
                "Luis" to "Excelente modelo, muy detallado.",
                "Camila" to "Buena calidad, llegó rápido.",
                "Andrés" to "Perfecto para mi colección Hot Wheels."
            )

            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(fondoGradiente)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // IMAGEN
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                                .shadow(10.dp, RoundedCornerShape(20.dp)),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = blanco)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (p.imagenUrl.isNotBlank()) {
                                    Image(
                                        painter = rememberAsyncImagePainter(p.imagenUrl),
                                        contentDescription = p.nombre,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight()
                                    )
                                } else {
                                    Text(
                                        "Sin imagen",
                                        color = Color.Gray,
                                        fontSize = 14.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    // NOMBRE + PRECIO + STOCK
                    item {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = p.nombre,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = blanco,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = formatoPesos.format(p.precio),
                                fontSize = 24.sp,
                                color = blanco,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = if (p.stock > 0)
                                    "Stock disponible: ${p.stock}"
                                else
                                    "Sin stock",
                                color = if (p.stock > 0) Color(0xFFB9F6CA) else Color(0xFFFFCDD2),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // VALORACIÓN
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
                            Text("4.9 / 5", color = blanco)
                        }
                    }

                    // DESCRIPCIÓN + FICHA TÉCNICA
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(6.dp, RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(containerColor = blanco),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    "Descripción",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                )
                                Text(
                                    text = p.descripcion,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = Color.DarkGray,
                                        lineHeight = 22.sp
                                    ),
                                    textAlign = TextAlign.Justify
                                )

                                // 🔥 FICHA TÉCNICA AUTO REAL (CarQuery)
                                Spacer(Modifier.height(8.dp))

                                Text(
                                    "Ficha técnica (auto real)",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                )

                                when {
                                    cargandoCarInfo -> {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(28.dp),
                                                color = rojoHot,
                                                strokeWidth = 3.dp
                                            )
                                        }
                                    }

                                    carInfo != null -> {
                                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                            Text("Marca: ${carInfo?.model_make_id ?: "-"}")
                                            Text("Modelo: ${carInfo?.model_name ?: "-"}")
                                            carInfo?.model_year?.let {
                                                Text("Año: $it")
                                            }
                                            carInfo?.model_engine_power_ps?.let {
                                                Text("Potencia: ${it} PS")
                                            }
                                            carInfo?.model_engine_type?.let {
                                                Text("Motor: $it")
                                            }
                                            carInfo?.model_engine_fuel?.let {
                                                Text("Combustible: $it")
                                            }
                                            carInfo?.model_body?.let {
                                                Text("Carrocería: $it")
                                            }
                                            carInfo?.model_drive?.let {
                                                Text("Tracción: $it")
                                            }

                                            Text(
                                                text = "Datos obtenidos desde CarQuery API.",
                                                fontSize = 11.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    }

                                    else -> {
                                        Text(
                                            "No se pudo obtener la ficha técnica.",
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // OPINIONES
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(6.dp, RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(containerColor = blanco),
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

                    // BOTONES
                    item {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth(0.9f)
                        ) {
                            Button(
                                onClick = {
                                    if (p.stock <= 0) {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Sin stock disponible")
                                        }
                                    } else {
                                        vm.agregarAlCarrito(p)
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Producto agregado al carrito")
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = blanco)
                            ) {
                                Text(
                                    "Agregar",
                                    color = rojoHot,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            OutlinedButton(
                                onClick = onVerCarrito,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = blanco)
                            ) {
                                Text(
                                    "Ver carrito",
                                    color = blanco,
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
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(fondoGradiente),
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
