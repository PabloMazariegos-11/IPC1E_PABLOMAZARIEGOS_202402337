package model;

public class EntradaBitacora {
    private String fechaHora;
    private String usuario;
    private String modulo;
    private String tipoEvento;
    private String descripcion;
    private String motivoRechazo; // Solo aplica para Bitácora de Errores

    public EntradaBitacora(String fechaHora, String usuario, String modulo, String tipoEvento, String descripcion, String motivoRechazo) {
        this.fechaHora = fechaHora;
        this.usuario = usuario;
        this.modulo = modulo;
        this.tipoEvento = tipoEvento;
        this.descripcion = descripcion;
        this.motivoRechazo = motivoRechazo;
    }

    public String getFechaHora() { return fechaHora; }
    public String getUsuario() { return usuario; }
    public String getModulo() { return modulo; }
    public String getTipoEvento() { return tipoEvento; }
    public String getDescripcion() { return descripcion; }
    public String getMotivoRechazo() { return motivoRechazo; }
}