Sistema de Administración de Profesores y Alumnos

Integrante: Cintia Paez
Materia: Programación 02 - UCES
Parcial: 2do parcial

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
