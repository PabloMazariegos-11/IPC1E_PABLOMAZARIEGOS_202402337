package service;

import model.Animal;

public class UbicacionService {
    private Animal[][] matrizJaulas;
    private int filas;
    private int columnas;

    public UbicacionService(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.matrizJaulas = new Animal[filas][columnas];
    }

    public boolean asignarUbicacion(int fila, int columna, Animal animal, String fechaHora, String usuario, BitacoraService bitacora) {
        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas) {
            if (bitacora != null) {
                // 6 parámetros: fechaHora, usuario, modulo, tipoEvento, descripcion, motivo
                bitacora.registrarError(
                    fechaHora, 
                    usuario, 
                    "UBICACIONES", 
                    "CAPACIDAD", 
                    "Error al asignar ubicacion", 
                    "Coordenadas fuera de rango: [" + fila + "][" + columna + "]"
                );
            }
            return false;
        }
        if (matrizJaulas[fila][columna] != null) {
            if (bitacora != null) {
                // 6 parámetros: fechaHora, usuario, modulo, tipoEvento, descripcion, motivo
                bitacora.registrarError(
                    fechaHora, 
                    usuario, 
                    "UBICACIONES", 
                    "CAPACIDAD", 
                    "Espacio ocupado", 
                    "Celda [" + fila + "][" + columna + "] ya ocupada por " + matrizJaulas[fila][columna].getCodigo()
                );
            }
            return false;
        }
        
        matrizJaulas[fila][columna] = animal;
        if (bitacora != null) {
            // 5 parámetros: fechaHora, usuario, modulo, tipoEvento, descripcion
            bitacora.registrarAccion(
                fechaHora, 
                usuario, 
                "UBICACIONES", 
                "ASIGNAR", 
                animal.getCodigo() + " asignado a [" + fila + "][" + columna + "]"
            );
        }
        return true;
    }

    public boolean liberarUbicacion(int fila, int columna, String fechaHora, String usuario, BitacoraService bitacora) {
        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas || matrizJaulas[fila][columna] == null) {
            return false;
        }
        String codAnimal = matrizJaulas[fila][columna].getCodigo();
        matrizJaulas[fila][columna] = null;
        if (bitacora != null) {
            // 5 parámetros: fechaHora, usuario, modulo, tipoEvento, descripcion
            bitacora.registrarAccion(
                fechaHora, 
                usuario, 
                "UBICACIONES", 
                "LIBERAR", 
                "Celda [" + fila + "][" + columna + "] liberada (estaba " + codAnimal + ")"
            );
        }
        return true;
    }

    public Animal getAnimalEn(int fila, int columna) {
        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas) return null;
        return matrizJaulas[fila][columna];
    }

    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }
}