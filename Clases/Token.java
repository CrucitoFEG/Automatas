public class Token {
    TokenType tipo;
    String valor;

    public Token(TokenType tipo, String valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    public String toString() {
        return tipo + "('" + valor + "')";
    }
}