package UI;

import analizador.lexico.AnalizadorLexico;
import analizador.lexico.Token;
import analizador.sintactico.AnalizadorSintactico;
import util.FileManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class InterfazGrafica extends JFrame {
    private String rutaArchivoActual = null;
    // Función auxiliar para evaluar texto (una línea o varias)
    private String evaluarTexto(String texto) {
        try {
            AnalizadorLexico lexico = new AnalizadorLexico();
            List<Token> tokens = lexico.analizar(texto);
            AnalizadorSintactico sintactico = new AnalizadorSintactico();
            boolean valido = sintactico.validarEstructura(tokens);
            StringBuilder resultado = new StringBuilder();
            resultado.append("<b>Tokens detectados:</b><br>");
            for (Token token : tokens) {
                resultado.append(token.toString()).append("<br>");
            }
            resultado.append(valido ? "<br><span style='color:green;'>&#10003; Estructura válida</span>" : "<br><span style='color:red;'>&#10007; Estructura inválida</span>");
            if (valido) {
                for (int i = 0; i < tokens.size(); i++) {
                    Token t = tokens.get(i);
                    String tipo = t.getTipo().name();
                    if (tipo.equals("CURSO")) {
                        int num = Integer.parseInt(tokens.get(i+2).getValor());
                        String nombre = tokens.get(i+4).getValor();
                        resultado.append("<br><b>Curso creado:</b> ").append(sistema.crearCurso(num, nombre));
                    } else if (tipo.equals("ESTUDIANTE")) {
                        int num = Integer.parseInt(tokens.get(i+2).getValor());
                        String nombre = tokens.get(i+4).getValor();
                        int numCurso = Integer.parseInt(tokens.get(i+6).getValor());
                        resultado.append("<br><b>Estudiante creado:</b> ").append(sistema.crearEstudiante(num, nombre, numCurso));
                    } else if (tipo.equals("BUSCAR_ESTUDIANTE")) {
                        int num = Integer.parseInt(tokens.get(i+2).getValor());
                        resultado.append("<br><b>Buscar estudiante:</b> ").append(sistema.buscarEstudiante(num));
                    } else if (tipo.equals("ELIMINAR_ESTUDIANTE")) {
                        int num = Integer.parseInt(tokens.get(i+2).getValor());
                        resultado.append("<br><b>Eliminar estudiante:</b> ").append(sistema.eliminarEstudiante(num));
                    } else if (tipo.equals("BUSCAR_CURSO")) {
                        int num = Integer.parseInt(tokens.get(i+2).getValor());
                        resultado.append("<br><b>Buscar curso:</b> ").append(sistema.buscarCursoString(num));
                    } else if (tipo.equals("ELIMINAR_CURSO")) {
                        int num = Integer.parseInt(tokens.get(i+2).getValor());
                        resultado.append("<br><b>Eliminar curso:</b> ").append(sistema.eliminarCurso(num));
                    } else if (tipo.equals("MOSTRAR_CURSOS") || tipo.equals("MOSTRAR_CURSO")) {
                        resultado.append("<br><b>Listado de cursos:</b><br>").append(sistema.mostrarCursos());
                    } else if (tipo.equals("MOSTRAR_ESTUDIANTES") || tipo.equals("MOSTRAR_ESTUDIANTE") || tipo.equals("MOSTRAR_ESTUDIANTE_ID")) {
                        resultado.append("<br><b>Listado de estudiantes:</b><br>").append(sistema.mostrarEstudiantes());
                    } else if (tipo.equals("PARTICIPANTES")) {
                        resultado.append("<br><b>Participantes:</b><br>").append(getParticipantesHtml());
                    }
                }
            }
            return resultado.toString();
        } catch (Exception ex) {
            return "<span style='color:red;'>Error durante la evaluación:<br>" + ex.getMessage() + "</span>";
        }
    }

    private JTextPane areaRegistros;
    private JTextPane areaRespuestas;
    private JLabel etiquetaEstado;
    private String contenidoArchivo = "";
    private sistema.SistemaAcademico sistema;

    public InterfazGrafica() {
        setTitle("Analizador Académico - Grupo 3 - Autómatas");
        setSize(1200, 700);
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
    JButton btnEvaluarLinea = new JButton("📄 Evaluar Línea");
    JButton btnGuardarArchivo = new JButton("💾 Guardar Archivo");
    JButton btnAyuda = new JButton("❓ Ayuda");

    panelBotones.add(btnCargar);
    panelBotones.add(btnEvaluar);
    panelBotones.add(btnEvaluarLinea);
    panelBotones.add(btnGuardarArchivo);
    panelBotones.add(btnAyuda);

        // Panel derecho con logo
    JLabel logo = new JLabel(new ImageIcon("UI/logo.png")); // Asegúrate que logo.png sea 100x100
        JPanel panelLogo = new JPanel(new BorderLayout());
        panelLogo.setBackground(Color.WHITE);
        panelLogo.add(logo, BorderLayout.EAST);

    // Paneles de área
    areaRegistros = new JTextPane();
    areaRegistros.setEditable(true);
    areaRegistros.setFont(new Font("Consolas", Font.PLAIN, 14));
    areaRegistros.setContentType("text/plain");
    JScrollPane scrollRegistros = new JScrollPane(areaRegistros);

    areaRespuestas = new JTextPane();
    areaRespuestas.setEditable(false);
    areaRespuestas.setFont(new Font("Consolas", Font.PLAIN, 14));
    areaRespuestas.setContentType("text/html");
    JScrollPane scrollRespuestas = new JScrollPane(areaRespuestas);

        // Etiqueta de estado
        etiquetaEstado = new JLabel("Estado: Esperando archivo...");
        etiquetaEstado.setForeground(Color.DARK_GRAY);

    // Panel central con GridLayout (2 columnas)
    JPanel panelCentral = new JPanel(new GridLayout(1, 2, 10, 0));
    panelCentral.setBackground(Color.WHITE);
    panelCentral.add(scrollRegistros);
    panelCentral.add(scrollRespuestas);

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
    btnEvaluarLinea.addActionListener(this::accionEvaluarLinea);
    btnGuardarArchivo.addActionListener(this::accionGuardarArchivo);
    btnAyuda.addActionListener(this::mostrarAyuda);

    }
    
    // Acción para mostrar ayuda de tokens disponibles
    private void mostrarAyuda(ActionEvent e) {
        String ayuda = "<html><h2>Tokens disponibles</h2>"
            + "<ul>"
            + "<li><b>CURSO(num, nombre);</b> - Crear curso</li>"
            + "<li><b>ESTUDIANTE(num, nombre, numCurso);</b> - Crear estudiante</li>"
            + "<li><b>BUSCAR_ESTUDIANTE(num);</b> - Buscar estudiante por ID</li>"
            + "<li><b>ELIMINAR_ESTUDIANTE(num);</b> - Eliminar estudiante por ID</li>"
            + "<li><b>MOSTRAR_CURSOS();</b> o <b>mostrar_curso();</b> - Listar cursos</li>"
            + "<li><b>MOSTRAR_ESTUDIANTES();</b> - Listar estudiantes</li>"
            + "<li><b>PARTICIPANTES();</b> - Mostrar participantes del grupo</li>"
            + "</ul></html>";
        areaRespuestas.setContentType("text/html");
        areaRespuestas.setText(ayuda);
        etiquetaEstado.setText("Mostrando ayuda de tokens.");
    }
    

    private void accionCargar(ActionEvent e) {
        Object[] resultado = FileManager.cargarArchivoConRuta();
        contenidoArchivo = resultado != null ? (String) resultado[0] : null;
        rutaArchivoActual = resultado != null ? (String) resultado[1] : null;
        if (contenidoArchivo != null) {
            etiquetaEstado.setText("Archivo cargado correctamente.");
            areaRegistros.setText(contenidoArchivo);
        } else {
            etiquetaEstado.setText("Error al cargar archivo.");
            areaRegistros.setText("");
            rutaArchivoActual = null;
        }
    }

    private void accionEvaluar(ActionEvent e) {
        String texto = areaRegistros.getText();
        if (texto == null || texto.isEmpty()) {
            etiquetaEstado.setText("No hay contenido para evaluar.");
            return;
        }
        areaRespuestas.setText(evaluarTexto(texto));
        etiquetaEstado.setText("Evaluación completada.");
    }
    private String getParticipantesHtml() {
        return "<table border='1' cellpadding='5' cellspacing='0' style='border-collapse:collapse;'>"
            + "<tr style='background-color:#f0f0f0;'><th>Carnet</th><th>Nombre</th></tr>"
            + "<tr><td>7690-23-5339</td><td>Cruz Francisco Estrada Gregorio</td></tr>"
            + "<tr><td>7690-23-25069</td><td>Brandon Tomas Morales Ixcoy</td></tr>"
            + "<tr><td>7690-23-22940</td><td>Nery Geovany Osorio Tecu</td></tr>"
            + "<tr><td>7690-18-24917</td><td>José Fernando Pérez Sipaque</td></tr>"
            + "<tr><td>7690-15-3698</td><td>Kevin Rai Salazar Pérez</td></tr>"
            + "</table>";
    }
    
    // Evaluar solo la línea seleccionada
    private void accionEvaluarLinea(ActionEvent e) {
        String texto = areaRegistros.getSelectedText();
        if (texto == null || texto.isEmpty()) {
            // Si no hay selección, tomar la línea donde está el cursor
            int caret = areaRegistros.getCaretPosition();
            javax.swing.text.Document doc = areaRegistros.getDocument();
            int start = caret, end = caret;
            try {
                while (start > 0 && doc.getText(start - 1, 1).charAt(0) != '\n') start--;
                while (end < doc.getLength() && doc.getText(end, 1).charAt(0) != '\n') end++;
                texto = doc.getText(start, end - start);
            } catch (javax.swing.text.BadLocationException ex) {
                areaRespuestas.setText("<span style='color:red;'>Error al obtener la línea: " + ex.getMessage() + "</span>");
                etiquetaEstado.setText("Error en evaluación de línea.");
                return;
            }
        }
        if (texto == null || texto.trim().isEmpty()) {
            etiquetaEstado.setText("No hay línea seleccionada para evaluar.");
            return;
        }
        areaRespuestas.setText(evaluarTexto(texto.trim()));
        etiquetaEstado.setText("Evaluación de línea completada.");
    }


    // Acción para guardar el archivo cargado con los cambios y análisis
    private void accionGuardarArchivo(ActionEvent e) {
        String comandos = areaRegistros.getText();
        String analisis = areaRespuestas.getText();
        String contenidoFinal = comandos + "\n\n--- ANALISIS ---\n" + analisis.replaceAll("<[^>]+>", "");
        boolean guardado;
        if (rutaArchivoActual == null) {
            // Guardar como nuevo
            rutaArchivoActual = FileManager.guardarArchivoComo(contenidoFinal);
            guardado = rutaArchivoActual != null;
        } else {
            // Sobrescribir archivo abierto
            guardado = FileManager.guardarArchivoEnRuta(rutaArchivoActual, contenidoFinal);
        }
        if (guardado) {
            etiquetaEstado.setText("Archivo guardado correctamente.");
            JOptionPane.showMessageDialog(this, "Archivo guardado con éxito.", "Guardado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            etiquetaEstado.setText("Error al guardar archivo.");
            JOptionPane.showMessageDialog(this, "No se pudo guardar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }    

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new InterfazGrafica().setVisible(true));

    }
}

