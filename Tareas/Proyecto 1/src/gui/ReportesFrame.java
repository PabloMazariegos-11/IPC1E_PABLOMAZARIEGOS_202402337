package gui;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.Usuario;
import service.AnimalService;
import service.BitacoraService;
import service.ReporteService;

public class ReportesFrame extends JFrame {
    private Usuario usuarioLogueado;
    private AnimalService animalService;
    private BitacoraService bitacoraService;
    private MenuPrincipalFrame menuPrincipal;

    public ReportesFrame(Usuario usuario, AnimalService animalService, BitacoraService bitacora, MenuPrincipalFrame menu) {
        this.usuarioLogueado = usuario;
        this.animalService = animalService;
        this.bitacoraService = bitacora;
        this.menuPrincipal = menu;

        setTitle("Módulo de Reportes y Bitácoras HTML");
        setSize(450, 320);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel lblTitulo = new JLabel("Generación de Reportes", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setBounds(20, 20, 400, 25);
        panel.add(lblTitulo);

        JButton btnReporteAnimales = new JButton("Generar Reporte Animales (HTML)");
        btnReporteAnimales.setBounds(60, 65, 310, 35);
        panel.add(btnReporteAnimales);

        JButton btnReporteBitacora = new JButton("Generar Reporte Bitácora (HTML)");
        btnReporteBitacora.setBounds(60, 115, 310, 35);
        panel.add(btnReporteBitacora);

        JButton btnExportarCSV = new JButton("Exportar Animales a CSV");
        btnExportarCSV.setBounds(60, 165, 310, 35);
        panel.add(btnExportarCSV);

        JButton btnVolver = new JButton("Volver al Menú");
        btnVolver.setBounds(150, 220, 140, 30);
        panel.add(btnVolver);

        btnReporteAnimales.addActionListener(e -> {
            String fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
            boolean ok = ReporteService.generarReporteAnimalesHTML(animalService, "Reporte_Animales.html");
            if (ok) {
                registrarAccionBitacora(fechaHora, getNombreUsuario(), "REPORTES", "GENERACION", "Reporte HTML de animales generado");
                JOptionPane.showMessageDialog(this, "Reporte de Animales generado con éxito: Reporte_Animales.html");
            } else {
                registrarErrorBitacora(fechaHora, getNombreUsuario(), "REPORTES", "ERROR", "Falló generación HTML", "Error al escribir archivo");
                JOptionPane.showMessageDialog(this, "Error al generar el reporte HTML.");
            }
        });

        btnReporteBitacora.addActionListener(e -> {
            String fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
            boolean ok = ReporteService.generarReporteBitacoraHTML(bitacoraService, "Reporte_Bitacora.html");
            if (ok) {
                registrarAccionBitacora(fechaHora, getNombreUsuario(), "REPORTES", "GENERACION", "Reporte HTML de bitácora generado");
                JOptionPane.showMessageDialog(this, "Reporte de Bitácora generado con éxito: Reporte_Bitacora.html");
            } else {
                registrarErrorBitacora(fechaHora, getNombreUsuario(), "REPORTES", "ERROR", "Falló generación HTML", "Error al escribir archivo");
                JOptionPane.showMessageDialog(this, "Error al generar la bitácora HTML.");
            }
        });

        btnExportarCSV.addActionListener(e -> {
            String fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
            boolean ok = ReporteService.exportarAnimalesCSV(animalService, "Animales.csv");
            if (ok) {
                registrarAccionBitacora(fechaHora, getNombreUsuario(), "PERSISTENCIA", "EXPORTAR", "Exportación de animales a CSV realizada");
                JOptionPane.showMessageDialog(this, "Datos exportados a CSV con éxito: Animales.csv");
            } else {
                registrarErrorBitacora(fechaHora, getNombreUsuario(), "PERSISTENCIA", "ERROR", "Falló exportación CSV", "Error al escribir CSV");
                JOptionPane.showMessageDialog(this, "Error al exportar a CSV.");
            }
        });

        btnVolver.addActionListener(e -> {
            if (menuPrincipal != null) {
                menuPrincipal.setVisible(true);
            }
            dispose();
        });

        add(panel);
    }

    private String getNombreUsuario() {
        if (usuarioLogueado == null) return "Sistema";
        try {
            Method m = usuarioLogueado.getClass().getMethod("getUsuario");
            return (String) m.invoke(usuarioLogueado);
        } catch (Exception e) {
            return "Sistema";
        }
    }

    private void registrarAccionBitacora(String fechaHora, String usuario, String modulo, String tipo, String desc) {
        if (bitacoraService == null) return;
        try {
            for (Method m : bitacoraService.getClass().getMethods()) {
                if (m.getName().equals("registrarAccion") && m.getParameterCount() == 5) {
                    m.invoke(bitacoraService, fechaHora, usuario, modulo, tipo, desc);
                    return;
                }
            }
        } catch (Exception ignored) {}
    }

    private void registrarErrorBitacora(String fechaHora, String usuario, String modulo, String tipo, String desc, String motivo) {
        if (bitacoraService == null) return;
        try {
            for (Method m : bitacoraService.getClass().getMethods()) {
                if (m.getName().equals("registrarError") && m.getParameterCount() == 6) {
                    m.invoke(bitacoraService, fechaHora, usuario, modulo, tipo, desc, motivo);
                    return;
                }
            }
        } catch (Exception ignored) {}
    }
}
