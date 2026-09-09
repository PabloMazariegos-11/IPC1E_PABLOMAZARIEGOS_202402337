package service;

import model.Solicitud;
import model.Animal;
import model.Adoptante;

public class SolicitudService {
    private Solicitud[] solicitudes;
    private int contador;

    public SolicitudService(int capacidad) {
        this.solicitudes = new Solicitud[capacidad];
        this.contador = 0;
    }

    public boolean crearSolicitud(Solicitud nueva, AnimalService animalService, AdoptanteService adoptanteService) {
        if (buscarPorCodigo(nueva.getCodigo()) != null) return false; 
        if (contador >= solicitudes.length) return false;            

        Animal animal = animalService.buscarPorCodigo(nueva.getCodigoAnimal());
        Adoptante adoptante = adoptanteService.buscarPorCodigo(nueva.getCodigoAdoptante());

        if (animal == null || adoptante == null) return false;

        // Valida que el estado clínico sea APTO y que no esté ya ADOPTADO
        if (animal.getEstadoClinico() == null || !animal.getEstadoClinico().equalsIgnoreCase("APTO")) {
            return false;
        }
        if (animal.getEstadoAdopcion() != null && animal.getEstadoAdopcion().equalsIgnoreCase("ADOPTADO")) {
            return false;
        }

        solicitudes[contador] = nueva;
        contador++;
        return true;
    }

    public Solicitud buscarPorCodigo(String codigo) {
        for (int i = 0; i < contador; i++) {
            if (solicitudes[i].getCodigo().equalsIgnoreCase(codigo)) {
                return solicitudes[i];
            }
        }
        return null;
    }

    public boolean cambiarEstado(String codigoSolicitud, String nuevoEstado, AnimalService animalService) {
        Solicitud sol = buscarPorCodigo(codigoSolicitud);
        if (sol == null) return false;

        Animal animal = animalService.buscarPorCodigo(sol.getCodigoAnimal());

        if (nuevoEstado.equals("APROBADA") || nuevoEstado.equals("COMPLETADA")) {
            if (animal != null) {
                animal.setEstadoAdopcion("ADOPTADO");
            }
        } else if (nuevoEstado.equals("RECHAZADA")) {
            if (animal != null && animal.getEstadoAdopcion().equalsIgnoreCase("ADOPTADO")) {
                animal.setEstadoAdopcion("DISPONIBLE");
            }
        }

        sol.setEstado(nuevoEstado);
        return true;
    }

    public Solicitud[] getTodas() {
        Solicitud[] copia = new Solicitud[contador];
        for (int i = 0; i < contador; i++) {
            copia[i] = solicitudes[i];
        }
        return copia;
    }
}