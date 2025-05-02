package uniandes.dpoo.hamburguesas.tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import uniandes.dpoo.hamburguesas.mundo.*;

public class ProductoAjustadoTest {

    private ProductoMenu productoBase;
    private Ingrediente queso;
    private Ingrediente tocineta;

    @Before
    public void setUp() {
        productoBase = new ProductoMenu("Hamburguesa sencilla", 10000);
        queso = new Ingrediente("Queso", 2000);
        tocineta = new Ingrediente("Tocineta", 3000);
    }

    @Test
    public void testSinAjustes() {
        ProductoAjustado producto = new ProductoAjustado(productoBase);
        assertEquals("Hamburguesa sencilla", producto.getNombre());
        assertEquals(10000, producto.getPrecio());
        String factura = producto.generarTextoFactura();
        assertTrue(factura.contains("Hamburguesa sencilla"));
        assertFalse(factura.contains("+"));
        assertFalse(factura.contains("-"));
    }

    @Test
    public void testConAgregado() {
        ProductoAjustado producto = new ProductoAjustado(productoBase);
        producto.agregarIngrediente(queso);
        assertEquals(12000, producto.getPrecio());
        String factura = producto.generarTextoFactura();
        assertTrue(factura.contains("+Queso"));
        assertTrue(factura.contains("2000"));
    }

    @Test
    public void testConEliminado() {
        ProductoAjustado producto = new ProductoAjustado(productoBase);
        producto.eliminarIngrediente(tocineta);
        assertEquals(10000, producto.getPrecio()); // No cambia el precio
        String factura = producto.generarTextoFactura();
        assertTrue(factura.contains("-Tocineta"));
    }

    @Test
    public void testConMultiplesAgregados() {
        ProductoAjustado producto = new ProductoAjustado(productoBase);
        producto.agregarIngrediente(queso);
        producto.agregarIngrediente(tocineta);
        assertEquals(15000, producto.getPrecio());
        String factura = producto.generarTextoFactura();
        assertTrue(factura.contains("+Queso"));
        assertTrue(factura.contains("+Tocineta"));
    }

    @Test
    public void testIngredienteDuplicado() {
        ProductoAjustado producto = new ProductoAjustado(productoBase);
        producto.agregarIngrediente(queso);
        producto.agregarIngrediente(queso);
        assertEquals(14000, producto.getPrecio());
        String factura = producto.generarTextoFactura();
        int ocurrencias = factura.split("\\+Queso").length - 1;
        assertEquals(2, ocurrencias);
    }

    @Test
    public void testAgregarYEliminarMismoIngrediente() {
        ProductoAjustado producto = new ProductoAjustado(productoBase);
        producto.agregarIngrediente(queso);
        producto.eliminarIngrediente(queso);
        assertEquals(12000, producto.getPrecio());
        String factura = producto.generarTextoFactura();
        assertTrue(factura.contains("+Queso"));
        assertTrue(factura.contains("-Queso"));
    }

    @Test
    public void testFacturaCompleta() {
        ProductoAjustado producto = new ProductoAjustado(productoBase);
        producto.agregarIngrediente(queso);
        producto.agregarIngrediente(tocineta);
        producto.eliminarIngrediente(queso);
        String factura = producto.generarTextoFactura();
        assertTrue(factura.contains("Hamburguesa sencilla"));
        assertTrue(factura.contains("+Queso"));
        assertTrue(factura.contains("+Tocineta"));
        assertTrue(factura.contains("-Queso"));
        assertTrue(factura.contains("15000"));
    }
}
