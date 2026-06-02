package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import excepciones.DatoInvalidoException;
import excepciones.SistemaException;
import modelo.Sistema;
import persistencia.GestorJson;

class GestorJsonTest {

    @Test
    @DisplayName("Roundtrip: guardar y cargar produce un sistema equivalente")
    void roundtripGuardarCargarFunciona(@TempDir Path tempDir) throws Exception {
        Sistema original = new Sistema("Sistema UCES", LocalDate.of(2026, 6, 1));
        original.registrarProfesor(1, "Ana", "Matematica");
        original.registrarProfesor(2, "Luis", "Historia");
        original.agregarAlumnoAProfesor(1, 100, "Juan", "1 Anio");
        original.agregarAlumnoAProfesor(1, 101, "Pedro", "1 Anio");
        original.agregarAlumnoAProfesor(2, 200, "Sofia", "2 Anio");

        Path archivo = tempDir.resolve("sistema.json");
        GestorJson.guardar(original, archivo.toString());

        assertTrue(Files.exists(archivo), "El archivo deberia existir tras guardar");

        Sistema cargado = GestorJson.cargar(archivo.toString());
        assertEquals(original.getNombre(), cargado.getNombre());
        assertEquals(original.getFechaCreacion(), cargado.getFechaCreacion());
        assertEquals(original.listarProfesores().size(), cargado.listarProfesores().size());
        assertEquals(original.obtenerCantidadTotalAlumnos(), cargado.obtenerCantidadTotalAlumnos());
        assertEquals(2, cargado.obtenerProfesor(1).obtenerCantidadAlumnos());
        assertEquals(1, cargado.obtenerProfesor(2).obtenerCantidadAlumnos());
    }

    @Test
    @DisplayName("Guardar sistema vacio y cargarlo funciona")
    void guardarSistemaVacioCargarOK(@TempDir Path tempDir) throws Exception {
        Sistema original = new Sistema("Vacio", LocalDate.of(2026, 1, 1));
        Path archivo = tempDir.resolve("vacio.json");
        GestorJson.guardar(original, archivo.toString());

        Sistema cargado = GestorJson.cargar(archivo.toString());
        assertEquals("Vacio", cargado.getNombre());
        assertEquals(0, cargado.listarProfesores().size());
        assertEquals(0, cargado.obtenerCantidadTotalAlumnos());
    }

    @Test
    @DisplayName("Cargar archivo inexistente lanza IOException")
    void cargarArchivoInexistenteLanzaIOException(@TempDir Path tempDir) {
        Path archivo = tempDir.resolve("no_existe.json");
        assertThrows(IOException.class,
                () -> GestorJson.cargar(archivo.toString()));
    }

    @Test
    @DisplayName("Cargar JSON malformado lanza DatoInvalidoException")
    void cargarJsonMalformadoLanzaDatoInvalido(@TempDir Path tempDir) throws Exception {
        Path archivo = tempDir.resolve("malformado.json");
        Files.write(archivo, "{ esto no es JSON valido ::: }".getBytes(StandardCharsets.UTF_8));
        assertThrows(DatoInvalidoException.class,
                () -> GestorJson.cargar(archivo.toString()));
    }

    @Test
    @DisplayName("Cargar JSON con campos faltantes lanza DatoInvalidoException")
    void cargarJsonSinCamposObligatoriosLanzaExcepcion(@TempDir Path tempDir) throws Exception {
        Path archivo = tempDir.resolve("incompleto.json");
        Files.write(archivo, "{ \"profesores\": [] }".getBytes(StandardCharsets.UTF_8));
        assertThrows(SistemaException.class,
                () -> GestorJson.cargar(archivo.toString()));
    }
}
