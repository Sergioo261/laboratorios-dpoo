package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

class ComboTest {

    private ProductoMenu producto1;
    private ProductoMenu producto2;

    @BeforeEach
    void setUp() {
        producto1 = new ProductoMenu("Hamburguesa", 10000);
        producto2 = new ProductoMenu("Papas", 5000);
    }

    @Test
    void testGetNombre() {
        ArrayList<ProductoMenu> items = new ArrayList<>();
        items.add(producto1);

        Combo combo = new Combo("Combo 1", 0.2, items);
        assertEquals("Combo 1", combo.getNombre());
    }

    @Test
    void testGetPrecioConDescuento() {
        ArrayList<ProductoMenu> items = new ArrayList<>();
        items.add(producto1); // 10000
        items.add(producto2); // 5000

        Combo combo = new Combo("Combo Descuento", 0.2, items); // 15000 * 0.8 = 12000
        assertEquals(12000, combo.getPrecio());
    }

    @Test
    void testPrecioSinDescuento() {
        ArrayList<ProductoMenu> items = new ArrayList<>();
        items.add(producto1);
        items.add(producto2);

        Combo combo = new Combo("Combo Sin Descuento", 0.0, items); // 15000 * 1 = 15000
        assertEquals(15000, combo.getPrecio());
    }

    @Test
    void testPrecioConDescuentoTotal() {
        ArrayList<ProductoMenu> items = new ArrayList<>();
        items.add(producto1);
        items.add(producto2);

        Combo combo = new Combo("Combo Gratis", 1.0, items); // 15000 * 0 = 0
        assertEquals(0, combo.getPrecio());
    }

    @Test
    void testComboConListaVacia() {
        ArrayList<ProductoMenu> items = new ArrayList<>();

        Combo combo = new Combo("Combo Vacio", 0.5, items);
        assertEquals(0, combo.getPrecio());
    }

    @Test
    void testEncapsulamientoLista() {
        ArrayList<ProductoMenu> items = new ArrayList<>();
        items.add(producto1);

        Combo combo = new Combo("Combo Seguro", 0.1, items);
        items.add(producto2); // Cambio externo

        // combo no debería verse afectado
        assertEquals(9000, combo.getPrecio()); // Solo incluye producto1 (10000 * 0.9)
    }

    @Test
    void testTextoFactura() {
        ArrayList<ProductoMenu> items = new ArrayList<>();
        items.add(producto1);
        Combo combo = new Combo("Combo Texto", 0.1, items); // 10000 * 0.9 = 9000

        String expected = "Combo Combo Texto\n Descuento: 0.1\n            9000\n";
        assertEquals(expected, combo.generarTextoFactura());
    }

    @Test
    void testDescuentoInvalidoMenorQueCero() {
        ArrayList<ProductoMenu> items = new ArrayList<>();
        items.add(producto1);

        assertThrows(IllegalArgumentException.class, () -> {
            new Combo("Combo Invalido", -0.1, items);
        });
    }

    @Test
    void testDescuentoInvalidoMayorQueUno() {
        ArrayList<ProductoMenu> items = new ArrayList<>();
        items.add(producto1);

        assertThrows(IllegalArgumentException.class, () -> {
            new Combo("Combo Invalido", 1.1, items);
        });
    }
}