package analizador.lexico;

public class Token {

    private TipoToken tipo;
    private String valor;
    private int linea;

    public Token(TipoToken tipo, String valor, int linea) {
        this.tipo = tipo;
        this.valor = valor;
        this.linea = linea;
    }

    //Getters
    public TipoToken getTipo() {return tipo;}
    public String getValor() {return valor;}
    public int getLinea() {return linea;}

    @Override
    public String toString() {
        return String.format("Linea %d: %-20s -> '%s'", linea, tipo ,valor );
    }

}
