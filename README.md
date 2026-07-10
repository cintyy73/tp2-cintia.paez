Sistema de Administración de Profesores y Alumnos

Integrante: Cintia Paez
Materia: Programación 02 - UCES
Parcial: 2do parcial (base) + mejoras para el final

--------------------------------------------------
Mejoras agregadas para el final
--------------------------------------------------

Sobre la entrega del 2do parcial se sumaron, sin modificar la estructura
interna que ya funcionaba (la lista de profesores se mantiene):

- Expresiones lambda y Streams: nuevas consultas y reportes en Sistema.
- Maps: agrupación y conteo de alumnos por curso (groupingBy / counting).
- Front de consola interactivo: App.java pasó a ser un menú operable.
- 8 tests nuevos sobre las consultas (47 tests en total, todos en verde).

--------------------------------------------------
TAD Sistema
--------------------------------------------------

Atributos
- nombre = String requerido
- fechaCreacion = Date requerido
- profesores = Lista de Profesor

Operaciones
- registrarProfesor(id, nombre, materia)
- eliminarProfesor(idProfesor)
- agregarAlumnoAProfesor(idProfesor, idAlumno, nombreAlumno, gradoCurso)
- eliminarAlumnoDeProfesor(idProfesor, idAlumno)
- obtenerCantidadTotalAlumnos()
- obtenerProfesor(idProfesor)
- listarProfesores()

Consultas y reportes (expresiones lambda, Streams y Maps)
- buscarProfesoresPorMateria(materia)  -> filter con lambda
- profesorConMasAlumnos()              -> max con Comparator (Optional)
- listarTodosLosAlumnos()              -> flatMap
- agruparAlumnosPorCurso()             -> Map<curso, lista de alumnos>
- contarAlumnosPorCurso()              -> Map<curso, cantidad> (TreeMap)

--------------------------------------------------
TAD Profesor
--------------------------------------------------

Atributos
- id = int requerido único
- nombre = String requerido
- materia = String requerido
- alumnos = Lista de Alumno

Operaciones
- agregarAlumno(alumno)
- eliminarAlumno(idAlumno)
- obtenerCantidadAlumnos()
- contieneAlumno(idAlumno)

--------------------------------------------------
TAD Alumno
--------------------------------------------------

Atributos
- id = int requerido único
- nombre = String requerido
- gradoCurso = String requerido

Operaciones
- (getters y setters)

--------------------------------------------------
Invariantes de Representación
--------------------------------------------------

- La cantidad total de profesores no puede superar 10.
- La cantidad de alumnos asignados a un profesor no puede superar 10.
- El ID de cada profesor debe ser único en el sistema.
- El ID de cada alumno debe ser único dentro de la lista de alumnos de un profesor.
- El nombre del profesor es obligatorio (no nulo ni vacío).
- El nombre del alumno es obligatorio (no nulo ni vacío).
- La materia del profesor es obligatoria (no nula ni vacía).
- El grado/curso del alumno es obligatorio (no nulo ni vacío).
- No se puede asignar un alumno a un profesor inexistente.
- Un alumno no puede estar asignado dos veces al mismo profesor.
- El nombre del sistema es obligatorio.
- La fecha de creación del sistema es obligatoria.

--------------------------------------------------
Cómo ejecutar
--------------------------------------------------

Aplicación (menú interactivo)
- En VS Code, abrir src/App.java y usar el botón "Run".
- Al iniciar pregunta el nombre del sistema y si se quieren precargar
  datos de ejemplo. El menú permite dar de alta/baja profesores y
  alumnos, ver el estado, ver reportes y guardar/cargar en JSON.

Requisito de versión de Java
- El proyecto se compila y ejecuta con JDK 21 (configurado en
  .vscode/settings.json). Si aparece "UnsupportedClassVersionError" es
  porque se compiló y ejecutó con versiones distintas de Java.

Tests (JUnit 5)
- Ejecutar la clase de tests desde VS Code, o por consola con el
  junit-platform-console-standalone incluido en lib/.

--------------------------------------------------
Librerías (lib/)
--------------------------------------------------

- gson-2.10.1.jar                     -> persistencia JSON
- junit-jupiter-*, junit-platform-*   -> tests unitarios
