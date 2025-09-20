import java.util.*;

// ================== CLASES BASE ==================
class Curso {
    int id;
    String nombre;

    Curso(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Curso{id=" + id + ", nombre='" + nombre + "'}";
    }
}

class Estudiante {
    int id;
    String nombre;
    int cursoId;

    Estudiante(int id, String nombre, int cursoId) {
        this.id = id;
        this.nombre = nombre;
        this.cursoId = cursoId;
    }

    @Override
    public String toString() {
        return "Estudiante{id=" + id + ", nombre='" + nombre + "', cursoId=" + cursoId + "}";
    }
}

// ================== GESTOR ==================
class Gestor {
    Map<Integer, Curso> cursos = new HashMap<>();
    Map<Integer, Estudiante> estudiantes = new HashMap<>();

    void agregarCurso(int id, String nombre) {
        cursos.put(id, new Curso(id, nombre));
        System.out.println("✔ Curso agregado: " + nombre);
    }

    void agregarEstudiante(int id, String nombre, int cursoId) {
        if (!cursos.containsKey(cursoId)) {
            System.out.println("⚠ Error: El curso " + cursoId + " no existe.");
            return;
        }
        estudiantes.put(id, new Estudiante(id, nombre, cursoId));
        System.out.println("✔ Estudiante agregado: " + nombre);
    }

    void buscarEstudiante(int id) {
        Estudiante e = estudiantes.get(id);
        if (e != null) {
            System.out.println("🔍 Encontrado: " + e);
        } else {
            System.out.println("⚠ Estudiante " + id + " no encontrado.");
        }
    }

    void eliminarEstudiante(int id) {
        Estudiante e = estudiantes.remove(id);
        if (e != null) {
            System.out.println("❌ Eliminado: " + e.nombre);
        } else {
            System.out.println("⚠ Estudiante " + id + " no encontrado.");
        }
    }

    void mostrarEstudiantes() {
        System.out.println("📚 Lista de estudiantes:");
        for (Estudiante e : estudiantes.values()) {
            System.out.println(" - " + e);
        }
    }

    void mostrarCursos() {
        System.out.println("🏫 Lista de cursos:");
        for (Curso c : cursos.values()) {
            System.out.println(" - " + c);
        }
    }
}

// ================== ANALIZADOR LÉXICO ==================
enum TokenType {
    CURSO, ESTUDIANTE, BUSCAR, ELIMINAR, MOSTRAR_ESTUDIANTE, MOSTRAR_CURSO,
    NUMERO, IDENTIFICADOR, PAREN_IZQ, PAREN_DER, COMA, PUNTOYCOMA, EOF
}

class Token {
    TokenType tipo;
    String valor;

    Token(TokenType tipo, String valor) {
        this.tipo = tipo;
        this.valor = valor;
    }
}

class AnalizadorLexico {
    private String entrada;
    private int pos;

    AnalizadorLexico(String entrada) {
        this.entrada = entrada;
        this.pos = 0;
    }

    boolean esLetra(char c) { return Character.isLetter(c); }
    boolean esNumero(char c) { return Character.isDigit(c); }

    Token siguienteToken() {
        while (pos < entrada.length()) {
            char c = entrada.charAt(pos);

            if (Character.isWhitespace(c)) { pos++; continue; }

            if (c == '(') { pos++; return new Token(TokenType.PAREN_IZQ, "("); }
            if (c == ')') { pos++; return new Token(TokenType.PAREN_DER, ")"); }
            if (c == ',') { pos++; return new Token(TokenType.COMA, ","); }
            if (c == ';') { pos++; return new Token(TokenType.PUNTOYCOMA, ";"); }

            if (esNumero(c)) {
                StringBuilder sb = new StringBuilder();
                while (pos < entrada.length() && esNumero(entrada.charAt(pos))) {
                    sb.append(entrada.charAt(pos++));
                }
                return new Token(TokenType.NUMERO, sb.toString());
            }

            if (esLetra(c)) {
                StringBuilder sb = new StringBuilder();
                while (pos < entrada.length() && (esLetra(entrada.charAt(pos)) || esNumero(entrada.charAt(pos)))) {
                    sb.append(entrada.charAt(pos++));
                }
                String palabra = sb.toString().toUpperCase();

                switch (palabra) {
                    case "CURSO": return new Token(TokenType.CURSO, palabra);
                    case "ESTUDIANTE": return new Token(TokenType.ESTUDIANTE, palabra);
                    case "BUSCAR_ESTUDIANTE": return new Token(TokenType.BUSCAR, palabra);
                    case "ELIMINAR_ESTUDIANTE": return new Token(TokenType.ELIMINAR, palabra);
                    case "MOSTRAR_ESTUDIANTE": return new Token(TokenType.MOSTRAR_ESTUDIANTE, palabra);
                    case "MOSTRAR_CURSO": return new Token(TokenType.MOSTRAR_CURSO, palabra);
                    default: return new Token(TokenType.IDENTIFICADOR, palabra);
                }
            }

            pos++; 
        }
        return new Token(TokenType.EOF, "");
    }
}

// ================== ANALIZADOR SINTÁCTICO ==================
class AnalizadorSintactico {
    private AnalizadorLexico lexico;
    private Token token;
    private Gestor gestor;

    AnalizadorSintactico(String entrada, Gestor gestor) {
        this.lexico = new AnalizadorLexico(entrada);
        this.token = lexico.siguienteToken();
        this.gestor = gestor;
    }

    void consumir(TokenType esperado) {
        if (token.tipo == esperado) {
            token = lexico.siguienteToken();
        } else {
            throw new RuntimeException("Error sintáctico: se esperaba " + esperado);
        }
    }

    void parse() {
        while (token.tipo != TokenType.EOF) {
            if (token.tipo == TokenType.CURSO) parseCurso();
            else if (token.tipo == TokenType.ESTUDIANTE) parseEstudiante();
            else if (token.tipo == TokenType.BUSCAR) parseBuscar();
            else if (token.tipo == TokenType.ELIMINAR) parseEliminar();
            else if (token.tipo == TokenType.MOSTRAR_ESTUDIANTE) { consumir(TokenType.MOSTRAR_ESTUDIANTE); consumir(TokenType.PAREN_IZQ); consumir(TokenType.PAREN_DER); consumir(TokenType.PUNTOYCOMA); gestor.mostrarEstudiantes(); }
            else if (token.tipo == TokenType.MOSTRAR_CURSO) { consumir(TokenType.MOSTRAR_CURSO); consumir(TokenType.PAREN_IZQ); consumir(TokenType.PAREN_DER); consumir(TokenType.PUNTOYCOMA); gestor.mostrarCursos(); }
            else { throw new RuntimeException("Instrucción no reconocida"); }
        }
    }

    void parseCurso() {
        consumir(TokenType.CURSO);
        consumir(TokenType.PAREN_IZQ);
        int id = Integer.parseInt(token.valor); consumir(TokenType.NUMERO);
        consumir(TokenType.COMA);
        String nombre = token.valor; consumir(TokenType.IDENTIFICADOR);
        consumir(TokenType.PAREN_DER);
        consumir(TokenType.PUNTOYCOMA);
        gestor.agregarCurso(id, nombre);
    }

    void parseEstudiante() {
        consumir(TokenType.ESTUDIANTE);
        consumir(TokenType.PAREN_IZQ);
        int id = Integer.parseInt(token.valor); consumir(TokenType.NUMERO);
        consumir(TokenType.COMA);
        String nombre = token.valor; consumir(TokenType.IDENTIFICADOR);
        consumir(TokenType.COMA);
        int cursoId = Integer.parseInt(token.valor); consumir(TokenType.NUMERO);
        consumir(TokenType.PAREN_DER);
        consumir(TokenType.PUNTOYCOMA);
        gestor.agregarEstudiante(id, nombre, cursoId);
    }

    void parseBuscar() {
        consumir(TokenType.BUSCAR);
        consumir(TokenType.PAREN_IZQ);
        int id = Integer.parseInt(token.valor); consumir(TokenType.NUMERO);
        consumir(TokenType.PAREN_DER);
        consumir(TokenType.PUNTOYCOMA);
        gestor.buscarEstudiante(id);
    }

    void parseEliminar() {
        consumir(TokenType.ELIMINAR);
        consumir(TokenType.PAREN_IZQ);
        int id = Integer.parseInt(token.valor); consumir(TokenType.NUMERO);
        consumir(TokenType.PAREN_DER);
        consumir(TokenType.PUNTOYCOMA);
        gestor.eliminarEstudiante(id);
    }
}

// ================== MAIN ==================
public class Main {
    public static void main(String[] args) {
        String entrada = """
                CURSO(1,MATE);
                CURSO(2,FISICA);
                ESTUDIANTE(1,JOSE,1);
                ESTUDIANTE(2,PEDRO,1);
                CURSO(3,ESTADISTICA);
                ESTUDIANTE(3,JUAN,2);
                Buscar_estudiante(1);
                Eliminar_estudiante(2);
                Mostrar_estudiante();
                Mostrar_curso();
                """;

        Gestor gestor = new Gestor();
        AnalizadorSintactico parser = new AnalizadorSintactico(entrada, gestor);
        parser.parse();
    }
}
