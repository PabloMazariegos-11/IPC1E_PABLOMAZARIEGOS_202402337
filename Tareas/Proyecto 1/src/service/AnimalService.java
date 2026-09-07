package service;

import model.Animal;

public class AnimalService {
    private Animal[] animales;
    private int contadorAnimales;

    public AnimalService(int capacidadMaxima) {
        this.animales = new Animal[capacidadMaxima];
        this.contadorAnimales = 0;
    }

    // Registrar animal validando código único y capacidad
    public boolean registrarAnimal(Animal nuevo) {
        if (contadorAnimales >= animales.length) {
            return false; // Arreglo lleno
        }
        if (buscarPorCodigo(nuevo.getCodigo()) != null) {
            return false; // Código duplicado
        }
        animales[contadorAnimales] = nuevo;
        contadorAnimales++;
        return true;
    }

    // Búsqueda por código exacto
    public Animal buscarPorCodigo(String codigo) {
        for (int i = 0; i < contadorAnimales; i++) {
            if (animales[i].getCodigo().equalsIgnoreCase(codigo)) {
                return animales[i];
            }
        }
        return null;
    }

    // Baja lógica: cambia el estado a ELIMINADO
    public boolean eliminarLogico(String codigo) {
        Animal a = buscarPorCodigo(codigo);
        if (a != null && !a.getEstadoAdopcion().equals("ELIMINADO")) {
            a.setEstadoAdopcion("ELIMINADO");
            return true;
        }
        return false;
    }

    // Modificar estado clínico (EN_OBSERVACION, EN_TRATAMIENTO, APTO)
    public boolean actualizarEstadoClinico(String codigo, String nuevoEstado) {
        Animal a = buscarPorCodigo(codigo);
        if (a != null && !a.getEstadoAdopcion().equals("ELIMINADO")) {
            a.setEstadoClinico(nuevoEstado);
            return true;
        }
        return false;
    }

    // Obtener arreglo activo filtrando eliminados lógicamente
    public Animal[] getAnimalesActivos() {
        int activos = 0;
        for (int i = 0; i < contadorAnimales; i++) {
            if (!animales[i].getEstadoAdopcion().equals("ELIMINADO")) {
                activos++;
            }
        }

        Animal[] resultado = new Animal[activos];
        int index = 0;
        for (int i = 0; i < contadorAnimales; i++) {
            if (!animales[i].getEstadoAdopcion().equals("ELIMINADO")) {
                resultado[index] = animales[i];
                index++;
            }
        }
        return resultado;
    }

    public int getContadorAnimales() {
        return contadorAnimales;
    }
}