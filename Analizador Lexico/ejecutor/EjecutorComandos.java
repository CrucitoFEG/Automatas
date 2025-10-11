package org.project.ejecutor;

import org.project.sistema.SistemaAcademico;
import org.project.analizador.lexico.Token;
import org.project.analizador.lexico.TipoToken;
import java.util.*;

public class EjecutorComandos {
    private SistemaAcademico sistema;
    private List<Token> tokens;
    private int posicionActual;
    private Token tokenActual;

    public EjecutorComandos(SistemaAcademico sistema) {
        this.sistema = sistema;
    }

    public void ejecutar(List<Token> tokens) {
        this.tokens = tokens;
        this.posicionActual = 0;
        avanzar();

        System.out.println("\n=== EJECUTANDO COMANDOS ===");

        while (!esFin()) {
            ejecutarComando();
        }

        System.out.println("=== EJECUCIÓN COMPLETADA ===");
    }

    private void ejecutarComando() {
        if (coincide(TipoToken.CURSO)) {
            ejecutarCrearCurso();
        } else if (coincide(TipoToken.ESTUDIANTE)) {
            ejecutarCrearEstudiante();
        } else if (coincide(TipoToken.BUSCAR_ESTUDIANTE)) {
            ejecutarBuscarEstudiante();
        } else if (coincide(TipoToken.ELIMINAR_ESTUDIANTE)) {
            ejecutarEliminarEstudiante();
        } else if (coincide(TipoToken.MOSTRAR_ESTUDIANTE)) {
            ejecutarMostrarEstudiante();
        } else if (coincide(TipoToken.MOSTRAR_CURSO)) {
            ejecutarMostrarCurso();
        } else {
            avanzar(); // Saltar tokens no reconocidos
        }
    }

    private void ejecutarCrearCurso() {
        consumir(TipoToken.CURSO);
        consumir(TipoToken.PARENTESIS_IZQ);

        Token numeroToken = consumir(TipoToken.NUMERO);
        int numero = Integer.parseInt(numeroToken.getValor());

        consumir(TipoToken.COMA);

        Token nombreToken = consumir(TipoToken.TEXTO);
        String nombre = nombreToken.getValor();

        consumir(TipoToken.PARENTESIS_DER);
        consumir(TipoToken.PUNTO_COMA);

        sistema.crearCurso(numero, nombre);
    }

    private void ejecutarCrearEstudiante() {
        consumir(TipoToken.ESTUDIANTE);
        consumir(TipoToken.PARENTESIS_IZQ);

        Token numeroEstToken = consumir(TipoToken.NUMERO);
        int numeroEst = Integer.parseInt(numeroEstToken.getValor());

        consumir(TipoToken.COMA);

        Token nombreToken = consumir(TipoToken.TEXTO);
        String nombre = nombreToken.getValor();

        consumir(TipoToken.COMA);

        Token numeroCursoToken = consumir(TipoToken.NUMERO);
        int numeroCurso = Integer.parseInt(numeroCursoToken.getValor());

        consumir(TipoToken.PARENTESIS_DER);
        consumir(TipoToken.PUNTO_COMA);

        sistema.crearEstudiante(numeroEst, nombre, numeroCurso);
    }

    private void ejecutarBuscarEstudiante() {
        consumir(TipoToken.BUSCAR_ESTUDIANTE);
        consumir(TipoToken.PARENTESIS_IZQ);

        Token numeroToken = consumir(TipoToken.NUMERO);
        int numero = Integer.parseInt(numeroToken.getValor());

        consumir(TipoToken.PARENTESIS_DER);
        consumir(TipoToken.PUNTO_COMA);

        sistema.buscarEstudiante(numero);
    }

    private void ejecutarEliminarEstudiante() {
        consumir(TipoToken.ELIMINAR_ESTUDIANTE);
        consumir(TipoToken.PARENTESIS_IZQ);

        Token numeroToken = consumir(TipoToken.NUMERO);
        int numero = Integer.parseInt(numeroToken.getValor());

        consumir(TipoToken.PARENTESIS_DER);
        consumir(TipoToken.PUNTO_COMA);

        sistema.eliminarEstudiante(numero);
    }

    private void ejecutarMostrarEstudiante() {
        consumir(TipoToken.MOSTRAR_ESTUDIANTE);
        consumir(TipoToken.PARENTESIS_IZQ);
        consumir(TipoToken.PARENTESIS_DER);
        consumir(TipoToken.PUNTO_COMA);

        sistema.mostrarEstudiantes();
    }

    private void ejecutarMostrarCurso() {
        consumir(TipoToken.MOSTRAR_CURSO);
        consumir(TipoToken.PARENTESIS_IZQ);
        consumir(TipoToken.PARENTESIS_DER);
        consumir(TipoToken.PUNTO_COMA);

        sistema.mostrarCursos();
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

    private Token consumir(TipoToken tipo) {
        if (coincide(tipo)) {
            Token token = tokenActual;
            avanzar();
            return token;
        } else {
            throw new RuntimeException("Error ejecución: Se esperaba " + tipo +
                    " pero se encontró: " +
                    (tokenActual != null ? tokenActual.getValor() : "EOF"));
        }
    }

    private boolean esFin() {
        return tokenActual == null || tokenActual.getTipo() == TipoToken.EOF;
    }


}
