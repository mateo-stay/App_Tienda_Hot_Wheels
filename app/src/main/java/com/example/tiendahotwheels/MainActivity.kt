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
        val productVM = ProductViewModel(this)

        setContent {
            TiendaHotWheelsTheme {
                Surface {
                    TiendaApp(authVM, productVM)
                }
            }
        }
    }
}

@Composable
fun TiendaApp(authVM: AuthViewModel, productVM: ProductViewModel) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = "login") {

        // 🔐 Pantalla de Inicio de Sesión
        composable("login") {
            LoginScreen(
                vm = authVM,
                onLoginOk = { email ->
                    if (email == "admin@tienda.cl") {
                        // Si el usuario es administrador
                        nav.navigate("backoffice") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        // Si es un usuario normal
                        nav.navigate("inicio") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                },
                onGoRegister = { nav.navigate("registro") }
            )
        }

        // 🧾 Pantalla de Registro
        composable("registro") {
            RegisterScreen(
                vm = authVM,
                onRegistered = { nav.navigate("login") }
            )
        }

        // 🏠 Pantalla de Inicio (Catálogo)
        composable("inicio") {
            HomeScreen(
                vm = productVM,
                authVM = authVM,
                onSelectProduct = { id -> nav.navigate("detalle/$id") },
                onLogout = {
                    authVM.logout()
                    nav.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // 🔍 Detalle del Producto
        composable(
            route = "detalle/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStack ->
            val id = backStack.arguments?.getString("id") ?: return@composable
            ProductDetailScreen(
                id = id,
                vm = productVM,
                onBack = { nav.popBackStack() },
                onGoCart = { nav.navigate("carrito") }
            )
        }

        // 🛒 Carrito de Compras
        composable("carrito") {
            CartScreen(
                vm = productVM,
                onCheckout = { exitoso ->
                    val pedido = productVM.checkout(!exitoso)
                    if (pedido != null) {
                        nav.navigate("compra_exitosa/${pedido.id}/${pedido.total}")
                    } else {
                        nav.navigate("compra_rechazada")
                    }
                },
                onBack = { nav.popBackStack() }
            )
        }

        // ✅ Compra Exitosa
        composable(
            route = "compra_exitosa/{id}/{total}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("total") { type = NavType.StringType }
            )
        ) { backStack ->
            val id = backStack.arguments?.getString("id") ?: ""
            val total = backStack.arguments?.getString("total")?.toDoubleOrNull() ?: 0.0
            PurchaseSuccessScreen(
                pedidoId = id,
                total = total,
                onContinue = { nav.navigate("inicio") },
                onBackHome = { nav.navigate("inicio") }
            )
        }

        // ❌ Compra Rechazada
        composable("compra_rechazada") {
            PurchaseFailedScreen(
                onRetry = { nav.popBackStack() },
                onBackToCart = { nav.navigate("carrito") }
            )
        }

        // ⚙️ Panel de Administración (BackOffice)
        composable("backoffice") {
            BackOfficeScreen(
                navController = nav,
                productViewModel = productVM
            )
        }

        // ➕ Agregar Producto (solo visual)
        composable("agregar_producto") {
            AddProductScreen(nav)
        }
    }
}
