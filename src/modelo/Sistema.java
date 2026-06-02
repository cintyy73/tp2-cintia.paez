package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import excepciones.AlumnoDuplicadoException;
import excepciones.AlumnoNoEncontradoException;
import excepciones.DatoInvalidoException;
import excepciones.LimiteAlumnosException;
import excepciones.LimiteProfesoresException;
import excepciones.ProfesorDuplicadoException;
import excepciones.ProfesorNoEncontradoException;

public class Sistema {

    public static final int MAX_PROFESORES = 10;

    private String nombre;
    private LocalDate fechaCreacion;
    private final List<Profesor> profesores;

    public Sistema(String nombre, LocalDate fechaCreacion) throws DatoInvalidoException {
        setNombre(nombre);
        setFechaCreacion(fechaCreacion);
        this.profesores = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws DatoInvalidoException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatoInvalidoException("El nombre del sistema es obligatorio.");
        }
        this.nombre = nombre.trim();
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) throws DatoInvalidoException {
        if (fechaCreacion == null) {
            throw new DatoInvalidoException("La fecha de creación del sistema es obligatoria.");
        }
        this.fechaCreacion = fechaCreacion;
    }

    public List<Profesor> listarProfesores() {
        return Collections.unmodifiableList(profesores);
    }

    public Profesor registrarProfesor(int id, String nombre, String materia)
            throws DatoInvalidoException, LimiteProfesoresException, ProfesorDuplicadoException {
        if (profesores.size() >= MAX_PROFESORES) {
            throw new LimiteProfesoresException(
                "El sistema ya tiene el máximo de " + MAX_PROFESORES + " profesores registrados.");
        }
        if (existeProfesor(id)) {
            throw new ProfesorDuplicadoException("Ya existe un profesor registrado con id " + id + ".");
        }
        Profesor profesor = new Profesor(id, nombre, materia);
        profesores.add(profesor);
        return profesor;
    }

    public void eliminarProfesor(int idProfesor) throws ProfesorNoEncontradoException {
        Profesor profesor = buscarProfesor(idProfesor);
        if (profesor == null) {
            throw new ProfesorNoEncontradoException("No existe un profesor con id " + idProfesor + ".");
        }
        profesores.remove(profesor);
    }

    public void agregarAlumnoAProfesor(int idProfesor, int idAlumno, String nombreAlumno, String gradoCurso)
            throws ProfesorNoEncontradoException, DatoInvalidoException,
                   LimiteAlumnosException, AlumnoDuplicadoException {
        Profesor profesor = obtenerProfesor(idProfesor);
        Alumno alumno = new Alumno(idAlumno, nombreAlumno, gradoCurso);
        profesor.agregarAlumno(alumno);
    }

    public void eliminarAlumnoDeProfesor(int idProfesor, int idAlumno)
            throws ProfesorNoEncontradoException, AlumnoNoEncontradoException {
        Profesor profesor = obtenerProfesor(idProfesor);
        profesor.eliminarAlumno(idAlumno);
    }

    public int obtenerCantidadTotalAlumnos() {
        int total = 0;
        for (Profesor p : profesores) {
            total += p.obtenerCantidadAlumnos();
        }
        return total;
    }

    public Profesor obtenerProfesor(int idProfesor) throws ProfesorNoEncontradoException {
        Profesor profesor = buscarProfesor(idProfesor);
        if (profesor == null) {
            throw new ProfesorNoEncontradoException("No existe un profesor con id " + idProfesor + ".");
        }
        return profesor;
    }

    public boolean existeProfesor(int idProfesor) {
        return buscarProfesor(idProfesor) != null;
    }

    private Profesor buscarProfesor(int idProfesor) {
        for (Profesor p : profesores) {
            if (p.getId() == idProfesor) {
                return p;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Sistema{nombre='" + nombre + "', fechaCreacion=" + fechaCreacion
                + ", cantidadProfesores=" + profesores.size()
                + ", cantidadTotalAlumnos=" + obtenerCantidadTotalAlumnos() + "}";
    }
}
