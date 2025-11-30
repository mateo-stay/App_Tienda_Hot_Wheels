package com.example.tiendahotwheels

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tiendahotwheels.data.ProductRepository
import com.example.tiendahotwheels.ui.theme.TiendaHotWheelsTheme
import com.example.tiendahotwheels.viewmodel.AuthViewModel
import com.example.tiendahotwheels.viewmodel.ProductViewModel
import com.example.tiendahotwheels.views.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authVM = AuthViewModel()
        val productRepository = ProductRepository(this)
        val productVM = ProductViewModel(productRepository)

        setContent {
            TiendaHotWheelsTheme {
                Surface {
                    AppTiendaHotWheels(authVM, productVM)
                }
            }
        }
    }
}

@Composable
fun AppTiendaHotWheels(
    authVM: AuthViewModel,
    productVM: ProductViewModel
) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = "inicio_sesion") {

        // 🟢 Inicio de sesión
        composable("inicio_sesion") {
            InicioSesion(
                vm = authVM,
                onLoginOk = { email ->
                    if (email == "admin@tienda.cl") {
                        nav.navigate("panel_administracion") {
                            popUpTo("inicio_sesion") { inclusive = true }
                        }
                    } else {
                        nav.navigate("inicio") {
                            popUpTo("inicio_sesion") { inclusive = true }
                        }
                    }
                },
                onGoRegister = { nav.navigate("registro_usuario") }
            )
        }

        // 🟣 Registro
        composable("registro_usuario") {
            Registro(
                vm = authVM,
                onRegistered = { nav.navigate("inicio_sesion") }
            )
        }

        // 🟡 Inicio (lista de productos)
        composable("inicio") {
            Inicio(
                vm = productVM,
                authVM = authVM,
                onSelectProduct = { id -> nav.navigate("detalle_producto/$id") },
                onLogout = {
                    authVM.logout()
                    nav.navigate("inicio_sesion") {
                        popUpTo("inicio_sesion") { inclusive = true }
                    }
                }
            )
        }

        // 🟠 Detalle de producto
        composable(
            route = "detalle_producto/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )
        ) { backStack ->
            val id = backStack.arguments?.getString("id") ?: return@composable
            DetalleProducto(
                id = id,
                vm = productVM,
                onVolver = { nav.popBackStack() },
                onVerCarrito = { nav.navigate("carrito_compras") }
            )
        }

        // 🛒 Carrito
        composable("carrito_compras") {
            Carrito(
                vm = productVM,
                onFinalizarCompra = { exitoso ->
                    if (exitoso) {
                        // Generamos un ID simple y usamos el total desde el ViewModel
                        val id = System.currentTimeMillis().toString()
                        val totalCompra = productVM.total()

                        nav.navigate("compra_exitosa/$id/$totalCompra")
                    } else {
                        nav.navigate("compra_fallida")
                    }
                },
                onVolver = { nav.popBackStack() }
            )
        }

        // ✅ Compra exitosa
        composable(
            route = "compra_exitosa/{id}/{total}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("total") { type = NavType.StringType }
            )
        ) { backStack ->
            val id = backStack.arguments?.getString("id") ?: ""
            val total = backStack.arguments?.getString("total")?.toDoubleOrNull() ?: 0.0

            CompraExitosa(
                idPedido = id,
                total = total,
                onSeguirComprando = { nav.navigate("inicio") },
                onIrInicio = { nav.navigate("inicio") }
            )
        }

        // ❌ Compra fallida
        composable("compra_fallida") {
            CompraFallida(
                onIntentarDeNuevo = { nav.popBackStack() },
                onVolverCarrito = { nav.navigate("carrito_compras") }
            )
        }

        // ⚙️ Panel admin
        composable("panel_administracion") {
            PanelAdministracion(
                navController = nav,
                productoVM = productVM
            )
        }

        // ➕ Agregar producto
        composable("agregar_producto") {
            AgregarProducto(navController = nav)
        }
    }
}
