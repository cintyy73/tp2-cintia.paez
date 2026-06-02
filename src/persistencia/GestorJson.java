package persistencia;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import excepciones.DatoInvalidoException;
import excepciones.SistemaException;
import modelo.Alumno;
import modelo.Profesor;
import modelo.Sistema;

public class GestorJson {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // ==============================================================
    // GUARDAR: Sistema -> archivo JSON
    // ==============================================================

    public static void guardar(Sistema sistema, String ruta) throws IOException {
        if (sistema == null) {
            throw new IllegalArgumentException("El sistema a guardar no puede ser nulo.");
        }

        JsonObject root = new JsonObject();
        root.addProperty("nombre", sistema.getNombre());
        root.addProperty("fechaCreacion", sistema.getFechaCreacion().toString());

        JsonArray profesoresArray = new JsonArray();
        for (Profesor p : sistema.listarProfesores()) {
            JsonObject profJson = new JsonObject();
            profJson.addProperty("id", p.getId());
            profJson.addProperty("nombre", p.getNombre());
            profJson.addProperty("materia", p.getMateria());

            JsonArray alumnosArray = new JsonArray();
            for (Alumno a : p.getAlumnos()) {
                JsonObject alumJson = new JsonObject();
                alumJson.addProperty("id", a.getId());
                alumJson.addProperty("nombre", a.getNombre());
                alumJson.addProperty("gradoCurso", a.getGradoCurso());
                alumnosArray.add(alumJson);
            }
            profJson.add("alumnos", alumnosArray);
            profesoresArray.add(profJson);
        }
        root.add("profesores", profesoresArray);

        try (FileWriter fw = new FileWriter(ruta)) {
            GSON.toJson(root, fw);
        }
    }

    // ==============================================================
    // CARGAR: archivo JSON -> Sistema
    // ==============================================================

    public static Sistema cargar(String ruta) throws IOException, SistemaException {
        JsonObject root;
        try (FileReader fr = new FileReader(ruta)) {
            root = JsonParser.parseReader(fr).getAsJsonObject();
        } catch (JsonSyntaxException | IllegalStateException e) {
            throw new DatoInvalidoException("JSON malformado: " + e.getMessage());
        }

        String nombre;
        LocalDate fecha;
        try {
            nombre = root.get("nombre").getAsString();
            String fechaStr = root.get("fechaCreacion").getAsString();
            fecha = LocalDate.parse(fechaStr);
        } catch (NullPointerException | IllegalStateException e) {
            throw new DatoInvalidoException("Faltan campos obligatorios (nombre o fechaCreacion) en el JSON.");
        } catch (DateTimeParseException e) {
            throw new DatoInvalidoException("Fecha de creacion invalida: " + e.getParsedString());
        }

        Sistema sistema = new Sistema(nombre, fecha);

        if (!root.has("profesores")) {
            return sistema;
        }
        JsonArray profesoresArray = root.getAsJsonArray("profesores");

        for (JsonElement profElem : profesoresArray) {
            JsonObject profJson = profElem.getAsJsonObject();
            int profId = profJson.get("id").getAsInt();
            String profNombre = profJson.get("nombre").getAsString();
            String profMateria = profJson.get("materia").getAsString();
            sistema.registrarProfesor(profId, profNombre, profMateria);

            if (!profJson.has("alumnos")) continue;
            JsonArray alumnosArray = profJson.getAsJsonArray("alumnos");
            for (JsonElement alumElem : alumnosArray) {
                JsonObject alumJson = alumElem.getAsJsonObject();
                int alumId = alumJson.get("id").getAsInt();
                String alumNombre = alumJson.get("nombre").getAsString();
                String alumGrado = alumJson.get("gradoCurso").getAsString();
                sistema.agregarAlumnoAProfesor(profId, alumId, alumNombre, alumGrado);
            }
        }

        return sistema;
    }
}
