import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Gestor gestor = new Gestor();

        System.out.println("=== Analizador Léxico y Sintáctico ===");
        System.out.println("Escribe instrucciones como:");
        System.out.println("CURSO(1,MATE); ESTUDIANTE(1,JOSE,1); MOSTRAR_CURSO();");
        System.out.println("Escribe 'SALIR' para terminar.");

        while (true) {
            System.out.print("\nEntrada: ");
            String entrada = scanner.nextLine();

            if (entrada.equalsIgnoreCase("SALIR")) {
                System.out.println("Programa finalizado.");
                break;
            }

            entrada = entrada.toUpperCase(); // normalizar entrada
            AnalizadorSintactico parser = new AnalizadorSintactico(entrada, gestor);

            try {
                parser.parse(); // analiza y ejecuta
            } catch (Exception e) {
                System.out.println("❌ Error de análisis: " + e.getMessage());
            }
        }
    }
}
