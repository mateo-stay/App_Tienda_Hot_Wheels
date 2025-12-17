package com.example.tiendahotwheels.viewmodel

import com.example.tiendahotwheels.data.ProductRepository
import com.example.tiendahotwheels.model.Producto
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
class ProductViewModelTest {

    private lateinit var repository: ProductRepository
    private lateinit var viewModel: ProductViewModel
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repository = mock()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun cargarProductos_actualiza_stateflow_correctamente() = runTest {
        // GIVEN
        val productosFake = listOf(
            Producto(
                id = "1",
                nombre = "Nissan Skyline",
                descripcion = "Auto deportivo a escala Hot Wheels",
                precio = 5000.0,
                categoria = "Deportivos",
                imagenUrl = "",
                stock = 10
            ),
            Producto(
                id = "2",
                nombre = "Toyota Supra",
                descripcion = "Modelo japonés clásico",
                precio = 4500.0,
                categoria = "Deportivos",
                imagenUrl = "",
                stock = 5
            )
        )

        whenever(repository.cargarProductos()).thenReturn(productosFake)

        viewModel = ProductViewModel(repository)

        // WHEN
        viewModel.cargarProductos()
        advanceUntilIdle()

        // THEN
        val resultado = viewModel.productos.first()

        assertEquals(2, resultado.size)
        assertEquals("Nissan Skyline", resultado[0].nombre)
        assertEquals(5000.0, resultado[0].precio, 0.0)
    }
}
