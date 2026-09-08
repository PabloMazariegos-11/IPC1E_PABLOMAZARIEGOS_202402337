package service;

import model.Adoptante;

public class AdoptanteService {
    private Adoptante[] adoptantes;
    private boolean[] activos;
    private int contador;

    public AdoptanteService(int capacidad) {
        this.adoptantes = new Adoptante[capacidad];
        this.activos = new boolean[capacidad];
        this.contador = 0;
    }

    public boolean registrarAdoptante(Adoptante nuevo) {
        if (buscarPorCodigo(nuevo.getCodigo()) != null || buscarPorDpi(nuevo.getDpi()) != null) {
            return false; // Código o DPI duplicado
        }
        if (contador >= adoptantes.length) return false;

        adoptantes[contador] = nuevo;
        activos[contador] = true;
        contador++;
        return true;
    }

    public Adoptante buscarPorCodigo(String codigo) {
        for (int i = 0; i < contador; i++) {
            if (adoptantes[i].getCodigo().equalsIgnoreCase(codigo)) {
                return adoptantes[i];
            }
        }
        return null;
    }

    public Adoptante buscarPorDpi(String dpi) {
        for (int i = 0; i < contador; i++) {
            if (adoptantes[i].getDpi().equals(dpi)) {
                return adoptantes[i];
            }
        }
        return null;
    }

    public boolean eliminarLogico(String codigo) {
        for (int i = 0; i < contador; i++) {
            if (adoptantes[i].getCodigo().equalsIgnoreCase(codigo) && activos[i]) {
                activos[i] = false;
                return true;
            }
        }
        return false;
    }

    public Adoptante[] getAdoptantesActivos() {
        int totalActivos = 0;
        for (int i = 0; i < contador; i++) {
            if (activos[i]) totalActivos++;
        }

        Adoptante[] resultado = new Adoptante[totalActivos];
        int idx = 0;
        for (int i = 0; i < contador; i++) {
            if (activos[i]) {
                resultado[idx++] = adoptantes[i];
            }
        }
        return resultado;
    }
}