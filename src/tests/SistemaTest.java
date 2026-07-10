package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import excepciones.DatoInvalidoException;
import excepciones.LimiteProfesoresException;
import excepciones.ProfesorDuplicadoException;
import excepciones.ProfesorNoEncontradoException;
import modelo.Alumno;
import modelo.Profesor;
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

    // ---------------------------------------------------------------
    // Consultas y reportes (Streams + Maps)
    // ---------------------------------------------------------------

    /** Arma un escenario con 3 profesores y varios alumnos para los reportes. */
    private void cargarEscenario() throws Exception {
        sistema.registrarProfesor(1, "Ana", "Matematica");
        sistema.registrarProfesor(2, "Luis", "Historia");
        sistema.registrarProfesor(3, "Marta", "Matematica");
        sistema.agregarAlumnoAProfesor(1, 101, "Juan", "1 Anio");
        sistema.agregarAlumnoAProfesor(1, 102, "Sofia", "1 Anio");
        sistema.agregarAlumnoAProfesor(1, 103, "Pablo", "2 Anio");
        sistema.agregarAlumnoAProfesor(2, 201, "Lucia", "2 Anio");
    }

    @Test
    @DisplayName("buscarProfesoresPorMateria devuelve todos los que dictan esa materia")
    void buscarProfesoresPorMateriaFunciona() throws Exception {
        cargarEscenario();
        List<Profesor> mate = sistema.buscarProfesoresPorMateria("Matematica");
        assertEquals(2, mate.size());
    }

    @Test
    @DisplayName("buscarProfesoresPorMateria no distingue mayusculas/minusculas")
    void buscarProfesoresPorMateriaIgnoraMayusculas() throws Exception {
        cargarEscenario();
        assertEquals(1, sistema.buscarProfesoresPorMateria("historia").size());
    }

    @Test
    @DisplayName("buscarProfesoresPorMateria con materia inexistente devuelve lista vacia")
    void buscarProfesoresPorMateriaInexistenteDevuelveVacio() throws Exception {
        cargarEscenario();
        assertTrue(sistema.buscarProfesoresPorMateria("Quimica").isEmpty());
    }

    @Test
    @DisplayName("profesorConMasAlumnos devuelve el profesor con mayor cantidad")
    void profesorConMasAlumnosFunciona() throws Exception {
        cargarEscenario();
        Optional<Profesor> top = sistema.profesorConMasAlumnos();
        assertTrue(top.isPresent());
        assertEquals(1, top.get().getId());
    }

    @Test
    @DisplayName("profesorConMasAlumnos sin profesores devuelve Optional vacio")
    void profesorConMasAlumnosSinProfesoresEsVacio() {
        assertFalse(sistema.profesorConMasAlumnos().isPresent());
    }

    @Test
    @DisplayName("listarTodosLosAlumnos aplana los alumnos de todos los profesores")
    void listarTodosLosAlumnosFunciona() throws Exception {
        cargarEscenario();
        List<Alumno> todos = sistema.listarTodosLosAlumnos();
        assertEquals(4, todos.size());
    }

    @Test
    @DisplayName("agruparAlumnosPorCurso agrupa por grado/curso")
    void agruparAlumnosPorCursoFunciona() throws Exception {
        cargarEscenario();
        Map<String, List<Alumno>> porCurso = sistema.agruparAlumnosPorCurso();
        assertEquals(2, porCurso.size());
        assertEquals(2, porCurso.get("1 Anio").size());
        assertEquals(2, porCurso.get("2 Anio").size());
    }

    @Test
    @DisplayName("contarAlumnosPorCurso cuenta los alumnos de cada curso")
    void contarAlumnosPorCursoFunciona() throws Exception {
        cargarEscenario();
        Map<String, Long> conteo = sistema.contarAlumnosPorCurso();
        assertEquals(2L, conteo.get("1 Anio"));
        assertEquals(2L, conteo.get("2 Anio"));
    }
}
