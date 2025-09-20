public class Estudiante {
    int id;
    String nombre;
    int cursoId;

    public Estudiante(int id, String nombre, int cursoId) {
        this.id = id;
        this.nombre = nombre;
        this.cursoId = cursoId;
    }

    public String toString() {
        return "Estudiante{id=" + id + ", nombre='" + nombre + "', cursoId=" + cursoId + "}";
    }
}