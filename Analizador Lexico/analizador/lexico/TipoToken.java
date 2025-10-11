package analizador.lexico;

public enum TipoToken {
    //Comandos principales
    CURSO,
    ESTUDIANTE,
    BUSCAR_ESTUDIANTE,
    ELIMINAR_ESTUDIANTE,
    MOSTRAR_ESTUDIANTE,
    MOSTRAR_CURSO,

    //Tipos de datos
    NUMERO,
    TEXTO,

    //Simbolos
    PARENTESIS_IZQ,
    PARENTESIS_DER,
    COMA,
    PUNTO_COMA,

    //FINAL DE ARCHIVO
    EOF

}
