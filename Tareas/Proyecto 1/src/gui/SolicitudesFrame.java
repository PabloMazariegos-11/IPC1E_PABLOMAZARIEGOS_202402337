package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.Solicitud;
import model.Usuario;
import service.AdoptanteService;
import service.AnimalService;
import service.BitacoraService;
import service.SolicitudService;

public class SolicitudesFrame extends JFrame {
    private Usuario usuarioLogueado;
    private SolicitudService solicitudService;
    private AnimalService animalService;
    private AdoptanteService adoptanteService;
    private BitacoraService bitacoraService;
    private MenuPrincipalFrame menuPrincipal;

    private JTextField txtCodigo, txtCodAnimal, txtCodAdoptante;
    private JTable tablaSolicitudes;
    private DefaultTableModel modelTabla;

    public SolicitudesFrame(Usuario usuarioLogueado, SolicitudService solicitudService, AnimalService animalService, AdoptanteService adoptanteService, BitacoraService bitacoraService, MenuPrincipalFrame menuPrincipal) {
        this.usuarioLogueado = usuarioLogueado;
        this.solicitudService = solicitudService;
        this.animalService = animalService;
        this.adoptanteService = adoptanteService;
        this.bitacoraService = bitacoraService;
        this.menuPrincipal = menuPrincipal;

        setTitle("Gestión de Solicitudes de Adopción");
        setSize(780, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
        actualizarTabla();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel lblCodigo = new JLabel("Cód. Solicitud (S-001):");
        lblCodigo.setBounds(20, 20, 140, 25);
        panel.add(lblCodigo);

        txtCodigo = new JTextField();
        txtCodigo.setBounds(160, 20, 140, 25);
        panel.add(txtCodigo);

        JLabel lblCodAnimal = new JLabel("Cód. Animal:");
        lblCodAnimal.setBounds(20, 55, 140, 25);
        panel.add(lblCodAnimal);

        txtCodAnimal = new JTextField();
        txtCodAnimal.setBounds(160, 55, 140, 25);
        panel.add(txtCodAnimal);

        JLabel lblCodAdoptante = new JLabel("Cód. Adoptante:");
        lblCodAdoptante.setBounds(20, 90, 140, 25);
        panel.add(lblCodAdoptante);

        txtCodAdoptante = new JTextField();
        txtCodAdoptante.setBounds(160, 90, 140, 25);
        panel.add(txtCodAdoptante);

        JButton btnCrear = new JButton("Crear Solicitud");
        btnCrear.setBounds(20, 135, 280, 30);
        panel.add(btnCrear);

        JButton btnAprobar = new JButton("Aprobar Solicitud");
        btnAprobar.setBounds(20, 175, 280, 30);
        panel.add(btnAprobar);

        JButton btnRechazar = new JButton("Rechazar Solicitud");
        btnRechazar.setBounds(20, 215, 280, 30);
        panel.add(btnRechazar);

        JButton btnVolver = new JButton("Volver al Menú");
        btnVolver.setBounds(20, 390, 280, 30);
        panel.add(btnVolver);

        String[] columnas = {"Código", "Animal", "Adoptante", "Fecha", "Estado"};
        modelTabla = new DefaultTableModel(columnas, 0);
        tablaSolicitudes = new JTable(modelTabla);
        JScrollPane scroll = new JScrollPane(tablaSolicitudes);
        scroll.setBounds(320, 20, 430, 400);
        panel.add(scroll);

        btnCrear.addActionListener(e -> crearSolicitud());
        btnAprobar.addActionListener(e -> procesarEstado("APROBADA"));
        btnRechazar.addActionListener(e -> procesarEstado("RECHAZADA"));
        btnVolver.addActionListener(e -> {
            menuPrincipal.setVisible(true);
            dispose();
        });

        add(panel);
    }

    private void crearSolicitud() {
        String cod = txtCodigo.getText().trim();
        String codAnimal = txtCodAnimal.getText().trim();
        String codAdoptante = txtCodAdoptante.getText().trim();
        String fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
        String fechaSimple = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        if (!cod.startsWith("S-") || codAnimal.isEmpty() || codAdoptante.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Código debe iniciar con 'S-' y todos los campos deben estar llenos.");
            bitacoraService.registrarError(fechaHora, usuarioLogueado.getUsuario(), "SOLICITUDES", "VALIDACION", "Datos incompletos/invalidos", "Cod: " + cod);
            return;
        }

        Solicitud nueva = new Solicitud(cod, codAnimal, codAdoptante, fechaSimple, "PENDIENTE");
        boolean exito = solicitudService.crearSolicitud(nueva, animalService, adoptanteService);

        if (exito) {
            bitacoraService.registrarAccion(fechaHora, usuarioLogueado.getUsuario(), "SOLICITUDES", "CREACION", "Solicitud " + cod + " registrada");
            JOptionPane.showMessageDialog(this, "Solicitud registrada con éxito.");
            actualizarTabla();
            limpiarCampos();
        } else {
            bitacoraService.registrarError(fechaHora, usuarioLogueado.getUsuario(), "SOLICITUDES", "ERROR", "Fallo al crear solicitud", "Verifique existencia o estado del animal/adoptante");
            JOptionPane.showMessageDialog(this, "No se pudo registrar. Asegúrese de que el Animal esté 'APTO' y los códigos existan.");
        }
    }

    private void procesarEstado(String nuevoEstado) {
        if (!usuarioLogueado.getRol().equals("ADMIN")) {
            JOptionPane.showMessageDialog(this, "Solo los usuarios ADMIN pueden cambiar el estado de las solicitudes.");
            return;
        }

        int fila = tablaSolicitudes.getSelectedRow();
        if (fila >= 0) {
            String cod = modelTabla.getValueAt(fila, 0).toString();
            String fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());

            if (solicitudService.cambiarEstado(cod, nuevoEstado, animalService)) {
                bitacoraService.registrarAccion(fechaHora, usuarioLogueado.getUsuario(), "SOLICITUDES", "ESTADO_CAMBIO", "Solicitud " + cod + " pasó a " + nuevoEstado);
                JOptionPane.showMessageDialog(this, "Solicitud " + nuevoEstado.toLowerCase() + " con éxito.");
                actualizarTabla();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una solicitud de la tabla.");
        }
    }

    private void actualizarTabla() {
        modelTabla.setRowCount(0);
        Solicitud[] lista = solicitudService.getTodas();
        for (Solicitud s : lista) {
            modelTabla.addRow(new Object[]{
                s.getCodigo(), s.getCodigoAnimal(), s.getCodigoAdoptante(), s.getFecha(), s.getEstado()
            });
        }
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtCodAnimal.setText("");
        txtCodAdoptante.setText("");
    }
}