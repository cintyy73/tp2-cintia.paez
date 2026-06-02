package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import excepciones.AlumnoDuplicadoException;
import excepciones.AlumnoNoEncontradoException;
import excepciones.DatoInvalidoException;
import excepciones.LimiteAlumnosException;

public class Profesor {

    public static final int MAX_ALUMNOS = 10;

    private int id;
    private String nombre;
    private String materia;
    private final List<Alumno> alumnos;

    public Profesor(int id, String nombre, String materia) throws DatoInvalidoException {
        setId(id);
        setNombre(nombre);
        setMateria(materia);
        this.alumnos = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) throws DatoInvalidoException {
        if (id <= 0) {
            throw new DatoInvalidoException("El id del profesor debe ser un entero positivo.");
        }
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws DatoInvalidoException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatoInvalidoException("El nombre del profesor es obligatorio.");
        }
        this.nombre = nombre.trim();
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) throws DatoInvalidoException {
        if (materia == null || materia.trim().isEmpty()) {
            throw new DatoInvalidoException("La materia del profesor es obligatoria.");
        }
        this.materia = materia.trim();
    }

    public List<Alumno> getAlumnos() {
        return Collections.unmodifiableList(alumnos);
    }

    public void agregarAlumno(Alumno alumno)
            throws DatoInvalidoException, LimiteAlumnosException, AlumnoDuplicadoException {
        if (alumno == null) {
            throw new DatoInvalidoException("El alumno no puede ser nulo.");
        }
        if (alumnos.size() >= MAX_ALUMNOS) {
            throw new LimiteAlumnosException(
                "El profesor " + nombre + " ya tiene el máximo de " + MAX_ALUMNOS + " alumnos asignados.");
        }
        if (contieneAlumno(alumno.getId())) {
            throw new AlumnoDuplicadoException(
                "El alumno con id " + alumno.getId() + " ya está asignado al profesor " + nombre + ".");
        }
        alumnos.add(alumno);
    }

    public void eliminarAlumno(int idAlumno) throws AlumnoNoEncontradoException {
        Alumno encontrado = buscarAlumno(idAlumno);
        if (encontrado == null) {
            throw new AlumnoNoEncontradoException(
                "No existe un alumno con id " + idAlumno + " asignado al profesor " + nombre + ".");
        }
        alumnos.remove(encontrado);
    }

    public int obtenerCantidadAlumnos() {
        return alumnos.size();
    }

    public boolean contieneAlumno(int idAlumno) {
        return buscarAlumno(idAlumno) != null;
    }

    private Alumno buscarAlumno(int idAlumno) {
        for (Alumno a : alumnos) {
            if (a.getId() == idAlumno) {
                return a;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Profesor)) return false;
        Profesor other = (Profesor) o;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Profesor{id=" + id + ", nombre='" + nombre + "', materia='" + materia
                + "', cantidadAlumnos=" + alumnos.size() + "}";
    }
}
