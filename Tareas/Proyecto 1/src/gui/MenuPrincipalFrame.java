package gui;

import javax.swing.*;
import java.awt.*;
import model.Usuario;
import service.AdoptanteService;
import service.AnimalService;
import service.AuthService;
import service.BitacoraService;
import service.SolicitudService;

public class MenuPrincipalFrame extends JFrame {
    private Usuario usuarioLogueado;
    private AuthService authService;
    private BitacoraService bitacoraService;
    private AnimalService animalService;
    private AdoptanteService adoptanteService;
    private SolicitudService solicitudService;

    public MenuPrincipalFrame(Usuario usuarioLogueado, AuthService authService, BitacoraService bitacoraService) {
        this.usuarioLogueado = usuarioLogueado;
        this.authService = authService;
        this.bitacoraService = bitacoraService;
        this.animalService = new AnimalService(100);
        this.adoptanteService = new AdoptanteService(100);
        this.solicitudService = new SolicitudService(100);

        setTitle("Centro de Rescate Animal - Menú Principal");
        setSize(500, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel lblBienvenida = new JLabel("Bienvenido, " + usuarioLogueado.getUsuario() + " [" + usuarioLogueado.getRol() + "]", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 15));
        lblBienvenida.setBounds(20, 15, 440, 25);
        panel.add(lblBienvenida);

        JButton btnAnimales = new JButton("Módulo de Animales Rescatados");
        btnAnimales.setBounds(100, 60, 280, 35);
        panel.add(btnAnimales);

        JButton btnAdoptantes = new JButton("Módulo de Adoptantes");
        btnAdoptantes.setBounds(100, 105, 280, 35);
        panel.add(btnAdoptantes);

        JButton btnSolicitudes = new JButton("Módulo de Solicitudes");
        btnSolicitudes.setBounds(100, 150, 280, 35);
        panel.add(btnSolicitudes);

        JButton btnRescates = new JButton("Rescates Urgentes");
        btnRescates.setBounds(100, 195, 280, 35);
        panel.add(btnRescates);

        JButton btnUbicaciones = new JButton("Panel de Ubicaciones (Matriz)");
        btnUbicaciones.setBounds(100, 240, 280, 35);
        panel.add(btnUbicaciones);

        JButton btnReportes = new JButton("Reportes y Bitácoras HTML");
        btnReportes.setBounds(100, 285, 280, 35);
        panel.add(btnReportes);

        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setBounds(180, 335, 120, 28);
        panel.add(btnLogout);

        // Eventos
        btnAnimales.addActionListener(e -> {
            AnimalesFrame animalesFrame = new AnimalesFrame(usuarioLogueado, animalService, bitacoraService, MenuPrincipalFrame.this);
            animalesFrame.setVisible(true);
            setVisible(false);
        });

        btnAdoptantes.addActionListener(e -> {
            AdoptanteFrame adoptantesFrame = new AdoptanteFrame(usuarioLogueado, adoptanteService, bitacoraService, MenuPrincipalFrame.this);
            adoptantesFrame.setVisible(true);
            setVisible(false);
        });

        btnSolicitudes.addActionListener(e -> {
            SolicitudesFrame solicitudesFrame = new SolicitudesFrame(usuarioLogueado, solicitudService, animalService, adoptanteService, bitacoraService, MenuPrincipalFrame.this);
            solicitudesFrame.setVisible(true);
            setVisible(false);
        });

        btnLogout.addActionListener(e -> {
            authService.cerrarSesion();
            LoginFrame login = new LoginFrame(authService, bitacoraService);
            login.setVisible(true);
            dispose();
        });

        add(panel);
    }
}