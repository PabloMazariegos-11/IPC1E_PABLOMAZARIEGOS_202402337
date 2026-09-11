package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.Rescate;
import model.Usuario;
import service.AnimalService;
import service.BitacoraService;
import service.RescateService;

public class RescatesFrame extends JFrame {
    private Usuario usuarioLogueado;
    private RescateService rescateService;
    private AnimalService animalService;
    private BitacoraService bitacoraService;
    private MenuPrincipalFrame menuPrincipal;

    private JTextField txtCodigo, txtCodAnimal;
    private JComboBox<String> cbPrioridad;
    private JTable tablaRescates;
    private DefaultTableModel modelTabla;

    public RescatesFrame(Usuario usuario, RescateService rescateService, AnimalService animalService, BitacoraService bitacora, MenuPrincipalFrame menu) {
        this.usuarioLogueado = usuario;
        this.rescateService = rescateService;
        this.animalService = animalService;
        this.bitacoraService = bitacora;
        this.menuPrincipal = menu;

        setTitle("Rescates Urgentes");
        setSize(780, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
        actualizarTabla();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel lblCodigo = new JLabel("Código (R-001):");
        lblCodigo.setBounds(20, 20, 130, 25);
        panel.add(lblCodigo);

        txtCodigo = new JTextField();
        txtCodigo.setBounds(150, 20, 140, 25);
        panel.add(txtCodigo);

        JLabel lblPrioridad = new JLabel("Prioridad:");
        lblPrioridad.setBounds(20, 60, 130, 25);
        panel.add(lblPrioridad);

        cbPrioridad = new JComboBox<>(new String[]{"ALTA", "MEDIA", "BAJA"});
        cbPrioridad.setBounds(150, 60, 140, 25);
        panel.add(cbPrioridad);

        JLabel lblCodAnimal = new JLabel("Animal Vinc. (Opcional):");
        lblCodAnimal.setBounds(20, 100, 140, 25);
        panel.add(lblCodAnimal);

        txtCodAnimal = new JTextField();
        txtCodAnimal.setBounds(150, 100, 140, 25);
        panel.add(txtCodAnimal);

        JButton btnRegistrar = new JButton("Registrar Rescate");
        btnRegistrar.setBounds(20, 150, 270, 30);
        panel.add(btnRegistrar);

        JButton btnAtender = new JButton("Atender Caso");
        btnAtender.setBounds(20, 190, 270, 30);
        panel.add(btnAtender);

        JButton btnVolver = new JButton("Volver al Menú");
        btnVolver.setBounds(20, 330, 270, 30);
        panel.add(btnVolver);

        String[] columnas = {"Código", "Prioridad", "Estado", "Fecha", "Animal Vinculado"};
        modelTabla = new DefaultTableModel(columnas, 0);
        tablaRescates = new JTable(modelTabla);
        JScrollPane scroll = new JScrollPane(tablaRescates);
        scroll.setBounds(310, 20, 440, 340);
        panel.add(scroll);

        btnRegistrar.addActionListener(e -> registrar());
        btnAtender.addActionListener(e -> atender());
        btnVolver.addActionListener(e -> {
            menuPrincipal.setVisible(true);
            dispose();
        });

        add(panel);
    }

    private void registrar() {
        String cod = txtCodigo.getText().trim();
        String prioridad = (String) cbPrioridad.getSelectedItem();
        String codAnimal = txtCodAnimal.getText().trim();
        String fechaSimple = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        String fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());

        if (!cod.startsWith("R-")) {
            JOptionPane.showMessageDialog(this, "El código debe iniciar con 'R-'.");
            bitacoraService.registrarError(fechaHora, usuarioLogueado.getUsuario(), "RESCATES", "VALIDACION", "Formato de código erróneo", "El código debe iniciar con R- (" + cod + ")");
            return;
        }

        Rescate nuevo = new Rescate(cod, prioridad, "PENDIENTE", fechaSimple, codAnimal);
        if (rescateService.registrarRescate(nuevo)) {
            bitacoraService.registrarAccion(fechaHora, usuarioLogueado.getUsuario(), "RESCATES", "CREACION", "Rescate " + cod + " registrado con prioridad " + prioridad);
            JOptionPane.showMessageDialog(this, "Rescate registrado exitosamente.");
            actualizarTabla();
            txtCodigo.setText("");
            txtCodAnimal.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Código duplicado o límite de memoria alcanzado.");
            bitacoraService.registrarError(fechaHora, usuarioLogueado.getUsuario(), "RESCATES", "DUPLICADO", "Error al registrar rescate", "Código duplicado: " + cod);
        }
    }

    private void atender() {
        int fila = tablaRescates.getSelectedRow();
        if (fila >= 0) {
            String codRescate = modelTabla.getValueAt(fila, 0).toString();
            String codAnimal = txtCodAnimal.getText().trim();
            String fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());

            if (rescateService.atenderRescate(codRescate, codAnimal, animalService, fechaHora, usuarioLogueado.getUsuario(), bitacoraService)) {
                JOptionPane.showMessageDialog(this, "Rescate atendido y vinculado correctamente.");
                actualizarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo atender el rescate (revisa el estado del caso).");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una fila de la tabla de rescates.");
        }
    }

    private void actualizarTabla() {
        modelTabla.setRowCount(0);
        for (Rescate r : rescateService.getTodos()) {
            modelTabla.addRow(new Object[]{
                r.getCodigo(),
                r.getPrioridad(),
                r.getEstado(),
                r.getFechaReporte(),
                r.getCodigoAnimalVinculado()
            });
        }
    }
}