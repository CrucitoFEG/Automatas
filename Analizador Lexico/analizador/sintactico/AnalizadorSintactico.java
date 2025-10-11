package analizador.sintactico;

import analizador.lexico.Token;
import analizador.lexico.TipoToken;
import  java.util.*;

public class AnalizadorSintactico {
    private List<Token> tokens;
    private int posicionActual;
    private Token tokenActual;

    public AnalizadorSintactico() {
        this.posicionActual = 0;
    }

    public boolean validarEstructura(List<Token> tokens) {
        this.tokens = tokens;
        this.posicionActual = 0;
        avanzar();

        try {
            while (!esFin()) {
                if (!validarComando()) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error sintáctico: " + e.getMessage());
            return false;
        }
    }

    private boolean validarComando() {
        if (coincide(TipoToken.CURSO)) {
            return validarCrearCurso();
        } else if (coincide(TipoToken.ESTUDIANTE)) {
            return validarCrearEstudiante();
        } else if (coincide(TipoToken.BUSCAR_ESTUDIANTE)) {
            return validarBuscarEstudiante();
        } else if (coincide(TipoToken.ELIMINAR_ESTUDIANTE)) {
            return validarEliminarEstudiante();
        } else if (coincide(TipoToken.MOSTRAR_ESTUDIANTE)) {
            return validarMostrarEstudiante();
        } else if (coincide(TipoToken.MOSTRAR_CURSO)) {
            return validarMostrarCurso();
        } else {
            errorSintactico("Se esperaba un comando válido");
            return false;
        }
    }

    private boolean validarCrearCurso() {
        // CURSO ( NUMERO , TEXTO ) ;
        consumir(TipoToken.CURSO, "Se esperaba 'CURSO'");
        consumir(TipoToken.PARENTESIS_IZQ, "Se esperaba '(' después de CURSO");
        consumir(TipoToken.NUMERO, "Se esperaba número de curso");
        consumir(TipoToken.COMA, "Se esperaba ',' después del número");
        consumir(TipoToken.TEXTO, "Se esperaba nombre del curso");
        consumir(TipoToken.PARENTESIS_DER, "Se esperaba ')' después de los parámetros");
        consumir(TipoToken.PUNTO_COMA, "Se esperaba ';' al final del comando");
        return true;
    }

    private boolean validarCrearEstudiante() {
        // ESTUDIANTE ( NUMERO , TEXTO , NUMERO ) ;
        consumir(TipoToken.ESTUDIANTE, "Se esperaba 'ESTUDIANTE'");
        consumir(TipoToken.PARENTESIS_IZQ, "Se esperaba '(' después de ESTUDIANTE");
        consumir(TipoToken.NUMERO, "Se esperaba número de estudiante");
        consumir(TipoToken.COMA, "Se esperaba ',' después del número de estudiante");
        consumir(TipoToken.TEXTO, "Se esperaba nombre del estudiante");
        consumir(TipoToken.COMA, "Se esperaba ',' después del nombre");
        consumir(TipoToken.NUMERO, "Se esperaba número de curso");
        consumir(TipoToken.PARENTESIS_DER, "Se esperaba ')' después de los parámetros");
        consumir(TipoToken.PUNTO_COMA, "Se esperaba ';' al final del comando");
        return true;
    }

    private boolean validarBuscarEstudiante() {
        // Buscar_estudiante ( NUMERO ) ;
        consumir(TipoToken.BUSCAR_ESTUDIANTE, "Se esperaba 'Buscar_estudiante'");
        consumir(TipoToken.PARENTESIS_IZQ, "Se esperaba '(' después de Buscar_estudiante");
        consumir(TipoToken.NUMERO, "Se esperaba número de estudiante");
        consumir(TipoToken.PARENTESIS_DER, "Se esperaba ')' después del número");
        consumir(TipoToken.PUNTO_COMA, "Se esperaba ';' al final del comando");
        return true;
    }

    private boolean validarEliminarEstudiante() {
        // Eliminar_estudiante ( NUMERO ) ;
        consumir(TipoToken.ELIMINAR_ESTUDIANTE, "Se esperaba 'Eliminar_estudiante'");
        consumir(TipoToken.PARENTESIS_IZQ, "Se esperaba '(' después de Eliminar_estudiante");
        consumir(TipoToken.NUMERO, "Se esperaba número de estudiante");
        consumir(TipoToken.PARENTESIS_DER, "Se esperaba ')' después del número");
        consumir(TipoToken.PUNTO_COMA, "Se esperaba ';' al final del comando");
        return true;
    }

    private boolean validarMostrarEstudiante() {
        // Mostrar_estudiante ( ) ;
        consumir(TipoToken.MOSTRAR_ESTUDIANTE, "Se esperaba 'Mostrar_estudiante'");
        consumir(TipoToken.PARENTESIS_IZQ, "Se esperaba '(' después de Mostrar_estudiante");
        consumir(TipoToken.PARENTESIS_DER, "Se esperaba ')'");
        consumir(TipoToken.PUNTO_COMA, "Se esperaba ';' al final del comando");
        return true;
    }

    private boolean validarMostrarCurso() {
        // mostrar_curso ( ) ;
        consumir(TipoToken.MOSTRAR_CURSO, "Se esperaba 'mostrar_curso'");
        consumir(TipoToken.PARENTESIS_IZQ, "Se esperaba '(' después de mostrar_curso");
        consumir(TipoToken.PARENTESIS_DER, "Se esperaba ')'");
        consumir(TipoToken.PUNTO_COMA, "Se esperaba ';' al final del comando");
        return true;
    }

    // --- MÉTODOS AUXILIARES ---

    private void avanzar() {
        if (posicionActual < tokens.size()) {
            tokenActual = tokens.get(posicionActual);
            posicionActual++;
        } else {
            tokenActual = null;
        }
    }

    private boolean coincide(TipoToken tipo) {
        return tokenActual != null && tokenActual.getTipo() == tipo;
    }

    private void consumir(TipoToken tipo, String mensajeError) {
        if (coincide(tipo)) {
            avanzar();
        } else {
            errorSintactico(mensajeError + ". Encontrado: " +
                    (tokenActual != null ? tokenActual.getValor() : "EOF"));
        }
    }

    private boolean esFin() {
        return tokenActual == null || tokenActual.getTipo() == TipoToken.EOF;
    }

    private void errorSintactico(String mensaje) {
        int linea = tokenActual != null ? tokenActual.getLinea() : 0;
        throw new RuntimeException("Línea " + linea + ": " + mensaje);
    }
}
