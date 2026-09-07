package model;

public class Animal {
    private String codigo; // "A-014"
    private String nombre;
    private String especie; // "Perro" o "Gato"
    private int edadEstimada;
    private String estadoClinico; // "EN_OBSERVACION", "EN_TRATAMIENTO", "APTO"
    private String estadoAdopcion; // "DISPONIBLE", "ADOPTADO", "ELIMINADO"

    public Animal(String codigo, String nombre, String especie, int edadEstimada, String estadoClinico, String estadoAdopcion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.especie = especie;
        this.edadEstimada = edadEstimada;
        this.estadoClinico = estadoClinico;
        this.estadoAdopcion = estadoAdopcion;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getEspecie() { return especie; }
    public int getEdadEstimada() { return edadEstimada; }
    public String getEstadoClinico() { return estadoClinico; }
    public void setEstadoClinico(String estadoClinico) { this.estadoClinico = estadoClinico; }
    public String getEstadoAdopcion() { return estadoAdopcion; }
    public void setEstadoAdopcion(String estadoAdopcion) { this.estadoAdopcion = estadoAdopcion; }
}
