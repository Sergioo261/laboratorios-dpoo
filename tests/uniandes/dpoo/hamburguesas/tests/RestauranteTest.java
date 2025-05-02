package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.ArrayList;

import org.junit.jupiter.api.*;

import uniandes.dpoo.hamburguesas.mundo.*;
import uniandes.dpoo.hamburguesas.excepciones.*;

public class RestauranteTest {

    private Restaurante restaurante;

    @BeforeEach
    public void setUp() {
        restaurante = new Restaurante();
    }

    @Test
    public void testIniciarPedidoCorrectamente() throws YaHayUnPedidoEnCursoException {
        restaurante.iniciarPedido("Carlos", "Calle 123");
        Pedido pedido = restaurante.getPedidoEnCurso();
        assertNotNull(pedido);
        assertEquals("Carlos", pedido.getNombreCliente());
    }

    @Test
    public void testIniciarPedidoCuandoYaHayUnoEnCurso() throws YaHayUnPedidoEnCursoException {
        restaurante.iniciarPedido("Carlos", "Calle 123");
        assertThrows(YaHayUnPedidoEnCursoException.class, () -> {
            restaurante.iniciarPedido("Pedro", "Otra calle");
        });
    }

    @Test
    public void testCerrarPedidoSinPedidoEnCurso() {
        assertThrows(NoHayPedidoEnCursoException.class, () -> {
            restaurante.cerrarYGuardarPedido();
        });
    }

    @Test
    public void testCerrarYGuardarPedidoCorrectamente() throws Exception {
        restaurante.iniciarPedido("Ana", "Calle 45");
        File facturaDir = new File("./facturas");
        facturaDir.mkdirs(); // Asegura que exista la carpeta
        restaurante.cerrarYGuardarPedido();

        ArrayList<Pedido> pedidos = restaurante.getPedidos();
        assertNull(restaurante.getPedidoEnCurso());

        // No hay forma directa de verificar archivo salvo listar archivos creados
        File[] archivos = facturaDir.listFiles((dir, name) -> name.startsWith("factura_") && name.endsWith(".txt"));
        assertTrue(archivos.length > 0);
    }

    @Test
    public void testCargarIngredientesYDetectarRepetidos() throws IOException {
        File tempFile = crearArchivoTemporal("ingredientes.txt", "lechuga;500\nlechuga;500\n");
        assertThrows(IngredienteRepetidoException.class, () -> {
            restaurante.cargarInformacionRestaurante(tempFile, crearArchivoTemporal("menu.txt", ""), crearArchivoTemporal("combos.txt", ""));
        });
    }

    @Test
    public void testCargarMenuYDetectarRepetidos() throws IOException {
        File tempFile = crearArchivoTemporal("menu.txt", "hamburguesa;15000\nhamburguesa;15000\n");
        assertThrows(ProductoRepetidoException.class, () -> {
            restaurante.cargarInformacionRestaurante(crearArchivoTemporal("ingredientes.txt", ""), tempFile, crearArchivoTemporal("combos.txt", ""));
        });
    }

    @Test
    public void testCargarCombosConProductoInexistente() throws IOException {
        File ingredientes = crearArchivoTemporal("ingredientes.txt", "");
        File menu = crearArchivoTemporal("menu.txt", "papas;3000\n");
        File combos = crearArchivoTemporal("combos.txt", "Combo1;10%;hamburguesa\n");
        assertThrows(ProductoFaltanteException.class, () -> {
            restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);
        });
    }

    @Test
    public void testCargaCorrectaDeTodo() throws Exception {
        File ingredientes = crearArchivoTemporal("ingredientes.txt", "lechuga;500\nsalsa;200\n");
        File menu = crearArchivoTemporal("menu.txt", "hamburguesa;12000\npapas;3000\n");
        File combos = crearArchivoTemporal("combos.txt", "Combo1;20%;hamburguesa;papas\n");

        restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);

        assertEquals(2, restaurante.getIngredientes().size());
        assertEquals(2, restaurante.getMenuBase().size());
        assertEquals(1, restaurante.getMenuCombos().size());
    }

    // Utilidades para test

    private File crearArchivoTemporal(String nombre, String contenido) throws IOException {
        File archivo = new File("./" + nombre);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            writer.write(contenido);
        }
        archivo.deleteOnExit();
        return archivo;
    }
}
