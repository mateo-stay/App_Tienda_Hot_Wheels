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
import com.example.tiendahotwheels.views.InicioSesion

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

    NavHost(navController = nav, startDestination = "inicio_sesion") {

        composable("inicio_sesion") {
            InicioSesion(
                vm = authVM,
                onLoginOk = { email ->
                    if (email == "admin@tienda.cl") {
                        nav.navigate("backoffice") {
                            popUpTo("inicio_sesion") { inclusive = true }
                        }
                    } else {
                        nav.navigate("inicio") {
                            popUpTo("inicio_sesion") { inclusive = true }
                        }
                    }
                },
                onGoRegister = { nav.navigate("registro") }
            )
        }

        composable("registro") {
            RegisterScreen(
                vm = authVM,
                onRegistered = { nav.navigate("inicio_sesion") }
            )
        }

        composable("inicio") {
            HomeScreen(
                vm = productVM,
                authVM = authVM,
                onSelectProduct = { id -> nav.navigate("detalle/$id") },
                onLogout = {
                    authVM.logout()
                    nav.navigate("inicio_sesion") {
                        popUpTo("inicio_sesion") { inclusive = true }
                    }
                }
            )
        }

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

        composable("compra_rechazada") {
            PurchaseFailedScreen(
                onRetry = { nav.popBackStack() },
                onBackToCart = { nav.navigate("carrito") }
            )
        }

        composable("backoffice") {
            BackOfficeScreen(
                navController = nav,
                productViewModel = productVM
            )
        }

        composable("agregar_producto") {
            AddProductScreen(nav)
        }
    }
}
