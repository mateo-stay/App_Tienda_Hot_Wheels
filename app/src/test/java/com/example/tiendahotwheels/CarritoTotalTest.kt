import org.junit.Assert.*
import org.junit.Test
import kotlin.collections.sumOf

class CarritoTotalTest {

    data class ItemCarrito(
        val nombre: String,
        val precio: Int,
        val cantidad: Int
    )

    private fun calcularTotal(items: List<ItemCarrito>): Int {
        return items.sumOf { it.precio * it.cantidad }
    }

    @Test
    fun carrito_con_un_producto_calcula_total_correcto() {
        val items = listOf(
            ItemCarrito("Nissan Skyline", 5000, 1)
        )

        val total = calcularTotal(items)

        assertEquals(5000, total)
    }

    @Test
    fun carrito_con_varios_productos_calcula_total_correcto() {
        val items = listOf(
            ItemCarrito("Nissan Skyline", 5000, 2),
            ItemCarrito("Toyota Supra", 4500, 1)
        )

        val total = calcularTotal(items)

        assertEquals(14500, total)
    }

    @Test
    fun carrito_vacio_retorna_total_cero() {
        val items = emptyList<ItemCarrito>()

        val total = calcularTotal(items)

        assertEquals(0, total)
    }

    @Test
    fun carrito_con_cantidad_cero_no_suma() {
        val items = listOf(
            ItemCarrito("Mazda RX-7", 6000, 0)
        )

        val total = calcularTotal(items)

        assertEquals(0, total)
    }
}
