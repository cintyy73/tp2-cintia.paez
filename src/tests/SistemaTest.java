package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import excepciones.DatoInvalidoException;
import excepciones.LimiteProfesoresException;
import excepciones.ProfesorDuplicadoException;
import excepciones.ProfesorNoEncontradoException;
import modelo.Sistema;

class SistemaTest {

    private Sistema sistema;

    @BeforeEach
    void setUp() throws DatoInvalidoException {
        sistema = new Sistema("Sistema UCES", LocalDate.of(2026, 6, 1));
    }

    @Test
    @DisplayName("Crear sistema con datos validos")
    void crearSistemaConDatosValidos() {
        assertEquals("Sistema UCES", sistema.getNombre());
        assertEquals(LocalDate.of(2026, 6, 1), sistema.getFechaCreacion());
        assertEquals(0, sistema.listarProfesores().size());
        assertEquals(0, sistema.obtenerCantidadTotalAlumnos());
    }

    @Test
    @DisplayName("Nombre vacio lanza DatoInvalidoException")
    void crearSistemaConNombreVacioLanzaExcepcion() {
        assertThrows(DatoInvalidoException.class,
                () -> new Sistema("", LocalDate.now()));
    }

    @Test
    @DisplayName("Fecha null lanza DatoInvalidoException")
    void crearSistemaConFechaNullLanzaExcepcion() {
        assertThrows(DatoInvalidoException.class,
                () -> new Sistema("Sistema", null));
    }

    @Test
    @DisplayName("Registrar profesor lo agrega al sistema")
    void registrarProfesorFunciona() throws Exception {
        sistema.registrarProfesor(1, "Ana", "Matematica");
        assertEquals(1, sistema.listarProfesores().size());
        assertTrue(sistema.existeProfesor(1));
    }

    @Test
    @DisplayName("Registrar profesor con id duplicado lanza ProfesorDuplicadoException")
    void registrarProfesorDuplicadoLanzaExcepcion() throws Exception {
        sistema.registrarProfesor(1, "Ana", "Matematica");
        assertThrows(ProfesorDuplicadoException.class,
                () -> sistema.registrarProfesor(1, "Otro", "Historia"));
    }

    @Test
    @DisplayName("Se pueden registrar exactamente 10 profesores")
    void registrar10ProfesoresFunciona() throws Exception {
        for (int i = 1; i <= Sistema.MAX_PROFESORES; i++) {
            sistema.registrarProfesor(i, "Prof_" + i, "Materia_" + i);
        }
        assertEquals(Sistema.MAX_PROFESORES, sistema.listarProfesores().size());
    }

    @Test
    @DisplayName("Registrar el profesor numero 11 lanza LimiteProfesoresException")
    void registrar11ProfesoresLanzaLimiteExcepcion() throws Exception {
        for (int i = 1; i <= Sistema.MAX_PROFESORES; i++) {
            sistema.registrarProfesor(i, "Prof_" + i, "Materia_" + i);
        }
        assertThrows(LimiteProfesoresException.class,
                () -> sistema.registrarProfesor(11, "Extra", "Extra"));
    }

    @Test
    @DisplayName("Eliminar profesor existente lo quita")
    void eliminarProfesorExistenteFunciona() throws Exception {
        sistema.registrarProfesor(1, "Ana", "Matematica");
        sistema.eliminarProfesor(1);
        assertEquals(0, sistema.listarProfesores().size());
        assertFalse(sistema.existeProfesor(1));
    }

    @Test
    @DisplayName("Eliminar profesor inexistente lanza ProfesorNoEncontradoException")
    void eliminarProfesorInexistenteLanzaExcepcion() {
        assertThrows(ProfesorNoEncontradoException.class,
                () -> sistema.eliminarProfesor(99));
    }

    @Test
    @DisplayName("Agregar alumno a profesor inexistente lanza ProfesorNoEncontradoException")
    void agregarAlumnoAProfesorInexistenteLanzaExcepcion() {
        assertThrows(ProfesorNoEncontradoException.class,
                () -> sistema.agregarAlumnoAProfesor(99, 100, "Juan", "1 Anio"));
    }

    @Test
    @DisplayName("Agregar alumno a profesor existente funciona")
    void agregarAlumnoAProfesorExistenteFunciona() throws Exception {
        sistema.registrarProfesor(1, "Ana", "Matematica");
        sistema.agregarAlumnoAProfesor(1, 100, "Juan", "1 Anio");
        assertEquals(1, sistema.obtenerProfesor(1).obtenerCantidadAlumnos());
    }

    @Test
    @DisplayName("obtenerCantidadTotalAlumnos suma todos los profesores")
    void obtenerCantidadTotalAlumnosSumaCorrectamente() throws Exception {
        sistema.registrarProfesor(1, "Ana", "Matematica");
        sistema.registrarProfesor(2, "Luis", "Historia");
        sistema.agregarAlumnoAProfesor(1, 100, "Juan", "1 Anio");
        sistema.agregarAlumnoAProfesor(1, 101, "Pedro", "1 Anio");
        sistema.agregarAlumnoAProfesor(2, 200, "Sofia", "2 Anio");
        assertEquals(3, sistema.obtenerCantidadTotalAlumnos());
    }

    @Test
    @DisplayName("obtenerProfesor con id inexistente lanza ProfesorNoEncontradoException")
    void obtenerProfesorInexistenteLanzaExcepcion() {
        assertThrows(ProfesorNoEncontradoException.class,
                () -> sistema.obtenerProfesor(99));
    }
}
