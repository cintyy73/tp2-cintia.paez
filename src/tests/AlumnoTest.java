package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import excepciones.DatoInvalidoException;
import modelo.Alumno;

class AlumnoTest {

    @Test
    @DisplayName("Crear alumno con datos validos")
    void crearAlumnoConDatosValidos() throws DatoInvalidoException {
        Alumno alumno = new Alumno(1, "Juan Perez", "1 Anio");
        assertEquals(1, alumno.getId());
        assertEquals("Juan Perez", alumno.getNombre());
        assertEquals("1 Anio", alumno.getGradoCurso());
    }

    @Test
    @DisplayName("Id negativo lanza DatoInvalidoException")
    void crearAlumnoConIdNegativoLanzaExcepcion() {
        assertThrows(DatoInvalidoException.class,
                () -> new Alumno(-1, "Juan", "1 Anio"));
    }

    @Test
    @DisplayName("Id cero lanza DatoInvalidoException")
    void crearAlumnoConIdCeroLanzaExcepcion() {
        assertThrows(DatoInvalidoException.class,
                () -> new Alumno(0, "Juan", "1 Anio"));
    }

    @Test
    @DisplayName("Nombre vacio lanza DatoInvalidoException")
    void crearAlumnoConNombreVacioLanzaExcepcion() {
        assertThrows(DatoInvalidoException.class,
                () -> new Alumno(1, "", "1 Anio"));
    }

    @Test
    @DisplayName("Nombre solo espacios lanza DatoInvalidoException")
    void crearAlumnoConNombreSoloEspaciosLanzaExcepcion() {
        assertThrows(DatoInvalidoException.class,
                () -> new Alumno(1, "   ", "1 Anio"));
    }

    @Test
    @DisplayName("Nombre null lanza DatoInvalidoException")
    void crearAlumnoConNombreNullLanzaExcepcion() {
        assertThrows(DatoInvalidoException.class,
                () -> new Alumno(1, null, "1 Anio"));
    }

    @Test
    @DisplayName("GradoCurso vacio lanza DatoInvalidoException")
    void crearAlumnoConGradoCursoVacioLanzaExcepcion() {
        assertThrows(DatoInvalidoException.class,
                () -> new Alumno(1, "Juan", ""));
    }

    @Test
    @DisplayName("setNombre recorta espacios al inicio y al final")
    void setNombreRecortaEspacios() throws DatoInvalidoException {
        Alumno alumno = new Alumno(1, "  Juan  ", "1 Anio");
        assertEquals("Juan", alumno.getNombre());
    }

    @Test
    @DisplayName("Dos alumnos con el mismo id son iguales (equals)")
    void alumnosConMismoIdSonIguales() throws DatoInvalidoException {
        Alumno a1 = new Alumno(1, "Juan", "1 Anio");
        Alumno a2 = new Alumno(1, "Pedro", "2 Anio");
        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    @DisplayName("Dos alumnos con id distinto NO son iguales")
    void alumnosConIdDistintoNoSonIguales() throws DatoInvalidoException {
        Alumno a1 = new Alumno(1, "Juan", "1 Anio");
        Alumno a2 = new Alumno(2, "Juan", "1 Anio");
        assertNotEquals(a1, a2);
    }
}
