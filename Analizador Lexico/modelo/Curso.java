package modelo;

public class Curso {

        private int numero;
        private String nombre;

        public Curso(int numero, String nombre) {
            this.numero = numero;
            this.nombre = nombre;
        }

        // Getters
        public int getNumero() {
            return numero;
        }

        public String getNombre() {
            return nombre;
        }

        // Setters
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        @Override
        public String toString() {
            return "Curso #" + numero + ": " + nombre;
        }

        // Para comparación y búsquedas
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Curso curso = (Curso) obj;
            return numero == curso.numero;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(numero);
        }
}
