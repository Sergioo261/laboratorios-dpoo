package uniandes.dpoo.hamburguesas.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.Producto;

public class PedidoTest {

    Pedido pedido;

    class ProductoMock implements Producto {
        private String nombre;
        private int precio;

        public ProductoMock(String nombre, int precio) {
            this.nombre = nombre;
            this.precio = precio;
        }

        @Override
        public int getPrecio() {
            return precio;
        }

        @Override
        public String getNombre() {
            return nombre;
        }

        @Override
        public String generarTextoFactura() {
            return nombre + ": " + precio + "\n";
        }
    }

    @Before
    public void setUp() {
        pedido = new Pedido("Juan Pérez", "Calle 123");
    }

    @Test
    public void testCrearPedidoSinProductos() {
        assertEquals("Juan Pérez", pedido.getNombreCliente());
        assertEquals(0, pedido.getPrecioTotalPedido());  // 0 productos
    }

    @Test
    public void testAgregarProductoYCalcularPrecio() {
        pedido.agregarProducto(new ProductoMock("Hamburguesa", 10000));
        pedido.agregarProducto(new ProductoMock("Papas", 5000));

        int netoEsperado = 15000;
        int ivaEsperado = (int)(netoEsperado * 0.19);
        int totalEsperado = netoEsperado + ivaEsperado;

        assertEquals(totalEsperado, pedido.getPrecioTotalPedido());
    }

    @Test
    public void testGenerarTextoFactura() {
        pedido.agregarProducto(new ProductoMock("Hamburguesa", 10000));
        pedido.agregarProducto(new ProductoMock("Gaseosa", 3000));

        String factura = pedido.generarTextoFactura();

        assertTrue(factura.contains("Cliente: Juan Pérez"));
        assertTrue(factura.contains("Dirección: Calle 123"));
        assertTrue(factura.contains("Hamburguesa: 10000"));
        assertTrue(factura.contains("Gaseosa: 3000"));
        assertTrue(factura.contains("Precio Neto:  13000"));
        assertTrue(factura.contains("IVA:          2470"));
        assertTrue(factura.contains("Precio Total: 15470"));
    }

    @Test
    public void testGuardarFactura() throws FileNotFoundException {
        pedido.agregarProducto(new ProductoMock("Combo", 20000));
        File archivo = new File("test_factura.txt");

        pedido.guardarFactura(archivo);

        Scanner sc = new Scanner(archivo);
        String contenido = sc.useDelimiter("\\A").next();
        sc.close();

        assertTrue(contenido.contains("Combo: 20000"));

        archivo.delete();  // limpiar después del test
    }

    @Test(expected = FileNotFoundException.class)
    public void testGuardarFacturaError() throws FileNotFoundException {
        pedido.agregarProducto(new ProductoMock("Producto", 10000));

        // Intentar guardar en carpeta inexistente
        File archivo = new File("carpeta_inexistente/factura.txt");
        pedido.guardarFactura(archivo);
    }
}