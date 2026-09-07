package model;

public class Solicitud {
    private String codigo;        // "S-021"
    private String codigoAnimal;  // "A-014"
    private String codigoAdoptante; // "AD-007"
    private String fecha;         // "dd/mm/aaaa"
    private String estado;        // "PENDIENTE", "APROBADA", "RECHAZADA", "COMPLETADA"

    public Solicitud(String codigo, String codigoAnimal, String codigoAdoptante, String fecha, String estado) {
        this.codigo = codigo;
        this.codigoAnimal = codigoAnimal;
        this.codigoAdoptante = codigoAdoptante;
        this.fecha = fecha;
        this.estado = estado;
    }

    public String getCodigo() { return codigo; }
    public String getCodigoAnimal() { return codigoAnimal; }
    public String getCodigoAdoptante() { return codigoAdoptante; }
    public String getFecha() { return fecha; }
    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }
}