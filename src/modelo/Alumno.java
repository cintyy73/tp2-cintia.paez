package modelo;

import java.util.Objects;

import excepciones.DatoInvalidoException;

public class Alumno {

    private int id;
    private String nombre;
    private String gradoCurso;

    public Alumno(int id, String nombre, String gradoCurso) throws DatoInvalidoException {
        setId(id);
        setNombre(nombre);
        setGradoCurso(gradoCurso);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) throws DatoInvalidoException {
        if (id <= 0) {
            throw new DatoInvalidoException("El id del alumno debe ser un entero positivo.");
        }
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws DatoInvalidoException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatoInvalidoException("El nombre del alumno es obligatorio.");
        }
        this.nombre = nombre.trim();
    }

    public String getGradoCurso() {
        return gradoCurso;
    }

    public void setGradoCurso(String gradoCurso) throws DatoInvalidoException {
        if (gradoCurso == null || gradoCurso.trim().isEmpty()) {
            throw new DatoInvalidoException("El grado o curso del alumno es obligatorio.");
        }
        this.gradoCurso = gradoCurso.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Alumno)) return false;
        Alumno other = (Alumno) o;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Alumno{id=" + id + ", nombre='" + nombre + "', gradoCurso='" + gradoCurso + "'}";
    }
}
