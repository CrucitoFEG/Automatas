package org.project.analizador.lexico;

import java.util.*;
import java.util.regex.*;


public class AnalizadorLexico {
    private int numeroLinea;

    public AnalizadorLexico(){
        this.numeroLinea = 0;
    }

    //Metodo para analizar el codigo que se ejecutara y convertilo a lineas
    public List<Token> analizar (String codigoFuente){
        List<Token> tokens = new ArrayList<>();
        String[] lineas = codigoFuente.split("\n");

        for(String linea : lineas){
            numeroLinea++;
            linea = linea.trim();

            if(linea.isEmpty()||linea.startsWith("//")){
                continue; //Saltar lineas vacias
            }
            //Procesar cada linea
            tokens.addAll(analizarLinea(linea));
        }
        tokens.add(new Token(TipoToken.EOF,"", numeroLinea));
        return tokens;
    }

    //Metodo para analizar cada linea
    private List<Token> analizarLinea(String linea){
        List<Token> tokensLinea = new ArrayList<>();
        int posicion = 0;

        while(posicion < linea.length()){
            char caracterActual = linea.charAt(posicion);

            if(Character.isWhitespace(caracterActual)){
                posicion++;
                continue;
            }

            //Intentar identificar un token
            Token token = extraerToken(linea, posicion);
            if (token != null){
                tokensLinea.add(token);
                posicion += token.getValor().length();
            }else {
                System.out.println("CARACTER NO RECONOCIDO EN LINEA:" + numeroLinea + ": " + caracterActual + "'");
                posicion++;
            }
        }
        return tokensLinea;
    }

    private Token extraerToken(String texto, int posicionInicio){
        String subtexto = texto.substring(posicionInicio);

        //Probar cada patron en orden de prioridad
        for (TipoToken tipo: TipoToken.values()){
            String patron = obtenerPatron(tipo);
            if(patron !=null){
                Pattern p = Pattern.compile(patron);
                Matcher m = p.matcher(subtexto);

                if(m.find() && m.start() == 0){
                    String valor = m.group();
                    return new Token(tipo, valor, numeroLinea);
                }
            }
        }
        return null;
    }

    private String obtenerPatron(TipoToken tipo){
        switch (tipo){
            case CURSO:
                return "(?i)CURSO";
            case ESTUDIANTE:
                return "(?i)ESTUDIANTE";
            case BUSCAR_ESTUDIANTE:
                return "(?i)Buscar_estudiante";
            case ELIMINAR_ESTUDIANTE:
                return "(?i)Eliminar_estudiante";
            case MOSTRAR_ESTUDIANTE:
                return "(?i)Mostrar_estudiante";
            case MOSTRAR_CURSO:
                return "(?i)Mostrar_curso";
            case NUMERO:
                return "\\d+";
            case TEXTO:
                return "[a-zA-Z_][a-zA-Z0-9_]*";
            case PARENTESIS_IZQ:
                return "\\(";
            case PARENTESIS_DER:
                return "\\)";
            case COMA:
                return ",";
            case PUNTO_COMA:
                return ";";
            default:
                return null;
        }
    }

}
