package com.example.tiendahotwheels.integration

import com.example.tiendahotwheels.data.ProductRepository
import com.example.tiendahotwheels.model.Producto
import com.example.tiendahotwheels.viewmodel.ProductViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ProductIntegrationTest {

    private lateinit var viewModel: ProductViewModel
    private lateinit var repository: ProductRepository
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        // 🔥 Mock del repository REAL
        repository = mock()

        viewModel = ProductViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun viewmodel_obtiene_productos_desde_repositorio() = runTest {

        // GIVEN
        val productosFake = listOf(
            Producto(
                id = "1",
                nombre = "Nissan Skyline",
                descripcion = "Auto deportivo",
                precio = 5000.0,
                categoria = "JDM",
                stock = 10
            ),
            Producto(
                id = "2",
                nombre = "Toyota Supra",
                descripcion = "Clásico japonés",
                precio = 4500.0,
                categoria = "JDM",
                stock = 5
            )
        )

        whenever(repository.cargarProductos()).thenReturn(productosFake)

        // WHEN
        viewModel.cargarProductos()
        advanceUntilIdle()

        // THEN
        val resultado = viewModel.productos.first()

        assertEquals(2, resultado.size)
        assertEquals("Nissan Skyline", resultado[0].nombre)
        assertEquals("Toyota Supra", resultado[1].nombre)
    }
}
