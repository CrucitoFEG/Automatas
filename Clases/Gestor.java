import java.util.*;

public class Gestor {
    List<Curso> cursos = new ArrayList<>();
    List<Estudiante> estudiantes = new ArrayList<>();

    public void agregarCurso(int id, String nombre) {
        cursos.add(new Curso(id, nombre));
        System.out.println("✔ Curso agregado: " + nombre);
    }

    public void agregarEstudiante(int id, String nombre, int cursoId) {
        estudiantes.add(new Estudiante(id, nombre, cursoId));
        System.out.println("✔ Estudiante agregado: " + nombre);
    }

    public void mostrarCurso() {
        System.out.println("🏫 Lista de cursos:");
        for (Curso c : cursos) System.out.println("- " + c);
    }

    public void mostrarEstudiante() {
        System.out.println("📚 Lista de estudiantes:");
        for (Estudiante e : estudiantes) System.out.println("- " + e);
    }

    public void buscarEstudiante(int id) {
        for (Estudiante e : estudiantes) {
            if (e.id == id) {
                System.out.println("🔍 Encontrado: " + e);
                return;
            }
        }
        System.out.println("❌ Estudiante no encontrado.");
    }

    public void eliminarEstudiante(int id) {
        estudiantes.removeIf(e -> e.id == id);
        System.out.println("❌ Eliminado: ID " + id);
    }
}