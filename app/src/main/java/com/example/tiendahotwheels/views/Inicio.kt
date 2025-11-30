package com.example.tiendahotwheels.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.tiendahotwheels.viewmodel.ProductViewModel
import com.example.tiendahotwheels.viewmodel.AuthViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Inicio(
    vm: ProductViewModel,
    authVM: AuthViewModel,
    onSelectProduct: (String) -> Unit,
    onLogout: () -> Unit
) {
    val productos by vm.productos.collectAsState()
    val usuario = authVM.usuarioActual.collectAsState().value

    val rojoHot = Color(0xFFFF1E00)
    val rojoOscuro = Color(0xFFD90000)
    val blanco = Color.White

    val formatoPesos = remember {
        NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CL")).apply {
            maximumFractionDigits = 0
        }
    }

    var categoriaSeleccionada by remember { mutableStateOf("Todos") }

    val productosFiltrados = remember(productos, categoriaSeleccionada) {
        if (categoriaSeleccionada == "Todos") productos
        else productos.filter { it.categoria.equals(categoriaSeleccionada, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Tienda Hot Wheels",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        usuario?.let {
                            Text(
                                "Hola, ${it.nombre}",
                                fontSize = 18.sp,
                                color = Color.White.copy(alpha = 0.9f),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = {
                        authVM.logout()
                        onLogout()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Cerrar sesión",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = rojoHot)
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(rojoHot, rojoOscuro)))
                .padding(padding)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {

                val categorias = listOf("Todos", "JDM", "Muscle", "Europeo", "Eléctrico")

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    categorias.forEach { cat ->
                        FilterChip(
                            selected = categoriaSeleccionada == cat,
                            onClick = { categoriaSeleccionada = cat },
                            label = {
                                Text(
                                    text = cat,
                                    fontWeight = if (categoriaSeleccionada == cat)
                                        FontWeight.Bold else FontWeight.Normal,
                                    color = if (categoriaSeleccionada == cat)
                                        rojoHot else Color.White
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color.White,
                                selectedLabelColor = rojoHot,
                                containerColor = rojoHot.copy(alpha = 0.2f),
                                labelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                if (productosFiltrados.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No hay productos disponibles",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(productosFiltrados) { p ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelectProduct(p.id) },
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                                colors = CardDefaults.cardColors(containerColor = blanco)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(p.imagen),
                                        contentDescription = p.nombre,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(100.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                    )

                                    Spacer(Modifier.width(12.dp))

                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = p.nombre,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black
                                            )
                                        )

                                        Text(
                                            text = formatoPesos.format(p.precio),
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                color = rojoHot,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )

                                        Text(
                                            text = "Categoría: ${p.categoria}",
                                            color = Color.Gray,
                                            fontSize = 13.sp
                                        )

                                        Text(
                                            text = if (p.stock > 0)
                                                "Stock: ${p.stock}"
                                            else "Sin stock",
                                            color = if (p.stock > 0)
                                                Color(0xFF2E7D32)
                                            else Color.Red,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )

                                        Text(
                                            text = p.descripcion,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = Color.DarkGray
                                            ),
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        Spacer(Modifier.height(4.dp))

                                        TextButton(
                                            onClick = { onSelectProduct(p.id) },
                                            modifier = Modifier.align(Alignment.End),
                                            colors = ButtonDefaults.textButtonColors(
                                                contentColor = rojoHot
                                            )
                                        ) {
                                            Text(
                                                "Ver detalles →",
                                                fontWeight = FontWeight.Bold
                                            )
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
}
