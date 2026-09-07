package service;

import model.Usuario;

public class AuthService {
    private Usuario[] usuarios;
    private int contadorUsuarios;
    private int intentosFallidos;
    private boolean sesionBloqueada;
    private Usuario usuarioLogueado;

    public AuthService() {
        this.usuarios = new Usuario[10];
        this.contadorUsuarios = 0;
        this.intentosFallidos = 0;
        this.sesionBloqueada = false;
        this.usuarioLogueado = null;

        // Usuarios iniciales del enunciado
        registrarUsuario(new Usuario("admin1", "Refugio2026", "ADMIN"));
        registrarUsuario(new Usuario("auxiliar1", "Refugio2026", "AUXILIAR"));
    }

    public boolean registrarUsuario(Usuario nuevo) {
        if (contadorUsuarios < usuarios.length) {
            usuarios[contadorUsuarios] = nuevo;
            contadorUsuarios++;
            return true;
        }
        return false;
    }

    public boolean autenticar(String user, String pass) {
        if (sesionBloqueada) {
            return false;
        }

        for (int i = 0; i < contadorUsuarios; i++) {
            if (usuarios[i].getUsuario().equals(user) && usuarios[i].getPassword().equals(pass)) {
                usuarioLogueado = usuarios[i];
                intentosFallidos = 0;
                return true;
            }
        }

        intentosFallidos++;
        if (intentosFallidos >= 3) {
            sesionBloqueada = true;
        }
        return false;
    }

    public boolean isSesionBloqueada() { return sesionBloqueada; }
    public int getIntentosFallidos() { return intentosFallidos; }
    public Usuario getUsuarioLogueado() { return usuarioLogueado; }
}