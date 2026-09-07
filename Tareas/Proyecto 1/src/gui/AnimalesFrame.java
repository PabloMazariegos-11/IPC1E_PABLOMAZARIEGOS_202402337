package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.Animal;
import model.Usuario;
import service.AnimalService;
import service.BitacoraService;

public class AnimalesFrame extends JFrame {
    private Usuario usuarioLogueado;
    private AnimalService animalService;
    private BitacoraService bitacoraService;
    private MenuPrincipalFrame menuPrincipal;

    private JTextField txtCodigo, txtNombre, txtEdad;
    private JComboBox<String> cbEspecie, cbEstadoClinico;
    private JTable tablaAnimales;
    private DefaultTableModel modelTabla;

    public AnimalesFrame(Usuario usuarioLogueado, AnimalService animalService, BitacoraService bitacoraService, MenuPrincipalFrame menuPrincipal) {
        this.usuarioLogueado = usuarioLogueado;
        this.animalService = animalService;
        this.bitacoraService = bitacoraService;
        this.menuPrincipal = menuPrincipal;

        setTitle("Gestión de Animales Rescatados");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
        actualizarTabla();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel lblCodigo = new JLabel("Código (ej. A-014):");
        lblCodigo.setBounds(20, 20, 130, 25);
        panel.add(lblCodigo);

        txtCodigo = new JTextField();
        txtCodigo.setBounds(150, 20, 150, 25);
        panel.add(txtCodigo);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 55, 130, 25);
        panel.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(150, 55, 150, 25);
        panel.add(txtNombre);

        JLabel lblEspecie = new JLabel("Especie:");
        lblEspecie.setBounds(20, 90, 130, 25);
        panel.add(lblEspecie);

        cbEspecie = new JComboBox<>(new String[]{"Perro", "Gato"});
        cbEspecie.setBounds(150, 90, 150, 25);
        panel.add(cbEspecie);

        JLabel lblEdad = new JLabel("Edad Estimada:");
        lblEdad.setBounds(20, 125, 130, 25);
        panel.add(lblEdad);

        txtEdad = new JTextField();
        txtEdad.setBounds(150, 125, 150, 25);
        panel.add(txtEdad);

        JLabel lblClinico = new JLabel("Estado Clínico:");
        lblClinico.setBounds(20, 160, 130, 25);
        panel.add(lblClinico);

        cbEstadoClinico = new JComboBox<>(new String[]{"EN_OBSERVACION", "EN_TRATAMIENTO", "APTO"});
        cbEstadoClinico.setBounds(150, 160, 150, 25);
        panel.add(cbEstadoClinico);

        JButton btnRegistrar = new JButton("Registrar Animal");
        btnRegistrar.setBounds(20, 200, 280, 30);
        panel.add(btnRegistrar);

        JButton btnEliminar = new JButton("Baja Lógica (Eliminar)");
        btnEliminar.setBounds(20, 240, 280, 30);
        panel.add(btnEliminar);

        JButton btnVolver = new JButton("Volver al Menú");
        btnVolver.setBounds(20, 410, 280, 30);
        panel.add(btnVolver);

        String[] columnas = {"Código", "Nombre", "Especie", "Edad", "Clínico", "Adopción"};
        modelTabla = new DefaultTableModel(columnas, 0);
        tablaAnimales = new JTable(modelTabla);
        JScrollPane scroll = new JScrollPane(tablaAnimales);
        scroll.setBounds(320, 20, 400, 420);
        panel.add(scroll);

        btnRegistrar.addActionListener(e -> registrarAnimal());
        btnEliminar.addActionListener(e -> eliminarAnimal());
        btnVolver.addActionListener(e -> {
            menuPrincipal.setVisible(true);
            dispose();
        });

        add(panel);
    }

    private void registrarAnimal() {
        String fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
        String cod = txtCodigo.getText().trim();
        String nom = txtNombre.getText().trim();
        String esp = cbEspecie.getSelectedItem().toString();
        String edadStr = txtEdad.getText().trim();
        String clinico = cbEstadoClinico.getSelectedItem().toString();

        if (cod.isEmpty() || !cod.startsWith("A-")) {
            JOptionPane.showMessageDialog(this, "El código debe iniciar con 'A-' (Ej. A-014).");
            bitacoraService.registrarError(fechaHora, usuarioLogueado.getUsuario(), "ANIMALES", "VALIDACION", "Formato de codigo invalido", "Codigo: " + cod);
            return;
        }

        try {
            int edad = Integer.parseInt(edadStr);
            if (edad < 0 || edad > 25) throw new NumberFormatException();

            Animal nuevo = new Animal(cod, nom, esp, edad, clinico, "DISPONIBLE");
            boolean exito = animalService.registrarAnimal(nuevo);

            if (exito) {
                bitacoraService.registrarAccion(fechaHora, usuarioLogueado.getUsuario(), "ANIMALES", "ALTA", "Animal " + cod + " registrado");
                JOptionPane.showMessageDialog(this, "Animal registrado exitosamente.");
                actualizarTabla();
                limpiarCampos();
            } else {
                bitacoraService.registrarError(fechaHora, usuarioLogueado.getUsuario(), "ANIMALES", "DUPLICADO", "Error al registrar animal", "Codigo o limite excedido");
                JOptionPane.showMessageDialog(this, "El código ya existe o el refugio está lleno.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Edad debe ser un número entero entre 0 y 25.");
            bitacoraService.registrarError(fechaHora, usuarioLogueado.getUsuario(), "ANIMALES", "VALIDACION", "Edad fuera de rango", "Edad: " + edadStr);
        }
    }

    private void eliminarAnimal() {
        if (!usuarioLogueado.getRol().equals("ADMIN")) {
            JOptionPane.showMessageDialog(this, "Solo un usuario ADMIN puede realizar bajas lógicas.");
            return;
        }

        int fila = tablaAnimales.getSelectedRow();
        if (fila >= 0) {
            String codigo = modelTabla.getValueAt(fila, 0).toString();
            String fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());

            if (animalService.eliminarLogico(codigo)) {
                bitacoraService.registrarAccion(fechaHora, usuarioLogueado.getUsuario(), "ANIMALES", "BAJA_LOGICA", "Animal " + codigo + " marcado como ELIMINADO");
                JOptionPane.showMessageDialog(this, "Registro dado de baja.");
                actualizarTabla();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un animal de la tabla.");
        }
    }

    private void actualizarTabla() {
        modelTabla.setRowCount(0);
        Animal[] activos = animalService.getAnimalesActivos();
        for (Animal a : activos) {
            modelTabla.addRow(new Object[]{
                a.getCodigo(), a.getNombre(), a.getEspecie(), 
                a.getEdadEstimada(), a.getEstadoClinico(), a.getEstadoAdopcion()
            });
        }
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtEdad.setText("");
    }
}