package uniandes.dpoo.hamburguesas.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoMenuTest {

    @Test
    public void testNombreYPrecio() {
        ProductoMenu producto = new ProductoMenu("Hamburguesa", 15000);
        assertEquals("Hamburguesa", producto.getNombre());
        assertEquals(15000, producto.getPrecio());
    }

    @Test
    public void testTextoFactura() {
        ProductoMenu producto = new ProductoMenu("Hamburguesa", 15000);
        String esperado = "Hamburguesa\n            15000\n";
        assertEquals(esperado, producto.generarTextoFactura());
    }

    @Test
    public void testPrecioCero() {
        ProductoMenu producto = new ProductoMenu("Agua", 0);
        assertEquals(0, producto.getPrecio());
        assertTrue(producto.generarTextoFactura().contains("0"));
    }

    @Test
    public void testNombreVacio() {
        ProductoMenu producto = new ProductoMenu("", 1000);
        assertEquals("", producto.getNombre());
        assertTrue(producto.generarTextoFactura().startsWith("\n"));
    }

    @Test
    public void testPrecioNegativo() {
        ProductoMenu producto = new ProductoMenu("Bug", -100);
        assertEquals(-100, producto.getPrecio());
        assertTrue(producto.generarTextoFactura().contains("-100"));
    }
}
