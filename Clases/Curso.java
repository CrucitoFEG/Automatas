public class Curso {
    int id;
    String nombre;

    public Curso(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String toString() {
        return "Curso{id=" + id + ", nombre='" + nombre + "'}";
    }
}