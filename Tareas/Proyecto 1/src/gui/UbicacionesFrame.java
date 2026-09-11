package gui;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.Animal;
import model.Usuario;
import service.AnimalService;
import service.BitacoraService;
import service.UbicacionService;

public class UbicacionesFrame extends JFrame {
    private Usuario usuarioLogueado;
    private UbicacionService ubicacionService;
    private AnimalService animalService;
    private BitacoraService bitacoraService;
    private MenuPrincipalFrame menuPrincipal;
    private JPanel panelMatriz;

    public UbicacionesFrame(Usuario usuario, UbicacionService ubiService, AnimalService animalService, BitacoraService bitacora, MenuPrincipalFrame menu) {
        this.usuarioLogueado = usuario;
        this.ubicacionService = ubiService;
        this.animalService = animalService;
        this.bitacoraService = bitacora;
        this.menuPrincipal = menu;

        setTitle("Panel de Ubicaciones (Matriz del Refugio)");
        setSize(650, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel panelMain = new JPanel();
        panelMain.setLayout(null);

        JLabel lblTitulo = new JLabel("Distribución Física de Espacios (Matriz 4x4)", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setBounds(20, 10, 590, 25);
        panelMain.add(lblTitulo);

        panelMatriz = new JPanel();
        panelMatriz.setLayout(new GridLayout(ubicacionService.getFilas(), ubicacionService.getColumnas(), 5, 5));
        panelMatriz.setBounds(20, 45, 590, 320);
        panelMain.add(panelMatriz);

        JButton btnAsignar = new JButton("Asignar Animal a Jaula");
        btnAsignar.setBounds(50, 380, 230, 30);
        panelMain.add(btnAsignar);

        JButton btnVolver = new JButton("Volver al Menú");
        btnVolver.setBounds(350, 380, 230, 30);
        panelMain.add(btnVolver);

        btnAsignar.addActionListener(e -> asignarAnimal());
        btnVolver.addActionListener(e -> {
            menuPrincipal.setVisible(true);
            dispose();
        });

        renderizarMatriz();
        add(panelMain);
    }

    private void renderizarMatriz() {
        panelMatriz.removeAll();
        for (int i = 0; i < ubicacionService.getFilas(); i++) {
            for (int j = 0; j < ubicacionService.getColumnas(); j++) {
                Animal a = ubicacionService.getAnimalEn(i, j);
                String texto = "Jaula [" + i + "][" + j + "]\n" + (a != null ? a.getCodigo() : "LIBRE");
                JButton btnCelda = new JButton("<html><center>" + texto.replace("\n", "<br>") + "</center></html>");
                btnCelda.setBackground(a != null ? Color.PINK : Color.GREEN);
                panelMatriz.add(btnCelda);
            }
        }
        panelMatriz.revalidate();
        panelMatriz.repaint();
    }

    private void asignarAnimal() {
        String codAnimal = JOptionPane.showInputDialog(this, "Ingrese el Código del Animal (ej. A-001):");
        if (codAnimal == null || codAnimal.trim().isEmpty()) return;

        Animal animal = animalService.buscarPorCodigo(codAnimal.trim());
        String fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());

        if (animal == null) {
            JOptionPane.showMessageDialog(this, "El animal no existe en el sistema.");
            // 6 parámetros: fechaHora, usuario, modulo, tipoEvento, descripcion, motivo
            bitacoraService.registrarError(
                fechaHora, 
                usuarioLogueado.getUsuario(), 
                "UBICACIONES", 
                "VALIDACION", 
                "Error al asignar animal a jaula", 
                "Animal " + codAnimal + " no encontrado"
            );
            return;
        }

        try {
            int f = Integer.parseInt(JOptionPane.showInputDialog(this, "Ingrese Fila (0 a " + (ubicacionService.getFilas() - 1) + "):"));
            int c = Integer.parseInt(JOptionPane.showInputDialog(this, "Ingrese Columna (0 a " + (ubicacionService.getColumnas() - 1) + "):"));

            if (ubicacionService.asignarUbicacion(f, c, animal, fechaHora, usuarioLogueado.getUsuario(), bitacoraService)) {
                JOptionPane.showMessageDialog(this, "Animal asignado a la celda [" + f + "][" + c + "] exitosamente.");
                renderizarMatriz();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo asignar: Jaula ocupada o posición fuera de rango.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Las coordenadas deben ser números enteros.");
        }
    }
}