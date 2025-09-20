public class AnalizadorLexico {
    private String entrada;
    private int pos;

    public AnalizadorLexico(String entrada) {
        this.entrada = entrada;
        this.pos = 0;
    }

    public Token siguienteToken() {
        while (pos < entrada.length() && Character.isWhitespace(entrada.charAt(pos))) pos++;

        if (pos >= entrada.length()) return new Token(TokenType.FIN, "");

        char actual = entrada.charAt(pos);

        if (actual == '(') return avanzar(TokenType.PARENTESIS_IZQ, "(");
        if (actual == ')') return avanzar(TokenType.PARENTESIS_DER, ")");
        if (actual == ',') return avanzar(TokenType.COMA, ",");
        if (actual == ';') return avanzar(TokenType.PUNTOYCOMA, ";");

        if (Character.isDigit(actual)) {
            StringBuilder numero = new StringBuilder();
            while (pos < entrada.length() && Character.isDigit(entrada.charAt(pos))) {
                numero.append(entrada.charAt(pos++));
            }
            return new Token(TokenType.NUMERO, numero.toString());
        }

        if (Character.isLetter(actual) || actual == '_') {
            StringBuilder palabra = new StringBuilder();
            while (pos < entrada.length() &&
                   (Character.isLetterOrDigit(entrada.charAt(pos)) || entrada.charAt(pos) == '_')) {
                palabra.append(entrada.charAt(pos++));
            }

            String lexema = palabra.toString();

            switch (lexema) {
                case "CURSO": return new Token(TokenType.CURSO, lexema);
                case "ESTUDIANTE": return new Token(TokenType.ESTUDIANTE, lexema);
                case "MOSTRAR_CURSO": return new Token(TokenType.MOSTRAR_CURSO, lexema);
                case "MOSTRAR_ESTUDIANTE": return new Token(TokenType.MOSTRAR_ESTUDIANTE, lexema);
                case "BUSCAR_ESTUDIANTE": return new Token(TokenType.BUSCAR_ESTUDIANTE, lexema);
                case "ELIMINAR_ESTUDIANTE": return new Token(TokenType.ELIMINAR_ESTUDIANTE, lexema);
                default: return new Token(TokenType.IDENTIFICADOR, lexema);
            }
        }

        throw new RuntimeException("Carácter inesperado: " + actual);
    }

    private Token avanzar(TokenType tipo, String valor) {
        pos++;
        return new Token(tipo, valor);
    }
}