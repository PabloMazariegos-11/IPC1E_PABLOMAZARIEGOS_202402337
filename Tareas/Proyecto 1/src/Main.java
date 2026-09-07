import gui.LoginFrame;
import javax.swing.SwingUtilities;
import service.AuthService;
import service.BitacoraService;

public class Main {
    public static void main(String[] args) {
        // Inicializar servicios principales con capacidad estática
        AuthService authService = new AuthService();
        BitacoraService bitacoraService = new BitacoraService(100);

        // Iniciar la interfaz gráfica Swing en el hilo de eventos de AWT
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame(authService, bitacoraService);
            login.setVisible(true);
        });
    }
}