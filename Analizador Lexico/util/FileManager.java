package util;

import javax.swing.*;
import java.io.*;
import java.nio.file.*;


public class FileManager {

    public static String cargarArchivo(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar un archivo txt");

        int resultado= fileChooser.showOpenDialog(null);

        if(resultado==JFileChooser.APPROVE_OPTION){
            File archivoSeleccionado = fileChooser.getSelectedFile();
            try{
                return leerArchivo(archivoSeleccionado.getAbsolutePath());
            }catch (IOException e){
                JOptionPane.showMessageDialog(null, "Error al leer el archivo" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }

    public static String leerArchivo(String ruta) throws IOException{
        return new String(Files.readAllBytes(Paths.get(ruta)));
    }

    public static boolean archivoExiste(String ruta){
        return Files.exists(Paths.get(ruta));
    }

}
