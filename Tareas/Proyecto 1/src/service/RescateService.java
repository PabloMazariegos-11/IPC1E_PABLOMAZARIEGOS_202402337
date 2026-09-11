package service;

import model.Animal;
import model.Rescate;

public class RescateService {
    private Rescate[] rescates;
    private int contador;

    public RescateService(int capacidad) {
        this.rescates = new Rescate[capacidad];
        this.contador = 0;
    }

    public boolean registrarRescate(Rescate nuevo) {
        if (nuevo == null || buscarPorCodigo(nuevo.getCodigo()) != null || contador >= rescates.length) {
            return false;
        }
        rescates[contador++] = nuevo;
        ordenarPorPrioridad();
        return true;
    }

    public Rescate buscarPorCodigo(String codigo) {
        for (int i = 0; i < contador; i++) {
            if (rescates[i].getCodigo().equalsIgnoreCase(codigo)) {
                return rescates[i];
            }
        }
        return null;
    }

    public boolean atenderRescate(String codigoRescate, String codAnimalIngresado, AnimalService animalService, String fechaHora, String usuario, BitacoraService bitacora) {
        Rescate rescate = buscarPorCodigo(codigoRescate);
        if (rescate == null || rescate.getEstado().equals("ATENDIDO")) {
            if (bitacora != null) {
                bitacora.registrarError(fechaHora, usuario, "RESCATES", "VALIDACION", "Error al atender rescate", "Rescate no existe o ya fue atendido: " + codigoRescate);
            }
            return false;
        }

        String codAnimalFinal;
        if (codAnimalIngresado != null && !codAnimalIngresado.trim().isEmpty()) {
            codAnimalFinal = codAnimalIngresado.trim();
        } else {
            codAnimalFinal = rescate.getCodigo().replace("R-", "A-");
        }

        if (animalService.buscarPorCodigo(codAnimalFinal) == null) {
            // Se invoca el constructor de 6 parámetros exactos de Animal.java
            Animal nuevoAnimal = new Animal(
                codAnimalFinal, 
                "Rescatado " + codAnimalFinal, 
                "Perro", 
                1, 
                "EN_TRATAMIENTO", 
                "DISPONIBLE"
            );
            animalService.registrarAnimal(nuevoAnimal);
            if (bitacora != null) {
                bitacora.registrarAccion(fechaHora, usuario, "ANIMALES", "ALTA", "Animal " + codAnimalFinal + " generado desde rescate " + codigoRescate);
            }
        }

        rescate.setEstado("ATENDIDO");
        rescate.setCodigoAnimalVinculado(codAnimalFinal);
        if (bitacora != null) {
            bitacora.registrarAccion(fechaHora, usuario, "RESCATES", "ATENDER", "Rescate " + codigoRescate + " atendido y vinculado con " + codAnimalFinal);
        }
        return true;
    }

    private void ordenarPorPrioridad() {
        for (int i = 0; i < contador - 1; i++) {
            for (int j = 0; j < contador - i - 1; j++) {
                if (getPesoPrioridad(rescates[j].getPrioridad()) > getPesoPrioridad(rescates[j + 1].getPrioridad())) {
                    Rescate temp = rescates[j];
                    rescates[j] = rescates[j + 1];
                    rescates[j + 1] = temp;
                }
            }
        }
    }

    private int getPesoPrioridad(String prioridad) {
        if (prioridad == null) return 4;
        switch (prioridad.toUpperCase()) {
            case "ALTA": return 1;
            case "MEDIA": return 2;
            case "BAJA": return 3;
            default: return 4;
        }
    }

    public Rescate[] getTodos() {
        Rescate[] copia = new Rescate[contador];
        for (int i = 0; i < contador; i++) copia[i] = rescates[i];
        return copia;
    }
}