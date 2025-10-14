package sistema;

import modelo.*;
import java.util.*;

public class SistemaAcademico {

    private Map<Integer, Curso> cursos;
    private Map<Integer, Estudiante> estudiantes;

    public SistemaAcademico() {
        this.cursos = new HashMap<>();
        this.estudiantes = new HashMap<>();
    }

    // --- OPERACIONES CON CURSOS ---
    public String crearCurso(int numero, String nombre) {
        if (cursos.containsKey(numero)) {
            return "<span style='color:red;'>Error: Ya existe un curso con número " + numero + "</span>";
        }
        cursos.put(numero, new Curso(numero, nombre));
        return "<span style='color:green;'>&#10003; Curso creado: " + nombre + " (#" + numero + ")</span>";
    }

    public Curso buscarCurso(int numero) {
        return cursos.get(numero);
    }

    public String buscarCursoString(int numero) {
        Curso curso = cursos.get(numero);
        if (curso == null) {
            return "<span style='color:red;'>No se encontró curso con número: " + numero + "</span>";
        }
        return "<span style='color:blue;'>" + curso.toString() + "</span>";
    }

    // --- OPERACIONES CON ESTUDIANTES ---
    public String crearEstudiante(int numero, String nombre, int numeroCurso) {
        if (estudiantes.containsKey(numero)) {
            return "<span style='color:red;'>Error: Ya existe un estudiante con número " + numero + "</span>";
        }
        if (!cursos.containsKey(numeroCurso)) {
            return "<span style='color:red;'>Error: No existe el curso #" + numeroCurso + "</span>";
        }
        estudiantes.put(numero, new Estudiante(numero, nombre, numeroCurso));
        return "<span style='color:green;'>&#10003; Estudiante creado: " + nombre + " (#" + numero + ")</span>";
    }

    public String buscarEstudiante(int numero) {
        Estudiante estudiante = estudiantes.get(numero);
        if (estudiante == null) {
            return "<span style='color:red;'>No se encontró estudiante con número: " + numero + "</span>";
        }
        return "<span style='color:blue;'>" + estudiante.toString() + "</span>";
    }

    public String eliminarEstudiante(int numero) {
        if (!estudiantes.containsKey(numero)) {
            return "<span style='color:red;'>Error: No existe estudiante con número " + numero + "</span>";
        }
        Estudiante eliminado = estudiantes.remove(numero);
        return "<span style='color:orange;'>&#10003; Estudiante eliminado: " + eliminado.getNombre() + "</span>";
    }

    // --- MÉTODOS DE MOSTRAR INFORMACIÓN ---
    public String mostrarEstudiantes() {
        StringBuilder sb = new StringBuilder();
        sb.append("<b>=== LISTA DE ESTUDIANTES ===</b><br>");
        if (estudiantes.isEmpty()) {
            sb.append("No hay estudiantes registrados.<br>");
        } else {
            for (Estudiante est : estudiantes.values()) {
                sb.append(est.toString()).append("<br>");
            }
        }
        sb.append("Total: ").append(estudiantes.size()).append(" estudiantes");
        return sb.toString();
    }

    public String mostrarCursos() {
        StringBuilder sb = new StringBuilder();
        sb.append("<b>=== LISTA DE CURSOS ===</b><br>");
        if (cursos.isEmpty()) {
            sb.append("No hay cursos registrados.<br>");
        } else {
            for (Curso curso : cursos.values()) {
                sb.append(curso.toString()).append("<br>");
            }
        }
        sb.append("Total: ").append(cursos.size()).append(" cursos");
        return sb.toString();
    }

    // --- GETTERS PARA EL SISTEMA ---
    public Map<Integer, Curso> getCursos() {
        return new HashMap<>(cursos);
    }

    public Map<Integer, Estudiante> getEstudiantes() {
        return new HashMap<>(estudiantes);
    }

    public String eliminarCurso(int num) {
        if (!cursos.containsKey(num)) {
            return "<span style='color:red;'>Error: No existe curso con número " + num + "</span>";
        }
        Curso eliminado = cursos.remove(num);
        return "<span style='color:orange;'>&#10003; Curso eliminado: " + eliminado.getNombre() + "</span>";
    }
}
