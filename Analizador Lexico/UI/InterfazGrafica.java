package UI;

import analizador.lexico.AnalizadorLexico;
import analizador.lexico.Token;
import analizador.sintactico.AnalizadorSintactico;
import util.FileManager;
import sistema.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class InterfazGrafica extends JFrame {

    private JTextPane areaRegistros;
    private JTextPane areaCursos;
    private JTextPane areaEstudiantes;
    private JLabel etiquetaEstado;
    private String contenidoArchivo = "";
    private sistema.SistemaAcademico sistema;

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
    // Quitar botón Mostrar Estudiantes
    // Agregar botón para añadir tokens manualmente
    JButton btnAgregarToken = new JButton("➕ Agregar Token");
    // Agregar botón para guardar archivo
    JButton btnGuardarArchivo = new JButton("💾 Guardar Archivo");
    JButton btnMostrarCursos = new JButton("📘 Mostrar Cursos");

    panelBotones.add(btnCargar);
    panelBotones.add(btnEvaluar);
    panelBotones.add(btnParticipantes);
    panelBotones.add(btnAgregarToken);
    panelBotones.add(btnMostrarCursos);
    panelBotones.add(btnGuardarArchivo);

        // Panel derecho con logo
    JLabel logo = new JLabel(new ImageIcon("UI/logo.png")); // Asegúrate que logo.png sea 100x100
        JPanel panelLogo = new JPanel(new BorderLayout());
        panelLogo.setBackground(Color.WHITE);
        panelLogo.add(logo, BorderLayout.EAST);

        // Paneles de área
        areaRegistros = new JTextPane();
        areaRegistros.setEditable(false);
        areaRegistros.setFont(new Font("Consolas", Font.PLAIN, 14));
        areaRegistros.setContentType("text/html");
        JScrollPane scrollRegistros = new JScrollPane(areaRegistros);

        areaCursos = new JTextPane();
        areaCursos.setEditable(false);
        areaCursos.setFont(new Font("Consolas", Font.PLAIN, 14));
        areaCursos.setContentType("text/html");
        JScrollPane scrollCursos = new JScrollPane(areaCursos);

        areaEstudiantes = new JTextPane();
        areaEstudiantes.setEditable(false);
        areaEstudiantes.setFont(new Font("Consolas", Font.PLAIN, 14));
        areaEstudiantes.setContentType("text/html");
        JScrollPane scrollEstudiantes = new JScrollPane(areaEstudiantes);

        // Etiqueta de estado
        etiquetaEstado = new JLabel("Estado: Esperando archivo...");
        etiquetaEstado.setForeground(Color.DARK_GRAY);

        // Panel central con GridLayout (3 columnas)
        JPanel panelCentral = new JPanel(new GridLayout(1, 3, 10, 0));
        panelCentral.setBackground(Color.WHITE);
        panelCentral.add(scrollRegistros);
        panelCentral.add(scrollCursos);
        panelCentral.add(scrollEstudiantes);

        // Composición
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(Color.WHITE);
        panelSuperior.add(panelBotones, BorderLayout.CENTER);
        panelSuperior.add(panelLogo, BorderLayout.EAST);

        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.add(etiquetaEstado, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);

    // Inicializar sistema académico
    sistema = new sistema.SistemaAcademico();

    // Acciones
    btnCargar.addActionListener(this::accionCargar);
    btnEvaluar.addActionListener(this::accionEvaluar);
    btnParticipantes.addActionListener(this::mostrarParticipantes);
    btnAgregarToken.addActionListener(this::accionAgregarToken);
    btnMostrarCursos.addActionListener(this::mostrarCursos);
    btnGuardarArchivo.addActionListener(this::accionGuardarArchivo);
    }

    private void accionCargar(ActionEvent e) {
        contenidoArchivo = FileManager.cargarArchivo();
        if (contenidoArchivo != null) {
            etiquetaEstado.setText("Archivo cargado correctamente.");
            areaRegistros.setText("<b>Archivo cargado:</b><br><pre>" + contenidoArchivo.replace("<", "&lt;").replace(">", "&gt;") + "</pre>");
        } else {
            etiquetaEstado.setText("Error al cargar archivo.");
            areaRegistros.setText("");
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
            resultado.append("<b>Tokens detectados:</b><br>");
            for (Token token : tokens) {
                resultado.append(token.toString()).append("<br>");
            }
            resultado.append(valido ? "<br><span style='color:green;'>&#10003; Estructura válida</span>" : "<br><span style='color:red;'>&#10007; Estructura inválida</span>");

            // Procesar instrucciones si la estructura es válida
            if (valido) {
                for (int i = 0; i < tokens.size(); i++) {
                    Token t = tokens.get(i);
                    if (t.getTipo().name().equals("CURSO")) {
                        int num = Integer.parseInt(tokens.get(i+2).getValor());
                        String nombre = tokens.get(i+4).getValor();
                        resultado.append("<br>").append(sistema.crearCurso(num, nombre));
                    } else if (t.getTipo().name().equals("ESTUDIANTE")) {
                        int num = Integer.parseInt(tokens.get(i+2).getValor());
                        String nombre = tokens.get(i+4).getValor();
                        int numCurso = Integer.parseInt(tokens.get(i+6).getValor());
                        resultado.append("<br>").append(sistema.crearEstudiante(num, nombre, numCurso));
                    } else if (t.getTipo().name().equals("BUSCAR_ESTUDIANTE")) {
                        int num = Integer.parseInt(tokens.get(i+2).getValor());
                        resultado.append("<br>").append(sistema.buscarEstudiante(num));
                    } else if (t.getTipo().name().equals("ELIMINAR_ESTUDIANTE")) {
                        int num = Integer.parseInt(tokens.get(i+2).getValor());
                        resultado.append("<br>").append(sistema.eliminarEstudiante(num));
                    }
                }
            }
            areaRegistros.setText(resultado.toString());
            areaCursos.setText(sistema.mostrarCursos());
            areaEstudiantes.setText(sistema.mostrarEstudiantes());
            etiquetaEstado.setText("Evaluación completada.");
        } catch (Exception ex) {
            areaRegistros.setText("<span style='color:red;'>Error durante la evaluación:<br>" + ex.getMessage() + "</span>");
            areaCursos.setText("");
            areaEstudiantes.setText("");
            etiquetaEstado.setText("Error en evaluación.");
        }
    }

    // Acción para agregar token manualmente
    private void accionAgregarToken(ActionEvent e) {
        String ejemplo = "Ejemplo de token: CURSO(4,QUIMICA);";
        String token = JOptionPane.showInputDialog(this, "Ingrese el token a agregar:\n" + ejemplo, "Agregar Token", JOptionPane.PLAIN_MESSAGE);
        if (token != null && !token.trim().isEmpty()) {
            // Validar el token usando el analizador léxico y sintáctico
            AnalizadorLexico lexico = new AnalizadorLexico();
            List<Token> tokens = lexico.analizar(token);
            AnalizadorSintactico sintactico = new AnalizadorSintactico();
            boolean valido = sintactico.validarEstructura(tokens);
            if (valido) {
                // Agregar el token al contenido actual
                contenidoArchivo += "\n" + token;
                // Mostrar el contenido actualizado en el área de lectura
                areaRegistros.setText("<b>Archivo cargado:</b><br><pre>" + contenidoArchivo.replace("<", "&lt;").replace(">", "&gt;") + "</pre><br><span style='color:green;'>Token agregado correctamente:</span> " + token);
                etiquetaEstado.setText("Token agregado y validado.");
            } else {
                JOptionPane.showMessageDialog(this, "El token ingresado no es válido.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Acción para guardar el archivo cargado con los cambios y análisis
    private void accionGuardarArchivo(ActionEvent e) {
        if (contenidoArchivo == null || contenidoArchivo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay contenido para guardar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Concatenar el análisis actual
        String analisis = areaRegistros.getText();
        String contenidoFinal = contenidoArchivo + "\n\n--- ANALISIS ---\n" + analisis.replaceAll("<[^>]+>", "");
        // Guardar en el mismo archivo
        boolean guardado = FileManager.guardarArchivo(contenidoFinal);
        if (guardado) {
            etiquetaEstado.setText("Archivo guardado correctamente.");
            JOptionPane.showMessageDialog(this, "Archivo guardado con éxito.", "Guardado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            etiquetaEstado.setText("Error al guardar archivo.");
            JOptionPane.showMessageDialog(this, "No se pudo guardar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarCursos(ActionEvent e) {
    areaCursos.setText(sistema.mostrarCursos());
    etiquetaEstado.setText("Mostrando cursos.");
    }
    

    private void mostrarParticipantes(ActionEvent e) {
    String participantes =
        "<html>"
        + "<h2>🧑‍💻 Participantes del Grupo</h2>"
        + "<table border='1' cellpadding='5' cellspacing='0' style='border-collapse:collapse;'>"
        + "<tr style='background-color:#f0f0f0;'><th>Carnet</th><th>Nombre</th></tr>"
        + "<tr><td>7690-23-5339</td><td>Cruz Francisco Estrada Gregorio</td></tr>"
        + "<tr><td>7690-23-25069</td><td>Brandon Tomas Morales Ixcoy</td></tr>"
        + "<tr><td>7690-23-22940</td><td>Nery Geovany Osorio Tecu</td></tr>"
        + "<tr><td>7690-18-24917</td><td>José Fernando Pérez Sipaque</td></tr>"
        + "<tr><td>7690-15-3698</td><td>Kevin Rai Salazar Pérez</td></tr>"
        + "</table></html>";
    areaRegistros.setContentType("text/html");
    areaRegistros.setText(participantes);
    etiquetaEstado.setText("Mostrando participantes.");
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new InterfazGrafica().setVisible(true));

    }
}

