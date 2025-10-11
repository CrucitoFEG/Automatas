package org.project.ejecutor;

import org.project.analizador.lexico.AnalizadorLexico;
import org.project.analizador.lexico.Token;
import org.project.analizador.sintactico.AnalizadorSintactico;
import org.project.sistema.SistemaAcademico;

import java.util.List;

public class Ejecutar {

    public static void probarSistemaCompleto(String codigoCompleto) {
        System.out.println("\n--- Prueba del Sistema COMPLETO ---");

//        String codigoCompleto =
//                "CURSO(1,MATEMATICAS);\n" +
//                        "CURSO(2,FISICA);\n" +
//                        "ESTUDIANTE(1,JOSE,1);\n" +
//                        "ESTUDIANTE(2,MARIA,2);\n" +
//                        "ESTUDIANTE(3,CARLOS,1);\n" +
//                        "Buscar_estudiante(1);\n" +
//                        "Eliminar_estudiante(2);\n" +
//                        "Mostrar_estudiante();\n" +
//                        "mostrar_curso();";

        // Crear sistema
        SistemaAcademico sistema = new SistemaAcademico();

        // Analizar léxicamente
        AnalizadorLexico analizadorLexico = new AnalizadorLexico();
        List<Token> tokens = analizadorLexico.analizar(codigoCompleto);

        // Validar sintácticamente
        AnalizadorSintactico analizadorSintactico = new AnalizadorSintactico();
        boolean esValido = analizadorSintactico.validarEstructura(tokens);

        if (esValido) {
            System.out.println("✓ Análisis sintáctico: VÁLIDO");

            // Ejecutar comandos
            EjecutorComandos ejecutor = new EjecutorComandos(sistema);
            ejecutor.ejecutar(tokens);
        } else {
            System.out.println("✗ Análisis sintáctico: INVÁLIDO - No se ejecutarán comandos");
        }
    }

}
