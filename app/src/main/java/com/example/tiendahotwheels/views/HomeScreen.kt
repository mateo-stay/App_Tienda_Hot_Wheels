package com.example.tiendahotwheels.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.tiendahotwheels.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(vm: ProductViewModel, onSelectProduct: (String) -> Unit) {
    val productos = vm.catalogo.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Hot Wheels Store 🚗") })
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize().padding(12.dp)
        ) {
            items(productos.value) { p ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { onSelectProduct(p.id) },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(modifier = Modifier.padding(12.dp)) {
                        Image(
                            painter = rememberAsyncImagePainter(p.imagen),
                            contentDescription = p.nombre,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .padding(end = 12.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(p.nombre, style = MaterialTheme.typography.titleMedium)
                            Text("$${p.precio}", style = MaterialTheme.typography.bodyLarge)
                            Text(p.descripcion, maxLines = 2)
                        }
                    }
                }
            }
        }
    }
}
