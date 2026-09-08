package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.Adoptante;
import model.Usuario;
import service.AdoptanteService;
import service.BitacoraService;

public class AdoptanteFrame extends JFrame {
    private Usuario usuarioLogueado;
    private AdoptanteService adoptanteService;
    private BitacoraService bitacoraService;
    private MenuPrincipalFrame menuPrincipal;

    private JTextField txtCodigo, txtNombre, txtDpi, txtTelefono;
    private JTable tablaAdoptantes;
    private DefaultTableModel modelTabla;

    public AdoptanteFrame(Usuario usuarioLogueado, AdoptanteService adoptanteService, BitacoraService bitacoraService, MenuPrincipalFrame menuPrincipal) {
        this.usuarioLogueado = usuarioLogueado;
        this.adoptanteService = adoptanteService;
        this.bitacoraService = bitacoraService;
        this.menuPrincipal = menuPrincipal;

        setTitle("Gestión de Adoptantes");
        setSize(750, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
        actualizarTabla();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Formulario
        JLabel lblCodigo = new JLabel("Código (ej. AD-007):");
        lblCodigo.setBounds(20, 20, 130, 25);
        panel.add(lblCodigo);

        txtCodigo = new JTextField();
        txtCodigo.setBounds(150, 20, 150, 25);
        panel.add(txtCodigo);

        JLabel lblNombre = new JLabel("Nombre Completo:");
        lblNombre.setBounds(20, 55, 130, 25);
        panel.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(150, 55, 150, 25);
        panel.add(txtNombre);

        JLabel lblDpi = new JLabel("DPI (13 dígitos):");
        lblDpi.setBounds(20, 90, 130, 25);
        panel.add(lblDpi);

        txtDpi = new JTextField();
        txtDpi.setBounds(150, 90, 150, 25);
        panel.add(txtDpi);

        JLabel lblTel = new JLabel("Teléfono (8 dígitos):");
        lblTel.setBounds(20, 125, 130, 25);
        panel.add(lblTel);

        txtTelefono = new JTextField();
        txtTelefono.setBounds(150, 125, 150, 25);
        panel.add(txtTelefono);

        // Botones
        JButton btnRegistrar = new JButton("Registrar Adoptante");
        btnRegistrar.setBounds(20, 170, 280, 30);
        panel.add(btnRegistrar);

        JButton btnEliminar = new JButton("Baja Lógica (Eliminar)");
        btnEliminar.setBounds(20, 210, 280, 30);
        panel.add(btnEliminar);

        JButton btnVolver = new JButton("Volver al Menú");
        btnVolver.setBounds(20, 390, 280, 30);
        panel.add(btnVolver);

        // Tabla Swing
        String[] columnas = {"Código", "Nombre", "DPI", "Teléfono"};
        modelTabla = new DefaultTableModel(columnas, 0);
        tablaAdoptantes = new JTable(modelTabla);
        JScrollPane scroll = new JScrollPane(tablaAdoptantes);
        scroll.setBounds(320, 20, 400, 400);
        panel.add(scroll);

        // Eventos
        btnRegistrar.addActionListener(e -> registrarAdoptante());
        btnEliminar.addActionListener(e -> eliminarAdoptante());
        btnVolver.addActionListener(e -> {
            menuPrincipal.setVisible(true);
            dispose();
        });

        add(panel);
    }

    private void registrarAdoptante() {
        String fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
        String cod = txtCodigo.getText().trim();
        String nom = txtNombre.getText().trim();
        String dpi = txtDpi.getText().trim();
        String tel = txtTelefono.getText().trim();

        // Validaciones requeridas
        if (!cod.startsWith("AD-") || dpi.length() != 13 || tel.length() != 8) {
            JOptionPane.showMessageDialog(this, "Validación fallida: Código debe iniciar con 'AD-', DPI debe tener 13 dígitos y Teléfono 8 dígitos.");
            bitacoraService.registrarError(fechaHora, usuarioLogueado.getUsuario(), "ADOPTANTES", "VALIDACION", "Formato de campos invalido", "Cod: " + cod + ", DPI: " + dpi);
            return;
        }

        Adoptante nuevo = new Adoptante(cod, nom, dpi, tel);
        boolean exito = adoptanteService.registrarAdoptante(nuevo);

        if (exito) {
            bitacoraService.registrarAccion(fechaHora, usuarioLogueado.getUsuario(), "ADOPTANTES", "ALTA", "Adoptante " + cod + " registrado");
            JOptionPane.showMessageDialog(this, "Adoptante registrado exitosamente.");
            actualizarTabla();
            limpiarCampos();
        } else {
            bitacoraService.registrarError(fechaHora, usuarioLogueado.getUsuario(), "ADOPTANTES", "DUPLICADO", "Error al registrar adoptante", "Codigo/DPI duplicado o limite alcanzado");
            JOptionPane.showMessageDialog(this, "El Código/DPI ya existe o el sistema alcanzó el límite.");
        }
    }

    private void eliminarAdoptante() {
        if (!usuarioLogueado.getRol().equals("ADMIN")) {
            JOptionPane.showMessageDialog(this, "Solo el usuario ADMIN puede dar de baja a un adoptante.");
            return;
        }

        int fila = tablaAdoptantes.getSelectedRow();
        if (fila >= 0) {
            String codigo = modelTabla.getValueAt(fila, 0).toString();
            String fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());

            if (adoptanteService.eliminarLogico(codigo)) {
                bitacoraService.registrarAccion(fechaHora, usuarioLogueado.getUsuario(), "ADOPTANTES", "BAJA_LOGICA", "Adoptante " + codigo + " eliminado");
                JOptionPane.showMessageDialog(this, "Adoptante dado de baja.");
                actualizarTabla();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un adoptante de la tabla.");
        }
    }

    private void actualizarTabla() {
        modelTabla.setRowCount(0);
        Adoptante[] activos = adoptanteService.getAdoptantesActivos();
        for (Adoptante a : activos) {
            modelTabla.addRow(new Object[]{
                a.getCodigo(), a.getNombre(), a.getDpi(), a.getTelefono()
            });
        }
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtDpi.setText("");
        txtTelefono.setText("");
    }
}