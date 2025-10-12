import UI.InterfazGrafica;
import ejecutor.Ejecutar;
import java.awt.GraphicsEnvironment;
import java.nio.file.*;
import java.io.IOException;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        if (!GraphicsEnvironment.isHeadless()) {
            SwingUtilities.invokeLater(() -> new InterfazGrafica().setVisible(true));
            return;
        }

        System.out.println("Entorno sin display detectado (Codespaces). Ejecutando en modo consola...\n");
        Ejecutar ejecutar = new Ejecutar();

        try {
            // Determinar directorios base
            Path outDir = Paths.get("").toAbsolutePath(); // normalmente .../Analizador Lexico/out
            Path projectDir = outDir.getFileName().toString().equals("out") ? outDir.getParent() : outDir; // .../Analizador Lexico
            Path workspaceDir = projectDir.getParent(); // .../Automatas

            // Probar ubicaciones posibles del archivo de prueba
            Path[] candidatos = new Path[] {
                    projectDir.resolve("Texto de prueba/ArchivoCorrecto.txt"),
                    workspaceDir != null ? workspaceDir.resolve("Texto de prueba/ArchivoCorrecto.txt") : null
            };

            Path archivo = null;
            for (Path c : candidatos) {
                if (c != null && Files.exists(c)) { archivo = c; break; }
            }

            if (archivo == null) {
                throw new IOException("Archivo de prueba no encontrado en rutas esperadas");
            }

            String contenido = new String(Files.readAllBytes(archivo));
            System.out.println("Contenido del archivo de prueba:\n" + contenido + "\n");
            ejecutar.probarSistemaCompleto(contenido);
        } catch (IOException e) {
            System.err.println("No se pudo leer el archivo de prueba: " + e.getMessage());
            System.err.println("Ubicación esperada: Texto de prueba/ArchivoCorrecto.txt");
        }
//        Ejecutar ejecutar = new Ejecutar();
//
//        System.out.println("PRUEBA DEL SISTEMA ACADÉMICO\n");
//        //Carga archivo y lee el archivo
//        String contenido = cargarArchivo();
//        System.out.println("Contenido del archvio: "+contenido);
//        //Probar sistema completo
//        ejecutar.probarSistemaCompleto(contenido);
    }


}