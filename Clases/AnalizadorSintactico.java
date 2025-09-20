public class AnalizadorSintactico {
    private AnalizadorLexico lexico;
    private Token actual;
    private Gestor gestor;

    public AnalizadorSintactico(String entrada, Gestor gestor) {
        this.lexico = new AnalizadorLexico(entrada);
        this.gestor = gestor;
        this.actual = lexico.siguienteToken();
    }

    private void consumir(TokenType esperado) {
        if (actual.tipo == esperado) {
            actual = lexico.siguienteToken();
        } else {
            throw new RuntimeException("Se esperaba " + esperado + " pero se encontró " + actual.tipo);
        }
    }

    public void parse() {
        while (actual.tipo != TokenType.FIN) {
            switch (actual.tipo) {
                case CURSO:
                    parseCurso();
                    break;
                case ESTUDIANTE:
                    parseEstudiante();
                    break;
                case MOSTRAR_CURSO:
                    consumir(TokenType.MOSTRAR_CURSO);
                    consumir(TokenType.PARENTESIS_IZQ);
                    consumir(TokenType.PARENTESIS_DER);
                    consumir(TokenType.PUNTOYCOMA);
                    gestor.mostrarCurso();
                    break;
                case MOSTRAR_ESTUDIANTE:
                    consumir(TokenType.MOSTRAR_ESTUDIANTE);
                    consumir(TokenType.PARENTESIS_IZQ);
                    consumir(TokenType.PARENTESIS_DER);
                    consumir(TokenType.PUNTOYCOMA);
                    gestor.mostrarEstudiante();
                    break;
                case BUSCAR_ESTUDIANTE:
                    parseBuscar();
                    break;
                case ELIMINAR_ESTUDIANTE:
                    parseEliminar();
                    break;
                default:
                    throw new RuntimeException("Instrucción no reconocida: " + actual.valor);
            }
        }
    }

    private void parseCurso() {
        consumir(TokenType.CURSO);
        consumir(TokenType.PARENTESIS_IZQ);
        int id = Integer.parseInt(actual.valor);
        consumir(TokenType.NUMERO);
        consumir(TokenType.COMA);
        String nombre = actual.valor;
        consumir(TokenType.IDENTIFICADOR);
        consumir(TokenType.PARENTESIS_DER);
        consumir(TokenType.PUNTOYCOMA);
        gestor.agregarCurso(id, nombre);
    }

    private void parseEstudiante() {
        consumir(TokenType.ESTUDIANTE);
        consumir(TokenType.PARENTESIS_IZQ);
        int id = Integer.parseInt(actual.valor);
        consumir(TokenType.NUMERO);
        consumir(TokenType.COMA);
        String nombre = actual.valor;
        consumir(TokenType.IDENTIFICADOR);
        consumir(TokenType.COMA);
        int cursoId = Integer.parseInt(actual.valor);
        consumir(TokenType.NUMERO);
        consumir(TokenType.PARENTESIS_DER);
        consumir(TokenType.PUNTOYCOMA);
        gestor.agregarEstudiante(id, nombre, cursoId);
    }

    private void parseBuscar() {
        consumir(TokenType.BUSCAR_ESTUDIANTE);
        consumir(TokenType.PARENTESIS_IZQ);
        int id = Integer.parseInt(actual.valor);
        consumir(TokenType.NUMERO);
        consumir(TokenType.PARENTESIS_DER);
        consumir(TokenType.PUNTOYCOMA);
        gestor.buscarEstudiante(id);
    }

    private void parseEliminar() {
        consumir(TokenType.ELIMINAR_ESTUDIANTE);
        consumir(TokenType.PARENTESIS_IZQ);
        int id = Integer.parseInt(actual.valor);
        consumir(TokenType.NUMERO);
        consumir(TokenType.PARENTESIS_DER);
        consumir(TokenType.PUNTOYCOMA);
        gestor.eliminarEstudiante(id);
    }
}
