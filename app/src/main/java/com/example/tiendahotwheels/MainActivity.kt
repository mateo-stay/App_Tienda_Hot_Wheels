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
import androidx.navigation.navArgument // 👈 IMPORTANTE: este es el que faltaba
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

        // 🔹 Pantalla de Login
        composable("login") {
            LoginScreen(
                vm = authVM,
                onLoginOk = { nav.navigate("home") },
                onGoRegister = { nav.navigate("register") }
            )
        }

        // 🔹 Pantalla de Registro
        composable("register") {
            RegisterScreen(
                vm = authVM,
                onRegistered = { nav.navigate("login") }
            )
        }

        // 🔹 Catálogo principal
        composable("home") {
            HomeScreen(
                vm = productVM,
                onSelectProduct = { id -> nav.navigate("detail/$id") }
            )
        }

        // 🔹 Detalle del producto
        composable(
            "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStack ->
            val id = backStack.arguments?.getString("id") ?: return@composable
            ProductDetailScreen(
                id = id,
                vm = productVM,
                onBack = { nav.popBackStack() }
            )
        }

        // 🔹 Carrito de compras
        composable("cart") {
            CartScreen(
                vm = productVM,
                onCheckout = { success ->
                    val pedido = productVM.checkout(!success)
                    if (pedido != null)
                        nav.navigate("success/${pedido.id}/${pedido.total}")
                    else
                        nav.navigate("failed")
                },
                onBack = { nav.popBackStack() }
            )
        }

        // 🔹 Compra Exitosa
        composable(
            "success/{id}/{total}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("total") { type = NavType.IntType }
            )
        ) { backStack ->
            val id = backStack.arguments?.getString("id") ?: ""
            val total = backStack.arguments?.getInt("total") ?: 0
            PurchaseSuccessScreen(
                pedidoId = id,
                total = total,
                onContinue = { nav.navigate("home") }
            )
        }

        // 🔹 Compra Fallida
        composable("failed") {
            PurchaseFailedScreen(
                onRetry = { nav.popBackStack() },
                onBackToCart = { nav.navigate("cart") }
            )
        }
    }
}
