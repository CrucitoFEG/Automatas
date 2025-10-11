package util;

import javax.swing.*;
import java.io.*;
import java.nio.file.*;


public class FileManager {

    private static File ultimoArchivoSeleccionado = null;

    public static String cargarArchivo(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar un archivo txt");

        int resultado= fileChooser.showOpenDialog(null);

        if(resultado==JFileChooser.APPROVE_OPTION){
            File archivoSeleccionado = fileChooser.getSelectedFile();
            ultimoArchivoSeleccionado = archivoSeleccionado;
            try{
                return leerArchivo(archivoSeleccionado.getAbsolutePath());
            }catch (IOException e){
                JOptionPane.showMessageDialog(null, "Error al leer el archivo" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }

    // Método para guardar el contenido en el último archivo seleccionado
    public static boolean guardarArchivo(String contenido) {
        if (ultimoArchivoSeleccionado == null) {
            JOptionPane.showMessageDialog(null, "No se ha cargado ningún archivo.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try (FileWriter writer = new FileWriter(ultimoArchivoSeleccionado)) {
            writer.write(contenido);
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static String leerArchivo(String ruta) throws IOException{
        return new String(Files.readAllBytes(Paths.get(ruta)));
    }

    public static boolean archivoExiste(String ruta){
        return Files.exists(Paths.get(ruta));
    }

}
