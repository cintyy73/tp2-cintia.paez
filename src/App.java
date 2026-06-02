import java.io.IOException;
import java.time.LocalDate;

import excepciones.AlumnoDuplicadoException;
import excepciones.AlumnoNoEncontradoException;
import excepciones.DatoInvalidoException;
import excepciones.LimiteAlumnosException;
import excepciones.LimiteProfesoresException;
import excepciones.ProfesorDuplicadoException;
import excepciones.ProfesorNoEncontradoException;
import excepciones.SistemaException;
import modelo.Alumno;
import modelo.Profesor;
import modelo.Sistema;
import persistencia.GestorJson;

public class App {

    public static void main(String[] args) {
        encabezado("SISTEMA DE ADMINISTRACION DE PROFESORES Y ALUMNOS");

        Sistema sistema;
        try {
            sistema = new Sistema("UCES - Programacion II", LocalDate.of(2026, 6, 1));
            System.out.println("[OK] Sistema creado: " + sistema.getNombre());
            System.out.println("     Fecha de creacion: " + sistema.getFechaCreacion());
        } catch (DatoInvalidoException e) {
            System.err.println("Error al crear el sistema: " + e.getMessage());
            return;
        }

        registrarProfesores(sistema);
        asignarAlumnos(sistema);
        mostrarEstadoSistema(sistema);
        demostrarManejoDeErrores(sistema);
        mostrarEstadoSistema(sistema);
        demostrarPersistenciaJson(sistema);

        encabezado("FIN DE LA EJECUCION");
    }

    // ---------------------------------------------------------------
    // Operaciones del flujo principal
    // ---------------------------------------------------------------

    private static void registrarProfesores(Sistema sistema) {
        seccion("Registrando profesores");
        try {
            sistema.registrarProfesor(1, "Ana Garcia", "Matematica");
            sistema.registrarProfesor(2, "Luis Perez", "Historia");
            sistema.registrarProfesor(3, "Marta Ruiz", "Programacion");
            System.out.println("[OK] 3 profesores registrados.");
        } catch (SistemaException e) {
            System.err.println("Error registrando profesores: " + e.getMessage());
        }
    }

    private static void asignarAlumnos(Sistema sistema) {
        seccion("Asignando alumnos a profesores");
        try {
            sistema.agregarAlumnoAProfesor(1, 101, "Juan Sosa", "1 Anio");
            sistema.agregarAlumnoAProfesor(1, 102, "Sofia Diaz", "1 Anio");
            sistema.agregarAlumnoAProfesor(1, 103, "Pablo Nunez", "2 Anio");
            sistema.agregarAlumnoAProfesor(2, 201, "Lucia Romero", "3 Anio");
            sistema.agregarAlumnoAProfesor(2, 202, "Tomas Vega", "3 Anio");
            sistema.agregarAlumnoAProfesor(3, 301, "Camila Torres", "4 Anio");
            System.out.println("[OK] 6 alumnos asignados a profesores.");
        } catch (SistemaException e) {
            System.err.println("Error asignando alumnos: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // Pantalla: estado completo del sistema
    // ---------------------------------------------------------------

    private static void mostrarEstadoSistema(Sistema sistema) {
        seccion("Estado actual del sistema");
        System.out.println("Sistema:        " + sistema.getNombre());
        System.out.println("Fecha creacion: " + sistema.getFechaCreacion());
        System.out.println("Profesores:     " + sistema.listarProfesores().size() + " / " + Sistema.MAX_PROFESORES);
        System.out.println("Alumnos total:  " + sistema.obtenerCantidadTotalAlumnos());
        System.out.println();
        System.out.println("Detalle por profesor:");
        for (Profesor p : sistema.listarProfesores()) {
            System.out.println("  - [" + p.getId() + "] " + p.getNombre()
                    + " | Materia: " + p.getMateria()
                    + " | Alumnos: " + p.obtenerCantidadAlumnos() + "/" + Profesor.MAX_ALUMNOS);
            for (Alumno a : p.getAlumnos()) {
                System.out.println("        * [" + a.getId() + "] " + a.getNombre() + " (" + a.getGradoCurso() + ")");
            }
        }
    }

    // ---------------------------------------------------------------
    // Demostracion de manejo de excepciones (try/catch)
    // ---------------------------------------------------------------

    private static void demostrarManejoDeErrores(Sistema sistema) {
        seccion("Demostracion de manejo de excepciones");

        // 1) Profesor inexistente
        System.out.println();
        System.out.println("-> Intento: agregar alumno a profesor inexistente (id=99)");
        try {
            sistema.agregarAlumnoAProfesor(99, 999, "Alumno X", "1 Anio");
        } catch (ProfesorNoEncontradoException e) {
            System.out.println("   [CAPTURADA " + e.getClass().getSimpleName() + "] " + e.getMessage());
        } catch (SistemaException e) {
            System.out.println("   [INESPERADA] " + e.getMessage());
        }

        // 2) Alumno duplicado
        System.out.println();
        System.out.println("-> Intento: agregar alumno duplicado (id=101 al profesor 1)");
        try {
            sistema.agregarAlumnoAProfesor(1, 101, "Repetido", "1 Anio");
        } catch (AlumnoDuplicadoException e) {
            System.out.println("   [CAPTURADA " + e.getClass().getSimpleName() + "] " + e.getMessage());
        } catch (SistemaException e) {
            System.out.println("   [INESPERADA] " + e.getMessage());
        }

        // 3) Profesor duplicado
        System.out.println();
        System.out.println("-> Intento: registrar profesor con id duplicado (id=1)");
        try {
            sistema.registrarProfesor(1, "Otro Nombre", "Otra Materia");
        } catch (ProfesorDuplicadoException e) {
            System.out.println("   [CAPTURADA " + e.getClass().getSimpleName() + "] " + e.getMessage());
        } catch (SistemaException e) {
            System.out.println("   [INESPERADA] " + e.getMessage());
        }

        // 4) Datos invalidos
        System.out.println();
        System.out.println("-> Intento: registrar profesor con nombre vacio");
        try {
            sistema.registrarProfesor(20, "   ", "Quimica");
        } catch (DatoInvalidoException e) {
            System.out.println("   [CAPTURADA " + e.getClass().getSimpleName() + "] " + e.getMessage());
        } catch (SistemaException e) {
            System.out.println("   [INESPERADA] " + e.getMessage());
        }

        // 5) Eliminar alumno inexistente
        System.out.println();
        System.out.println("-> Intento: eliminar alumno inexistente (idProf=1, idAlum=9999)");
        try {
            sistema.eliminarAlumnoDeProfesor(1, 9999);
        } catch (AlumnoNoEncontradoException e) {
            System.out.println("   [CAPTURADA " + e.getClass().getSimpleName() + "] " + e.getMessage());
        } catch (SistemaException e) {
            System.out.println("   [INESPERADA] " + e.getMessage());
        }

        // 6) Limite de alumnos por profesor (>10)
        System.out.println();
        System.out.println("-> Intento: superar limite de " + Profesor.MAX_ALUMNOS + " alumnos para el profesor 3");
        try {
            for (int i = 1000; i < 1015; i++) {
                sistema.agregarAlumnoAProfesor(3, i, "Alumno_" + i, "1 Anio");
            }
        } catch (LimiteAlumnosException e) {
            System.out.println("   [CAPTURADA " + e.getClass().getSimpleName() + "] " + e.getMessage());
        } catch (SistemaException e) {
            System.out.println("   [INESPERADA] " + e.getMessage());
        }

        // 7) Limite de profesores (>10)
        System.out.println();
        System.out.println("-> Intento: superar limite de " + Sistema.MAX_PROFESORES + " profesores");
        try {
            for (int i = 4; i <= 15; i++) {
                sistema.registrarProfesor(i, "Profesor_" + i, "Materia_" + i);
            }
        } catch (LimiteProfesoresException e) {
            System.out.println("   [CAPTURADA " + e.getClass().getSimpleName() + "] " + e.getMessage());
        } catch (SistemaException e) {
            System.out.println("   [INESPERADA] " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // Persistencia: guardar y cargar JSON
    // ---------------------------------------------------------------

    private static void demostrarPersistenciaJson(Sistema sistema) {
        seccion("Persistencia: guardar y cargar JSON");

        String rutaSalida = "sistema_resultado.json";

        // 1) Guardar
        System.out.println("-> Guardando sistema en \"" + rutaSalida + "\"...");
        try {
            GestorJson.guardar(sistema, rutaSalida);
            System.out.println("   [OK] Sistema guardado en \"" + rutaSalida + "\".");
        } catch (IOException e) {
            System.err.println("   [ERROR] No se pudo guardar: " + e.getMessage());
            return;
        }

        // 2) Cargar el mismo archivo en un nuevo Sistema (demuestra roundtrip)
        System.out.println();
        System.out.println("-> Cargando sistema desde \"" + rutaSalida + "\"...");
        Sistema sistemaCargado;
        try {
            sistemaCargado = GestorJson.cargar(rutaSalida);
            System.out.println("   [OK] Sistema cargado correctamente.");
        } catch (IOException e) {
            System.err.println("   [ERROR] No se pudo leer el archivo: " + e.getMessage());
            return;
        } catch (SistemaException e) {
            System.err.println("   [ERROR] Contenido invalido: " + e.getMessage());
            return;
        }

        // 3) Mostrar el sistema cargado
        System.out.println();
        System.out.println("Sistema cargado desde archivo:");
        System.out.println("  Nombre:         " + sistemaCargado.getNombre());
        System.out.println("  Fecha creacion: " + sistemaCargado.getFechaCreacion());
        System.out.println("  Profesores:     " + sistemaCargado.listarProfesores().size()
                + " / " + Sistema.MAX_PROFESORES);
        System.out.println("  Alumnos total:  " + sistemaCargado.obtenerCantidadTotalAlumnos());

        // 4) Verificar roundtrip
        boolean coincide = sistema.getNombre().equals(sistemaCargado.getNombre())
                && sistema.getFechaCreacion().equals(sistemaCargado.getFechaCreacion())
                && sistema.listarProfesores().size() == sistemaCargado.listarProfesores().size()
                && sistema.obtenerCantidadTotalAlumnos() == sistemaCargado.obtenerCantidadTotalAlumnos();
        System.out.println();
        System.out.println("  Roundtrip (guardar -> cargar) consistente: " + (coincide ? "SI" : "NO"));
    }

    // ---------------------------------------------------------------
    // Helpers de impresion
    // ---------------------------------------------------------------

    private static void encabezado(String titulo) {
        System.out.println();
        System.out.println("==============================================================");
        System.out.println("  " + titulo);
        System.out.println("==============================================================");
    }

    private static void seccion(String titulo) {
        System.out.println();
        System.out.println("--------------------------------------------------------------");
        System.out.println("  " + titulo);
        System.out.println("--------------------------------------------------------------");
    }
}
