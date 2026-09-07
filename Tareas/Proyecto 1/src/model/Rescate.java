package model;

public class Rescate {
    private String codigo;                 // "R-009"
    private String prioridad;              // "ALTA", "MEDIA", "BAJA"
    private String estado;                 // "PENDIENTE", "ATENDIDO"
    private String fechaReporte;           // "18/08/2026"
    private String codigoAnimalVinculado;  // "A-009" o nulo/vacío

    public Rescate(String codigo, String prioridad, String estado, String fechaReporte, String codigoAnimalVinculado) {
        this.codigo = codigo;
        this.prioridad = prioridad;
        this.estado = estado;
        this.fechaReporte = fechaReporte;
        this.codigoAnimalVinculado = codigoAnimalVinculado;
    }

    public String getCodigo() { return codigo; }
    public String getPrioridad() { return prioridad; }
    public String getEstado() { return estado; }
    public String getFechaReporte() { return fechaReporte; }
    public String getCodigoAnimalVinculado() { return codigoAnimalVinculado; }

    public void setEstado(String estado) { this.estado = estado; }
    public void setCodigoAnimalVinculado(String codigoAnimalVinculado) { this.codigoAnimalVinculado = codigoAnimalVinculado; }
}