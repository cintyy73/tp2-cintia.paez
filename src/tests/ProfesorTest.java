package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import excepciones.AlumnoDuplicadoException;
import excepciones.AlumnoNoEncontradoException;
import excepciones.DatoInvalidoException;
import excepciones.LimiteAlumnosException;
import modelo.Alumno;
import modelo.Profesor;

class ProfesorTest {

    private Profesor profesor;

    @BeforeEach
    void setUp() throws DatoInvalidoException {
        profesor = new Profesor(1, "Ana Garcia", "Matematica");
    }

    @Test
    @DisplayName("Crear profesor con datos validos")
    void crearProfesorConDatosValidos() {
        assertEquals(1, profesor.getId());
        assertEquals("Ana Garcia", profesor.getNombre());
        assertEquals("Matematica", profesor.getMateria());
        assertEquals(0, profesor.obtenerCantidadAlumnos());
    }

    @Test
    @DisplayName("Materia vacia lanza DatoInvalidoException")
    void crearProfesorConMateriaVaciaLanzaExcepcion() {
        assertThrows(DatoInvalidoException.class,
                () -> new Profesor(2, "Luis", ""));
    }

    @Test
    @DisplayName("Agregar alumno incrementa cantidad")
    void agregarAlumnoIncrementaCantidad() throws Exception {
        profesor.agregarAlumno(new Alumno(100, "Juan", "1 Anio"));
        assertEquals(1, profesor.obtenerCantidadAlumnos());
    }

    @Test
    @DisplayName("Agregar alumno null lanza DatoInvalidoException")
    void agregarAlumnoNullLanzaExcepcion() {
        assertThrows(DatoInvalidoException.class,
                () -> profesor.agregarAlumno(null));
    }

    @Test
    @DisplayName("Agregar alumno duplicado lanza AlumnoDuplicadoException")
    void agregarAlumnoDuplicadoLanzaExcepcion() throws Exception {
        profesor.agregarAlumno(new Alumno(100, "Juan", "1 Anio"));
        assertThrows(AlumnoDuplicadoException.class,
                () -> profesor.agregarAlumno(new Alumno(100, "Otro Nombre", "2 Anio")));
    }

    @Test
    @DisplayName("Se pueden agregar exactamente 10 alumnos (limite)")
    void agregar10AlumnosFunciona() throws Exception {
        for (int i = 1; i <= Profesor.MAX_ALUMNOS; i++) {
            profesor.agregarAlumno(new Alumno(i, "Alumno_" + i, "1 Anio"));
        }
        assertEquals(Profesor.MAX_ALUMNOS, profesor.obtenerCantidadAlumnos());
    }

    @Test
    @DisplayName("Agregar el alumno numero 11 lanza LimiteAlumnosException")
    void agregar11AlumnoLanzaLimiteExcepcion() throws Exception {
        for (int i = 1; i <= Profesor.MAX_ALUMNOS; i++) {
            profesor.agregarAlumno(new Alumno(i, "Alumno_" + i, "1 Anio"));
        }
        assertThrows(LimiteAlumnosException.class,
                () -> profesor.agregarAlumno(new Alumno(11, "Extra", "1 Anio")));
    }

    @Test
    @DisplayName("Eliminar alumno existente lo quita de la lista")
    void eliminarAlumnoExistenteFunciona() throws Exception {
        profesor.agregarAlumno(new Alumno(100, "Juan", "1 Anio"));
        profesor.eliminarAlumno(100);
        assertEquals(0, profesor.obtenerCantidadAlumnos());
        assertFalse(profesor.contieneAlumno(100));
    }

    @Test
    @DisplayName("Eliminar alumno inexistente lanza AlumnoNoEncontradoException")
    void eliminarAlumnoInexistenteLanzaExcepcion() {
        assertThrows(AlumnoNoEncontradoException.class,
                () -> profesor.eliminarAlumno(999));
    }

    @Test
    @DisplayName("contieneAlumno true si esta, false si no")
    void contieneAlumnoFunciona() throws Exception {
        profesor.agregarAlumno(new Alumno(100, "Juan", "1 Anio"));
        assertTrue(profesor.contieneAlumno(100));
        assertFalse(profesor.contieneAlumno(999));
    }

    @Test
    @DisplayName("getAlumnos devuelve lista inmutable")
    void getAlumnosEsInmutable() throws Exception {
        profesor.agregarAlumno(new Alumno(100, "Juan", "1 Anio"));
        assertThrows(UnsupportedOperationException.class,
                () -> profesor.getAlumnos().clear());
    }
}
