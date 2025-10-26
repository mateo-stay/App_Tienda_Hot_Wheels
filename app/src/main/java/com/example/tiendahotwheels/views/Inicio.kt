package com.example.tiendahotwheels.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
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
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    vm: ProductViewModel,
    onSelectProduct: (String) -> Unit
) {
    val productos = vm.catalogo.collectAsState()

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
                        "Tienda Hot Wheels",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = hotRed)
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(hotRed, darkRed)))
                .padding(padding)
        ) {
            if (productos.value.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No hay productos disponibles",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(productos.value) { p ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectProduct(p.id) },
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = white)
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
                                            color = hotRed,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )

                                    Text(
                                        text = p.descripcion,
                                        style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Spacer(Modifier.height(4.dp))

                                    TextButton(
                                        onClick = { onSelectProduct(p.id) },
                                        modifier = Modifier.align(Alignment.End),
                                        colors = ButtonDefaults.textButtonColors(contentColor = hotRed)
                                    ) {
                                        Text("Ver detalles →", fontWeight = FontWeight.Bold)
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


