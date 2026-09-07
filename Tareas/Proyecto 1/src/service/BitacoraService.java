package service;

import model.EntradaBitacora;

public class BitacoraService {
    private EntradaBitacora[] acciones;
    private int contadorAcciones;
    private EntradaBitacora[] errores;
    private int contadorErrores;

    public BitacoraService(int capacidad) {
        this.acciones = new EntradaBitacora[capacidad];
        this.contadorAcciones = 0;
        this.errores = new EntradaBitacora[capacidad];
        this.contadorErrores = 0;
    }

    // Registrar eventos exitosos
    public void registrarAccion(String fechaHora, String usuario, String modulo, String tipoEvento, String descripcion) {
        if (contadorAcciones < acciones.length) {
            acciones[contadorAcciones] = new EntradaBitacora(fechaHora, usuario, modulo, tipoEvento, descripcion, null);
            contadorAcciones++;
        }
    }

    // Registrar intentos fallidos o validaciones rechazadas
    public void registrarError(String fechaHora, String usuario, String modulo, String tipoEvento, String descripcion, String motivo) {
        if (contadorErrores < errores.length) {
            errores[contadorErrores] = new EntradaBitacora(fechaHora, usuario, modulo, tipoEvento, descripcion, motivo);
            contadorErrores++;
        }
    }

    public EntradaBitacora[] getAcciones() { return acciones; }
    public int getContadorAcciones() { return contadorAcciones; }

    public EntradaBitacora[] getErrores() { return errores; }
    public int getContadorErrores() { return contadorErrores; }
}