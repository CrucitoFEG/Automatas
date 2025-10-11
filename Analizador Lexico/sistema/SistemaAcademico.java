package org.project.sistema;

import org.project.modelo.*;
import java.util.*;

public class SistemaAcademico {

    private Map<Integer, Curso> cursos;
    private Map<Integer, Estudiante> estudiantes;

    public SistemaAcademico() {
        this.cursos = new HashMap<>();
        this.estudiantes = new HashMap<>();
    }

    // --- OPERACIONES CON CURSOS ---
    public boolean crearCurso(int numero, String nombre) {
        if (cursos.containsKey(numero)) {
            System.out.println("Error: Ya existe un curso con número " + numero);
            return false;
        }
        cursos.put(numero, new Curso(numero, nombre));
        System.out.println("✓ Curso creado: " + nombre + " (#" + numero + ")");
        return true;
    }

    public Curso buscarCurso(int numero) {
        return cursos.get(numero);
    }

    // --- OPERACIONES CON ESTUDIANTES ---
    public boolean crearEstudiante(int numero, String nombre, int numeroCurso) {
        if (estudiantes.containsKey(numero)) {
            System.out.println("Error: Ya existe un estudiante con número " + numero);
            return false;
        }

        // Verificar que el curso exista
        if (!cursos.containsKey(numeroCurso)) {
            System.out.println("Error: No existe el curso #" + numeroCurso);
            return false;
        }

        estudiantes.put(numero, new Estudiante(numero, nombre, numeroCurso));
        System.out.println("✓ Estudiante creado: " + nombre + " (#" + numero + ")");
        return true;
    }

    public Estudiante buscarEstudiante(int numero) {
        Estudiante estudiante = estudiantes.get(numero);
        if (estudiante == null) {
            System.out.println("No se encontró estudiante con número: " + numero);
        }
        return estudiante;
    }

    public boolean eliminarEstudiante(int numero) {
        if (!estudiantes.containsKey(numero)) {
            System.out.println("Error: No existe estudiante con número " + numero);
            return false;
        }

        Estudiante eliminado = estudiantes.remove(numero);
        System.out.println("✓ Estudiante eliminado: " + eliminado.getNombre());
        return true;
    }

    // --- MÉTODOS DE MOSTRAR INFORMACIÓN ---
    public void mostrarEstudiantes() {
        System.out.println("\n=== LISTA DE ESTUDIANTES ===");
        if (estudiantes.isEmpty()) {
            System.out.println("No hay estudiantes registrados.");
        } else {
            for (Estudiante est : estudiantes.values()) {
                System.out.println("  " + est);
            }
        }
        System.out.println("Total: " + estudiantes.size() + " estudiantes");
    }

    public void mostrarCursos() {
        System.out.println("\n=== LISTA DE CURSOS ===");
        if (cursos.isEmpty()) {
            System.out.println("No hay cursos registrados.");
        } else {
            for (Curso curso : cursos.values()) {
                System.out.println("  " + curso);
            }
        }
        System.out.println("Total: " + cursos.size() + " cursos");
    }

    // --- GETTERS PARA EL SISTEMA ---
    public Map<Integer, Curso> getCursos() {
        return new HashMap<>(cursos);
    }

    public Map<Integer, Estudiante> getEstudiantes() {
        return new HashMap<>(estudiantes);
    }
}
