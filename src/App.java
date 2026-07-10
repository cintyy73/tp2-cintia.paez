import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import excepciones.SistemaException;
import modelo.Alumno;
import modelo.Profesor;
import modelo.Sistema;
import persistencia.GestorJson;

/**
 * Front de consola interactivo del Sistema de Administracion de Profesores y
 * Alumnos. Presenta un menu por el que el usuario navega tipeando opciones, y
 * conecta la logica del modelo (Sistema), la persistencia (GestorJson) y el
 * manejo de excepciones propias.
 */
public class App {

    private static final Scanner SC = new Scanner(System.in);
    private static final String ARCHIVO_JSON = "sistema_resultado.json";

    public static void main(String[] args) {
        encabezado("SISTEMA DE ADMINISTRACION DE PROFESORES Y ALUMNOS");

        Sistema sistema = crearSistemaInicial();
        ofrecerDatosDeEjemplo(sistema);

        boolean salir = false;
        while (!salir) {
            mostrarMenuPrincipal();
            int opcion = leerEntero("Elegi una opcion");
            switch (opcion) {
                case 1: registrarProfesor(sistema); break;
                case 2: eliminarProfesor(sistema); break;
                case 3: agregarAlumno(sistema); break;
                case 4: eliminarAlumno(sistema); break;
                case 5: mostrarEstadoSistema(sistema); break;
                case 6: menuReportes(sistema); break;
                case 7: guardarJson(sistema); break;
                case 8: sistema = cargarJson(sistema); break;
                case 9: cargarDatosDeEjemplo(sistema); break;
                case 0: salir = true; break;
                default: System.out.println("Opcion invalida. Proba de nuevo.");
            }
        }

        encabezado("Hasta luego!");
    }

    // ===============================================================
    // Creacion inicial del sistema
    // ===============================================================

    private static Sistema crearSistemaInicial() {
        while (true) {
            String nombre = leerLinea("Nombre del sistema (Enter para 'Sistema UCES')");
            if (nombre.isEmpty()) {
                nombre = "Sistema UCES";
            }
            try {
                Sistema sistema = new Sistema(nombre, LocalDate.now());
                System.out.println("[OK] Sistema creado: " + sistema.getNombre());
                return sistema;
            } catch (SistemaException e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
        }
    }

    /**
     * Al arrancar, ofrece precargar el sistema con datos de ejemplo para no
     * tener que cargarlos a mano. Si el usuario responde que no, arranca vacio.
     */
    private static void ofrecerDatosDeEjemplo(Sistema sistema) {
        String respuesta = leerLinea("Queres cargar datos de ejemplo? (s/n, Enter = si)");
        if (respuesta.isEmpty() || respuesta.equalsIgnoreCase("s")) {
            cargarDatosDeEjemplo(sistema);
        }
    }

    // ===============================================================
    // Menu principal
    // ===============================================================

    private static void mostrarMenuPrincipal() {
        System.out.println();
        System.out.println("---------------------- MENU ----------------------");
        System.out.println(" 1. Registrar profesor");
        System.out.println(" 2. Eliminar profesor");
        System.out.println(" 3. Agregar alumno a un profesor");
        System.out.println(" 4. Eliminar alumno de un profesor");
        System.out.println(" 5. Ver estado del sistema");
        System.out.println(" 6. Reportes y consultas");
        System.out.println(" 7. Guardar en archivo JSON");
        System.out.println(" 8. Cargar desde archivo JSON");
        System.out.println(" 9. Cargar datos de ejemplo");
        System.out.println(" 0. Salir");
        System.out.println("--------------------------------------------------");
    }

    // ===============================================================
    // Operaciones ABM (dan de alta/baja profesores y alumnos)
    // ===============================================================

    private static void registrarProfesor(Sistema sistema) {
        seccion("Registrar profesor");
        int id = leerEntero("Id del profesor");
        String nombre = leerLinea("Nombre");
        String materia = leerLinea("Materia");
        try {
            Profesor p = sistema.registrarProfesor(id, nombre, materia);
            System.out.println("[OK] Profesor registrado: " + p);
        } catch (SistemaException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private static void eliminarProfesor(Sistema sistema) {
        seccion("Eliminar profesor");
        int id = leerEntero("Id del profesor a eliminar");
        try {
            sistema.eliminarProfesor(id);
            System.out.println("[OK] Profesor " + id + " eliminado.");
        } catch (SistemaException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private static void agregarAlumno(Sistema sistema) {
        seccion("Agregar alumno a un profesor");
        int idProfesor = leerEntero("Id del profesor");
        int idAlumno = leerEntero("Id del alumno");
        String nombre = leerLinea("Nombre del alumno");
        String curso = leerLinea("Grado o curso");
        try {
            sistema.agregarAlumnoAProfesor(idProfesor, idAlumno, nombre, curso);
            System.out.println("[OK] Alumno agregado al profesor " + idProfesor + ".");
        } catch (SistemaException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private static void eliminarAlumno(Sistema sistema) {
        seccion("Eliminar alumno de un profesor");
        int idProfesor = leerEntero("Id del profesor");
        int idAlumno = leerEntero("Id del alumno a eliminar");
        try {
            sistema.eliminarAlumnoDeProfesor(idProfesor, idAlumno);
            System.out.println("[OK] Alumno " + idAlumno + " eliminado del profesor " + idProfesor + ".");
        } catch (SistemaException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // ===============================================================
    // Estado completo del sistema
    // ===============================================================

    private static void mostrarEstadoSistema(Sistema sistema) {
        seccion("Estado actual del sistema");
        System.out.println("Sistema:        " + sistema.getNombre());
        System.out.println("Fecha creacion: " + sistema.getFechaCreacion());
        System.out.println("Profesores:     " + sistema.listarProfesores().size() + " / " + Sistema.MAX_PROFESORES);
        System.out.println("Alumnos total:  " + sistema.obtenerCantidadTotalAlumnos());
        System.out.println();
        if (sistema.listarProfesores().isEmpty()) {
            System.out.println("(Todavia no hay profesores registrados)");
            return;
        }
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

    // ===============================================================
    // Reportes y consultas (usan Streams + Maps del Sistema)
    // ===============================================================

    private static void menuReportes(Sistema sistema) {
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("------------- REPORTES Y CONSULTAS -------------");
            System.out.println(" 1. Buscar profesores por materia");
            System.out.println(" 2. Alumnos agrupados por curso");
            System.out.println(" 3. Cantidad de alumnos por curso");
            System.out.println(" 4. Profesor con mas alumnos");
            System.out.println(" 5. Listar todos los alumnos");
            System.out.println(" 0. Volver al menu principal");
            System.out.println("------------------------------------------------");
            int opcion = leerEntero("Elegi una opcion");
            switch (opcion) {
                case 1: reporteProfesoresPorMateria(sistema); break;
                case 2: reporteAlumnosPorCurso(sistema); break;
                case 3: reporteCantidadPorCurso(sistema); break;
                case 4: reporteProfesorConMasAlumnos(sistema); break;
                case 5: reporteTodosLosAlumnos(sistema); break;
                case 0: volver = true; break;
                default: System.out.println("Opcion invalida.");
            }
        }
    }

    private static void reporteProfesoresPorMateria(Sistema sistema) {
        seccion("Profesores por materia");
        String materia = leerLinea("Materia a buscar");
        List<Profesor> encontrados = sistema.buscarProfesoresPorMateria(materia);
        if (encontrados.isEmpty()) {
            System.out.println("No hay profesores que dicten \"" + materia + "\".");
            return;
        }
        System.out.println("Profesores de \"" + materia + "\":");
        encontrados.forEach(p ->
                System.out.println("  - [" + p.getId() + "] " + p.getNombre()
                        + " (" + p.obtenerCantidadAlumnos() + " alumnos)"));
    }

    private static void reporteAlumnosPorCurso(Sistema sistema) {
        seccion("Alumnos agrupados por curso");
        Map<String, List<Alumno>> porCurso = sistema.agruparAlumnosPorCurso();
        if (porCurso.isEmpty()) {
            System.out.println("Todavia no hay alumnos cargados.");
            return;
        }
        porCurso.forEach((curso, alumnos) -> {
            System.out.println(curso + ":");
            alumnos.forEach(a -> System.out.println("  - " + a.getNombre() + " [id " + a.getId() + "]"));
        });
    }

    private static void reporteCantidadPorCurso(Sistema sistema) {
        seccion("Cantidad de alumnos por curso");
        Map<String, Long> conteo = sistema.contarAlumnosPorCurso();
        if (conteo.isEmpty()) {
            System.out.println("Todavia no hay alumnos cargados.");
            return;
        }
        conteo.forEach((curso, cantidad) -> System.out.println("  " + curso + ": " + cantidad));
    }

    private static void reporteProfesorConMasAlumnos(Sistema sistema) {
        seccion("Profesor con mas alumnos");
        Optional<Profesor> top = sistema.profesorConMasAlumnos();
        if (!top.isPresent() || top.get().obtenerCantidadAlumnos() == 0) {
            System.out.println("Todavia no hay alumnos asignados a ningun profesor.");
            return;
        }
        Profesor p = top.get();
        System.out.println("  " + p.getNombre() + " (" + p.getMateria() + ") con "
                + p.obtenerCantidadAlumnos() + " alumnos.");
    }

    private static void reporteTodosLosAlumnos(Sistema sistema) {
        seccion("Todos los alumnos del sistema");
        List<Alumno> alumnos = sistema.listarTodosLosAlumnos();
        if (alumnos.isEmpty()) {
            System.out.println("Todavia no hay alumnos cargados.");
            return;
        }
        alumnos.forEach(a -> System.out.println("  - [" + a.getId() + "] " + a.getNombre()
                + " (" + a.getGradoCurso() + ")"));
        System.out.println("Total: " + alumnos.size() + " alumnos.");
    }

    // ===============================================================
    // Persistencia JSON
    // ===============================================================

    private static void guardarJson(Sistema sistema) {
        seccion("Guardar en archivo JSON");
        try {
            GestorJson.guardar(sistema, ARCHIVO_JSON);
            System.out.println("[OK] Sistema guardado en \"" + ARCHIVO_JSON + "\".");
        } catch (IOException e) {
            System.out.println("[ERROR] No se pudo guardar: " + e.getMessage());
        }
    }

    private static Sistema cargarJson(Sistema sistemaActual) {
        seccion("Cargar desde archivo JSON");
        try {
            Sistema cargado = GestorJson.cargar(ARCHIVO_JSON);
            System.out.println("[OK] Sistema cargado desde \"" + ARCHIVO_JSON + "\".");
            return cargado;
        } catch (IOException e) {
            System.out.println("[ERROR] No se pudo leer el archivo: " + e.getMessage());
        } catch (SistemaException e) {
            System.out.println("[ERROR] Contenido invalido: " + e.getMessage());
        }
        return sistemaActual;
    }

    // ===============================================================
    // Datos de ejemplo (equivale a la demo original)
    // ===============================================================

    private static void cargarDatosDeEjemplo(Sistema sistema) {
        seccion("Cargar datos de ejemplo");
        try {
            sistema.registrarProfesor(1, "Ana Garcia", "Matematica");
            sistema.registrarProfesor(2, "Luis Perez", "Historia");
            sistema.registrarProfesor(3, "Marta Ruiz", "Programacion");
            sistema.agregarAlumnoAProfesor(1, 101, "Juan Sosa", "1 Anio");
            sistema.agregarAlumnoAProfesor(1, 102, "Sofia Diaz", "1 Anio");
            sistema.agregarAlumnoAProfesor(1, 103, "Pablo Nunez", "2 Anio");
            sistema.agregarAlumnoAProfesor(2, 201, "Lucia Romero", "3 Anio");
            sistema.agregarAlumnoAProfesor(2, 202, "Tomas Vega", "3 Anio");
            sistema.agregarAlumnoAProfesor(3, 301, "Camila Torres", "4 Anio");
            System.out.println("[OK] Datos de ejemplo cargados (3 profesores, 6 alumnos).");
        } catch (SistemaException e) {
            System.out.println("[AVISO] " + e.getMessage()
                    + " (quizas ya estaban cargados)");
        }
    }

    // ===============================================================
    // Helpers de entrada y de impresion
    // ===============================================================

    private static String leerLinea(String mensaje) {
        System.out.print(mensaje + ": ");
        return SC.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje + ": ");
            String entrada = SC.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("  (Ingresa un numero entero valido)");
            }
        }
    }

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
