package org.project.modelo;

public class Estudiante {

    private int numero;
    private String nombre;
    private int numeroCurso; // Referencia al curso al que pertenece

    public Estudiante(int numero, String nombre, int numeroCurso) {
        this.numero = numero;
        this.nombre = nombre;
        this.numeroCurso = numeroCurso;
    }

    // Getters
    public int getNumero() {
        return numero;
    }

    public String getNombre() {
        return nombre;
    }

    public int getNumeroCurso() {
        return numeroCurso;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setNumeroCurso(int numeroCurso) {
        this.numeroCurso = numeroCurso;
    }

    @Override
    public String toString() {
        return "Estudiante #" + numero + ": " + nombre + " (Curso: " + numeroCurso + ")";
    }

    // Para comparación y búsquedas
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Estudiante estudiante = (Estudiante) obj;
        return numero == estudiante.numero;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(numero);
    }
}
