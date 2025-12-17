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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Tune



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

            LaunchedEffect(p.id) {
                vm.cargarFichaTecnica(p)
            }

            // 🔧 En vez de comentarios, detalles “pro” del modelo Hot Wheels
            val detallesHotWheels = listOf(
                "Escala: 1:64",
                "Línea: Hot Wheels Mainline",
                "Material: Metal y plástico",
                "Ideal para coleccionistas y fans de autos deportivos",
                "Perfecto para exhibir en vitrinas o pistas de carreras"
            )

            val sinStock = p.stock == 0

            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(fondoGradiente)
            ) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // IMAGEN PRINCIPAL (más compacta para smartphone)


                    // EFECTO 3D FAKE (más bajo)
                    item {
                        if (p.imagenUrl.isNotBlank()) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(8.dp, RoundedCornerShape(18.dp)),
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFCC1010).copy(alpha = 0.9f)
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(15.dp),
                                ) {

                                    // Header "vista interactiva"
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {

                                    }

                                    // TiltImage
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(400.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(Color(0xFFFDFDFD)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        TiltImage(
                                            imageUrl = p.imagenUrl,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight()
                                                .padding(6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }


                    // NOMBRE + PRECIO + STOCK
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth(0.95f)
                        ) {
                            Text(
                                text = p.nombre,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = blanco,
                                textAlign = TextAlign.Center
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                text = formatoPesos.format(p.precio),
                                fontSize = 22.sp,
                                color = blanco,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                text = when {
                                    p.stock == 0 -> "Sin stock"
                                    p.stock in 1..2 -> "⚠ Solo quedan ${p.stock} unidades"
                                    else -> "Stock disponible: ${p.stock}"
                                },
                                color = when {
                                    p.stock == 0 -> Color(0xFFFFCDD2)
                                    p.stock in 1..2 -> Color.Yellow
                                    else -> Color(0xFFB9F6CA)
                                },
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // VALORACIÓN
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .clip(RoundedCornerShape(50))
                                .background(Color.Black.copy(alpha = 0.18f))
                                .padding(horizontal = 18.dp, vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {


                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    repeat(5) {
                                        Icon(
                                            imageVector = Icons.Filled.Star,
                                            contentDescription = "Valoración",
                                            tint = Color(0xFFFFD54F),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Spacer(Modifier.width(12.dp))

                                // 🔢 Nota + subtítulo
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = "4.9 / 5",
                                        color = blanco,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Valoración de coleccionistas",
                                        color = blanco.copy(alpha = 0.8f),
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }


                    // DESCRIPCIÓN + FICHA TÉCNICA REAL
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(6.dp, RoundedCornerShape(18.dp)),
                            colors = CardDefaults.cardColors(containerColor = blanco),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {

                                // 📝 HEADER DESCRIPCIÓN
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFFFFEBEE)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Info,
                                            contentDescription = "Descripción",
                                            tint = rojoHot,
                                            modifier = Modifier
                                                .padding(6.dp)
                                                .size(18.dp)
                                        )
                                    }

                                    Spacer(Modifier.width(10.dp))

                                    Column {
                                        Text(
                                            "Descripción",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black
                                            )
                                        )
                                        Text(
                                            "Detalle del modelo a escala",
                                            fontSize = 11.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }

                                Text(
                                    text = p.descripcion,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = Color.DarkGray,
                                        lineHeight = 22.sp
                                    ),
                                    textAlign = TextAlign.Justify
                                )

                                Divider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp),
                                    color = Color(0xFFE0E0E0),
                                    thickness = 1.dp
                                )

                                // ⚙ HEADER FICHA TÉCNICA
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFFE3F2FD)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Tune,
                                            contentDescription = "Ficha técnica",
                                            tint = Color(0xFF1565C0),
                                            modifier = Modifier
                                                .padding(6.dp)
                                                .size(18.dp)
                                        )
                                    }

                                    Spacer(Modifier.width(10.dp))

                                    Column {
                                        Text(
                                            "Ficha técnica (auto real)",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black
                                            )
                                        )
                                        Text(
                                            "Especificaciones del vehículo real",
                                            fontSize = 11.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }

                                when {
                                    cargandoCarInfo -> {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 4.dp),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(22.dp),
                                                color = rojoHot,
                                                strokeWidth = 3.dp
                                            )
                                            Spacer(Modifier.width(8.dp))
                                            Text(
                                                "Cargando ficha técnica...",
                                                fontSize = 12.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    }

                                    carInfo != null -> {
                                        Column(
                                            verticalArrangement = Arrangement.spacedBy(3.dp),
                                            modifier = Modifier.padding(top = 4.dp)
                                        ) {
                                            carInfo?.model_make_id?.let {
                                                Text("Marca: $it", fontSize = 14.sp, color = Color.Black)
                                            }
                                            carInfo?.model_name?.let {
                                                Text("Modelo: $it", fontSize = 14.sp, color = Color.Black)
                                            }
                                            carInfo?.model_year?.let {
                                                Text("Año: $it", fontSize = 14.sp, color = Color.Black)
                                            }
                                            carInfo?.model_engine_power_ps?.let {
                                                Text("Potencia: ${it} PS", fontSize = 14.sp, color = Color.Black)
                                            }
                                            carInfo?.model_engine_type?.let {
                                                Text("Motor: $it", fontSize = 14.sp, color = Color.Black)
                                            }
                                            carInfo?.model_engine_fuel?.let {
                                                Text("Combustible: $it", fontSize = 14.sp, color = Color.Black)
                                            }
                                            carInfo?.model_body?.let {
                                                Text("Carrocería: $it", fontSize = 14.sp, color = Color.Black)
                                            }
                                            carInfo?.model_drive?.let {
                                                Text("Tracción: $it", fontSize = 14.sp, color = Color.Black)
                                            }

                                            Text(
                                                text = "Datos obtenidos desde CarQuery API.",
                                                fontSize = 11.sp,
                                                color = Color.Gray,
                                                modifier = Modifier.padding(top = 6.dp)
                                            )
                                        }
                                    }

                                    else -> {
                                        Text(
                                            "No se pudo obtener la ficha técnica.",
                                            fontSize = 12.sp,
                                            color = Color.Gray,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }


                    // 🧾 DETALLES DEL MODELO HOT WHEELS (en vez de opiniones)
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(6.dp, RoundedCornerShape(18.dp)),
                            colors = CardDefaults.cardColors(containerColor = blanco),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // HEADER
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = Color(0xFFFFEBEE),
                                            tonalElevation = 0.dp
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.DirectionsCar,
                                                contentDescription = "Hot Wheels",
                                                tint = rojoHot,
                                                modifier = Modifier
                                                    .padding(6.dp)
                                                    .size(18.dp)
                                            )
                                        }

                                        Spacer(Modifier.width(10.dp))

                                        Column {
                                            Text(
                                                "Detalles del modelo Hot Wheels",
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.Black
                                                )
                                            )
                                            Text(
                                                "Información pensada para coleccionistas",
                                                fontSize = 11.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    }

                                    Text(
                                        text = "Edición 1:64",
                                        fontSize = 11.sp,
                                        color = Color.DarkGray
                                    )
                                }

                                Divider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp),
                                    color = Color(0xFFE0E0E0),
                                    thickness = 1.dp
                                )

                                // LISTA DE DETALLES
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    detallesHotWheels.forEach { detalle ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(6.dp)
                                                    .clip(CircleShape)
                                                    .background(color = Color.Black)
                                            )

                                            Spacer(Modifier.width(8.dp))

                                            Text(
                                                text = detalle,
                                                color = Color.DarkGray,
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }


                    // BOTONES
                    item {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth(0.95f)
                        ) {
                            Button(
                                enabled = !sinStock,
                                onClick = {
                                    val agregado = vm.agregarAlCarrito(p)

                                    if (!agregado) {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                "No puedes agregar más, stock insuficiente"
                                            )
                                        }
                                    } else {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                "Producto agregado al carrito"
                                            )
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (!sinStock) blanco else Color.Gray,
                                    disabledContainerColor = Color.DarkGray
                                )
                            ) {
                                Text(
                                    if (!sinStock) "Agregar" else "Sin stock",
                                    color = if (!sinStock) rojoHot else Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            OutlinedButton(
                                onClick = onVerCarrito,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
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

                    item { Spacer(Modifier.height(24.dp)) }
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
