package org.project;

import org.project.UI.InterfazGrafica;
import org.project.ejecutor.Ejecutar;

import javax.swing.*;

import static org.project.util.FileManager.cargarArchivo;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfazGrafica().setVisible(true));
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