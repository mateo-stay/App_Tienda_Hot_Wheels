package com.example.tiendahotwheels.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiendahotwheels.viewmodel.AuthViewModel

// ==========================================================
// 🔹 TODAS LAS REGIONES + COMUNAS DE CHILE
// ==========================================================
val regionesConComunas = mapOf(
    "Arica y Parinacota" to listOf("Arica", "Camarones", "Putre", "General Lagos"),
    "Tarapacá" to listOf("Iquique", "Alto Hospicio", "Pozo Almonte", "Camiña", "Colchane", "Huara", "Pica"),
    "Antofagasta" to listOf(
        "Antofagasta", "Mejillones", "Sierra Gorda", "Taltal",
        "Calama", "Ollagüe", "San Pedro de Atacama",
        "Tocopilla", "María Elena"
    ),
    "Atacama" to listOf(
        "Copiapó", "Caldera", "Tierra Amarilla",
        "Chañaral", "Diego de Almagro",
        "Vallenar", "Alto del Carmen", "Freirina", "Huasco"
    ),
    "Coquimbo" to listOf(
        "La Serena", "Coquimbo", "Andacollo", "La Higuera",
        "Paiguano", "Vicuña",
        "Illapel", "Canela", "Los Vilos", "Salamanca",
        "Ovalle", "Combarbalá", "Monte Patria", "Punitaqui", "Río Hurtado"
    ),
    "Valparaíso" to listOf(
        "Valparaíso", "Viña del Mar", "Concón", "Quilpué", "Villa Alemana",
        "Limache", "Olmué", "Quintero", "Puchuncaví",
        "Casablanca", "San Antonio", "Cartagena", "El Quisco",
        "El Tabo", "Santo Domingo",
        "La Ligua", "Cabildo", "Zapallar", "Papudo", "Petorca",
        "Quillota", "La Cruz", "La Calera", "Nogales", "Hijuelas"
    ),
    "Región Metropolitana" to listOf(
        "Santiago", "Puente Alto", "Maipú", "Las Condes", "La Florida", "Ñuñoa",
        "San Miguel", "Providencia", "Estación Central", "Pudahuel", "Quilicura"
    ),
    "O'Higgins" to listOf(
        "Rancagua", "Machalí", "Graneros", "Mostazal",
        "San Vicente", "Rengo", "Requínoa",
        "Pichilemu", "Marchihue", "Navidad"
    ),
    "Maule" to listOf(
        "Talca", "Curicó", "Linares", "Constitución",
        "Parral", "San Javier"
    ),
    "Ñuble" to listOf(
        "Chillán", "Chillán Viejo", "San Carlos", "Quillón", "Yungay"
    ),
    "Biobío" to listOf(
        "Concepción", "Talcahuano", "Hualpén", "San Pedro de la Paz",
        "Coronel", "Lota", "Chiguayante"
    ),
    "La Araucanía" to listOf(
        "Temuco", "Villarrica", "Pucón", "Padre Las Casas"
    ),
    "Los Ríos" to listOf(
        "Valdivia", "Paillaco", "Panguipulli"
    ),
    "Los Lagos" to listOf(
        "Puerto Montt", "Puerto Varas", "Osorno", "Castro"
    ),
    "Aysén" to listOf(
        "Coyhaique", "Aysén", "Chile Chico"
    ),
    "Magallanes" to listOf(
        "Punta Arenas", "Puerto Natales", "Porvenir"
    )
)


// ==========================================================
// 🔹 UI DEL REGISTRO
// ==========================================================
@Composable
fun Registro(
    vm: AuthViewModel,
    onRegistered: () -> Unit
) {
    var nombre by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var direccion by remember { mutableStateOf(TextFieldValue("")) }
    var rut by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }

    var regionSeleccionada by remember { mutableStateOf("") }
    var comunaSeleccionada by remember { mutableStateOf("") }

    var mostrarRegiones by remember { mutableStateOf(false) }
    var mostrarComunas by remember { mutableStateOf(false) }

    var mensaje by remember { mutableStateOf<String?>(null) }

    val cargando by vm.cargando.collectAsState()

    val rojoHot = Color(0xFFFF1E00)
    val rojoOscuro = Color(0xFFD90000)
    val blanco = Color.White
    val fondoGradiente = Brush.verticalGradient(
        colors = listOf(rojoHot, rojoOscuro)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fondoGradiente),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = blanco)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Registro de Usuario",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = rojoHot,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(24.dp))

                // -----------------------------
                // CAMPOS DE TEXTO
                // -----------------------------
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre completo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                // -----------------------------
                // REGIÓN
                // -----------------------------
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = regionSeleccionada,
                        onValueChange = {},
                        label = { Text("Región") },
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { mostrarRegiones = true }
                    )
                }

                if (mostrarRegiones) {
                    DropdownMenu(
                        expanded = true,
                        onDismissRequest = { mostrarRegiones = false }
                    ) {
                        regionesConComunas.keys.forEach { region ->
                            DropdownMenuItem(
                                text = { Text(region) },
                                onClick = {
                                    regionSeleccionada = region
                                    comunaSeleccionada = ""
                                    mostrarRegiones = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                // -----------------------------
                // COMUNA
                // -----------------------------
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = comunaSeleccionada,
                        onValueChange = {},
                        label = { Text("Comuna") },
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (regionSeleccionada.isNotBlank()) {
                                    mostrarComunas = true
                                }
                            }
                    )
                }

                if (mostrarComunas) {
                    val comunas = regionesConComunas[regionSeleccionada] ?: emptyList()
                    DropdownMenu(
                        expanded = true,
                        onDismissRequest = { mostrarComunas = false }
                    ) {
                        comunas.forEach { comuna ->
                            DropdownMenuItem(
                                text = { Text(comuna) },
                                onClick = {
                                    comunaSeleccionada = comuna
                                    mostrarComunas = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección (calle y número)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = rut,
                    onValueChange = { rut = it },
                    label = { Text("RUT (11.111.111-K)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(24.dp))

                // -----------------------------
                // BOTÓN REGISTRAR
                // -----------------------------
                Button(
                    onClick = {
                        mensaje = null

                        val direccionCompleta =
                            "${direccion.text}, $comunaSeleccionada, $regionSeleccionada"

                        vm.registrar(
                            nombre = nombre.text,
                            email = email.text,
                            direccion = direccionCompleta,
                            rut = rut.text,
                            password = password.text
                        ) { errorMsg ->
                            if (errorMsg == null) {
                                mensaje = "Registro exitoso"
                                onRegistered()
                            } else {
                                mensaje = errorMsg
                            }
                        }
                    },
                    enabled = !cargando,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = rojoHot
                    )
                ) {
                    if (cargando) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(22.dp)
                        )
                    } else {
                        Text(
                            "Registrar",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color.White
                        )
                    }
                }

                mensaje?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        it,
                        color = if (it.contains("exitoso", ignoreCase = true))
                            Color(0xFF2E7D32)
                        else MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
