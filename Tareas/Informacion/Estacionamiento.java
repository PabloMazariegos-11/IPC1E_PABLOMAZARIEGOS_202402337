package Tareas.Informacion;

import java.util.Scanner;

public class Estacionamiento {

    // Arreglo Nativo para la gestión interna de vehículos (8x8)
    private static String[][] parqueo = new String[8][8];
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarParqueo();

        int opcion = 0;
        do {
            mostrarMenu();
            String entrada = scanner.nextLine();
            try {
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1:
                    System.out.println("[En proceso] Ingresar vehículo...");
                    break;
                case 2:
                    System.out.println("[En proceso] Retirar vehículo...");
                    break;
                case 7:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no disponible. Seleccione 1, 2 o 7.");
                    break;
            }
        } while (opcion != 7);
    }

    // Inicializa todos los espacios como 'L' (Libre)
    private static void inicializarParqueo() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                parqueo[i][j] = "L";
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n===== SISTEMA DE ESTACIONAMIENTO =====");
        System.out.println("1. Ingresar vehículo");
        System.out.println("2. Retirar vehículo");
        System.out.println("7. Salir");
        System.out.print("Seleccione una opción: ");
    }
}