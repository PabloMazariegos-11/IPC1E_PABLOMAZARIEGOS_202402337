package Tareas.Informacion;

import java.util.Random;
import java.util.Scanner;

public class Estacionamiento {

    private static String[][] parqueo = new String[8][8];

    // Variables para Entrada y Salida
    private static int entradaFila, entradaCol;
    private static int salidaFila, salidaCol;

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarParqueo();
        generarEntradaSalida();

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

    private static void inicializarParqueo() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                parqueo[i][j] = "L";
            }
        }
    }

    private static void generarEntradaSalida() {
        Random random = new Random();

        do {
            int[] pos = obtenerPosicionPerimetroAleatoria(random);
            entradaFila = pos[0];
            entradaCol = pos[1];
        } while (esEsquina(entradaFila, entradaCol));

        do {
            int[] pos = obtenerPosicionPerimetroAleatoria(random);
            salidaFila = pos[0];
            salidaCol = pos[1];
        } while ((salidaFila == entradaFila && salidaCol == entradaCol) || esEsquina(salidaFila, salidaCol));
    }

    private static int[] obtenerPosicionPerimetroAleatoria(Random r) {
        int lado = r.nextInt(4);
        int f = 0, c = 0;
        switch (lado) {
            case 0: f = 0; c = r.nextInt(10); break;
            case 1: f = 9; c = r.nextInt(10); break;
            case 2: f = r.nextInt(10); c = 0; break;
            case 3: f = r.nextInt(10); c = 9; break;
        }
        return new int[]{f, c};
    }

    private static boolean esEsquina(int f, int c) {
        return (f == 0 && c == 0) || (f == 0 && c == 9) || (f == 9 && c == 0) || (f == 9 && c == 9);
    }

    private static void mostrarMenu() {
        System.out.println("\n===== SISTEMA DE ESTACIONAMIENTO =====");
        System.out.println("1. Ingresar vehículo");
        System.out.println("2. Retirar vehículo");
        System.out.println("7. Salir");
        System.out.print("Seleccione una opción: ");
    }
}