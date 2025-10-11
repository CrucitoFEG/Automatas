package org.project.UI;

import org.project.analizador.lexico.AnalizadorLexico;
import org.project.analizador.lexico.Token;
import org.project.analizador.sintactico.AnalizadorSintactico;
import org.project.util.FileManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class InterfazGrafica extends JFrame {

    private JTextPane areaResultado;
    private JLabel etiquetaEstado;
    private String contenidoArchivo = "";

    public InterfazGrafica() {
        setTitle("Analizador Académico - Grupo 3 - Autómatas");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal blanco
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Panel superior con botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelBotones.setBackground(Color.WHITE);

        JButton btnCargar = new JButton("📂 Cargar");
        JButton btnEvaluar = new JButton("✅ Evaluar");
        JButton btnParticipantes = new JButton("👥 Participantes");

        panelBotones.add(btnCargar);
        panelBotones.add(btnEvaluar);
        panelBotones.add(btnParticipantes);

        // Panel derecho con logo
        JLabel logo = new JLabel(new ImageIcon("logo.png")); // Asegúrate que logo.png sea 100x100
        JPanel panelLogo = new JPanel(new BorderLayout());
        panelLogo.setBackground(Color.WHITE);
        panelLogo.add(logo, BorderLayout.EAST);

        // Área de resultados
        areaResultado = new JTextPane();
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(areaResultado);

        // Etiqueta de estado
        etiquetaEstado = new JLabel("Estado: Esperando archivo...");
        etiquetaEstado.setForeground(Color.DARK_GRAY);

        // Composición
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(Color.WHITE);
        panelSuperior.add(panelBotones, BorderLayout.CENTER);
        panelSuperior.add(panelLogo, BorderLayout.EAST);

        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(scroll, BorderLayout.CENTER);
        panelPrincipal.add(etiquetaEstado, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);

        // Acciones
        btnCargar.addActionListener(this::accionCargar);
        btnEvaluar.addActionListener(this::accionEvaluar);
        btnParticipantes.addActionListener(this::mostrarParticipantes);
    }

    private void accionCargar(ActionEvent e) {
        contenidoArchivo = FileManager.cargarArchivo();
        if (contenidoArchivo != null) {
            etiquetaEstado.setText("Archivo cargado correctamente.");
            areaResultado.setText(contenidoArchivo);
        } else {
            etiquetaEstado.setText("Error al cargar archivo.");
        }
    }

    private void accionEvaluar(ActionEvent e) {
        if (contenidoArchivo == null || contenidoArchivo.isEmpty()) {
            etiquetaEstado.setText("No hay contenido para evaluar.");
            return;
        }

        try {
            AnalizadorLexico lexico = new AnalizadorLexico();
            List<Token> tokens = lexico.analizar(contenidoArchivo);

            AnalizadorSintactico sintactico = new AnalizadorSintactico();
            boolean valido = sintactico.validarEstructura(tokens);

            StringBuilder resultado = new StringBuilder();
            for (Token token : tokens) {
                resultado.append(token.toString()).append("\n");
            }

            resultado.append(valido ? "\n✓ Estructura válida" : "\n✗ Estructura inválida");
            areaResultado.setText(resultado.toString());
            etiquetaEstado.setText("Evaluación completada.");
        } catch (Exception ex) {
            areaResultado.setText("Error durante la evaluación:\n" + ex.getMessage());
            etiquetaEstado.setText("Error en evaluación.");
        }
    }

    private void mostrarParticipantes(ActionEvent e) {
        String participantes = """
                🧑‍💻 Participantes del Grupo:
                ----------------------------------------
                Carnet           Nombre
                7690-23-5339     Cruz Francisco Estrada Gregorio
                7690-23-25069    Brandon Tomas Morales Ixcoy
                7690-23-22940    Nery Geovany Osorio Tecu
                7690-18-24917    José Fernando Pérez Sipaque
                7690-15-3698     Kevin Rai Salazar Pérez
                """;
        areaResultado.setText(participantes);
        etiquetaEstado.setText("Mostrando participantes.");
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new InterfazGrafica().setVisible(true));

    }
}

