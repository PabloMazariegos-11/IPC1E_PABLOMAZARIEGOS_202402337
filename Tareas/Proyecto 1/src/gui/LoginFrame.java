package gui;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.Usuario;
import service.AuthService;
import service.BitacoraService;

public class LoginFrame extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblMensaje;

    private AuthService authService;
    private BitacoraService bitacoraService;

    public LoginFrame(AuthService authService, BitacoraService bitacoraService) {
        this.authService = authService;
        this.bitacoraService = bitacoraService;

        setTitle("Centro de Rescate Animal - Autenticación");
        setSize(380, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel lblTitulo = new JLabel("INICIO DE SESIÓN", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setBounds(20, 15, 320, 25);
        panel.add(lblTitulo);

        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setBounds(40, 60, 80, 25);
        panel.add(lblUser);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(130, 60, 180, 25);
        panel.add(txtUsuario);

        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setBounds(40, 100, 80, 25);
        panel.add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(130, 100, 180, 25);
        panel.add(txtPassword);

        btnLogin = new JButton("Ingresar");
        btnLogin.setBounds(130, 140, 180, 30);
        panel.add(btnLogin);

        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setForeground(Color.RED);
        lblMensaje.setBounds(20, 175, 320, 25);
        panel.add(lblMensaje);

        btnLogin.addActionListener(e -> ejecutarLogin());

        add(panel);
    }

    private void ejecutarLogin() {
        String user = txtUsuario.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();
        String fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());

        if (user.isEmpty() || pass.isEmpty()) {
            lblMensaje.setText("Campos obligatorios incompletos.");
            bitacoraService.registrarError(fechaHora, user.isEmpty() ? "ANONYMOUS" : user, 
                "AUTENTICACION", "VALIDACION", "Campos vacios en login", "Credenciales no ingresadas");
            return;
        }

        if (authService.isSesionBloqueada()) {
            lblMensaje.setText("Sesión bloqueada, reinicie la aplicación.");
            btnLogin.setEnabled(false);
            return;
        }

        boolean exito = authService.autenticar(user, pass);

        if (exito) {
            Usuario usuarioLogueado = authService.getUsuarioLogueado();
            bitacoraService.registrarAccion(fechaHora, usuarioLogueado.getUsuario(), 
                "AUTENTICACION", "LOGIN_OK", "Inicio de sesión correcto");
            
            MenuPrincipalFrame menu = new MenuPrincipalFrame(usuarioLogueado, authService, bitacoraService);
            menu.setVisible(true);
            this.dispose();
        } else {
            int intentos = authService.getIntentosFallidos();
            bitacoraService.registrarError(fechaHora, user, 
                "AUTENTICACION", "LOGIN_FALLIDO", "Contraseña incorrecta", "Intento " + intentos + " de 3");

            if (authService.isSesionBloqueada()) {
                lblMensaje.setText("Sesión bloqueada, reinicie la aplicación.");
                btnLogin.setEnabled(false);
            } else {
                lblMensaje.setText("Credenciales inválidas. Intento " + intentos + " de 3.");
            }
        }
    }
}