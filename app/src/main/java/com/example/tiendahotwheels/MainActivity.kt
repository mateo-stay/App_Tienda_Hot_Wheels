package com.example.tiendahotwheels

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tiendahotwheels.ui.theme.TiendaHotWheelsTheme
import com.example.tiendahotwheels.viewmodel.AuthViewModel
import com.example.tiendahotwheels.viewmodel.ProductViewModel
import com.example.tiendahotwheels.views.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authVM = AuthViewModel()
        val productoVM = ProductViewModel(this)

        setContent {
            TiendaHotWheelsTheme {
                Surface {
                    AppTiendaHotWheels(authVM, productoVM)
                }
            }
        }
    }
}

@Composable
fun AppTiendaHotWheels(authVM: AuthViewModel, productoVM: ProductViewModel) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = "inicio_sesion") {

        // 🟢 Inicio de sesión
        composable("inicio_sesion") {
            InicioSesion(
                vm = authVM,
                onLoginOk = { email ->
                    if (email == "admin@tienda.cl") {
                        // ✅ Ruta corregida
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

        // 🟣 Registro de usuario
        composable("registro_usuario") {
            Registro(
                vm = authVM,
                onRegistered = { nav.navigate("inicio_sesion") }
            )
        }

        // 🟡 Pantalla principal
        composable("inicio") {
            Inicio(
                vm = productoVM,
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
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStack ->
            val id = backStack.arguments?.getString("id") ?: return@composable
            DetalleProducto(
                id = id,
                vm = productoVM,
                onVolver = { nav.popBackStack() },
                onVerCarrito = { nav.navigate("carrito_compras") }
            )
        }

        // 🛒 Carrito de compras
        composable("carrito_compras") {
            Carrito(
                vm = productoVM,
                onFinalizarCompra = { exitoso ->
                    val pedido = productoVM.checkout(!exitoso)
                    if (pedido != null) {
                        nav.navigate("compra_exitosa/${pedido.id}/${pedido.total}")
                    } else {
                        nav.navigate("compra_fallida")
                    }
                },
                onVolver = { nav.popBackStack() }
            )
        }

        // 🟢 Compra exitosa
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

        // 🔴 Compra fallida
        composable("compra_fallida") {
            CompraFallida(
                onIntentarDeNuevo = { nav.popBackStack() },
                onVolverCarrito = { nav.navigate("carrito_compras") }
            )
        }

        // ⚙️ Panel de administración (ruta corregida)
        composable("panel_administracion") {
            PanelAdministracion(
                navController = nav,
                productoVM = productoVM
            )
        }

        // 🧱 Agregar producto
        composable("agregar_producto") {
            AgregarProducto(navController = nav)
        }
    }
}

