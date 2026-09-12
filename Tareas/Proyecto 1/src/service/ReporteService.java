package service;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import model.Animal;

public class ReporteService {

    public static boolean generarReporteAnimalesHTML(AnimalService animalService, String rutaArchivo) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n<html lang='es'>\n<head>\n");
        html.append("<meta charset='UTF-8'>\n<title>Reporte de Animales Rescatados</title>\n");
        html.append("<style>\n");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f6f9; }\n");
        html.append("h1 { color: #2c3e50; text-align: center; }\n");
        html.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; background-color: #fff; }\n");
        html.append("th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }\n");
        html.append("th { background-color: #3498db; color: white; }\n");
        html.append("tr:nth-child(even) { background-color: #f2f2f2; }\n");
        html.append("</style>\n</head>\n<body>\n");
        html.append("<h1>Reporte de Animales Rescatados</h1>\n");
        html.append("<table>\n<tr><th>Código</th><th>Nombre</th><th>Especie</th><th>Edad</th><th>Estado Clínico</th><th>Estado Adopción</th></tr>\n");

        Animal[] lista = obtenerAnimales(animalService);
        if (lista != null) {
            for (Animal a : lista) {
                if (a != null) {
                    html.append("<tr>")
                        .append("<td>").append(a.getCodigo()).append("</td>")
                        .append("<td>").append(a.getNombre()).append("</td>")
                        .append("<td>").append(a.getEspecie()).append("</td>")
                        .append("<td>").append(a.getEdadEstimada()).append("</td>")
                        .append("<td>").append(a.getEstadoClinico()).append("</td>")
                        .append("<td>").append(a.getEstadoAdopcion()).append("</td>")
                        .append("</tr>\n");
                }
            }
        }

        html.append("</table>\n</body>\n</html>");
        return escribirArchivoUTF8(rutaArchivo, html.toString());
    }

    public static boolean generarReporteBitacoraHTML(BitacoraService bitacoraService, String rutaArchivo) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n<html lang='es'>\n<head>\n");
        html.append("<meta charset='UTF-8'>\n<title>Bitácora del Sistema</title>\n");
        html.append("<style>\n");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f6f9; }\n");
        html.append("h1 { color: #2c3e50; text-align: center; }\n");
        html.append("h2 { color: #34495e; margin-top: 25px; }\n");
        html.append("table { width: 100%; border-collapse: collapse; margin-top: 10px; background-color: #fff; }\n");
        html.append("th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }\n");
        html.append("th { background-color: #2ecc71; color: white; }\n");
        html.append("th.error { background-color: #e74c3c; color: white; }\n");
        html.append("tr:nth-child(even) { background-color: #f2f2f2; }\n");
        html.append("</style>\n</head>\n<body>\n");
        html.append("<h1>Bitácora de Auditoría y Eventos</h1>\n");

        // --- SECCIÓN ACCIONES ---
        html.append("<h2>Acciones Realizadas</h2>\n");
        html.append("<table>\n<tr><th>Registro / Evento</th></tr>\n");
        
        boolean hayAcciones = false;
        if (bitacoraService != null && bitacoraService.getAcciones() != null) {
            Object[] acciones = bitacoraService.getAcciones();
            for (int i = 0; i < bitacoraService.getContadorAcciones(); i++) {
                Object entrada = acciones[i];
                if (entrada != null) {
                    html.append("<tr><td>").append(entrada.toString()).append("</td></tr>\n");
                    hayAcciones = true;
                }
            }
        }
        if (!hayAcciones) {
            html.append("<tr><td style='text-align:center;'>No hay acciones registradas todavía.</td></tr>\n");
        }
        html.append("</table>\n");

        // --- SECCIÓN ERRORES ---
        html.append("<h2>Errores y Validaciones Rechazadas</h2>\n");
        html.append("<table>\n<tr><th class='error'>Registro / Evento de Error</th></tr>\n");

        boolean hayErrores = false;
        if (bitacoraService != null && bitacoraService.getErrores() != null) {
            Object[] errores = bitacoraService.getErrores();
            for (int i = 0; i < bitacoraService.getContadorErrores(); i++) {
                Object entrada = errores[i];
                if (entrada != null) {
                    html.append("<tr><td>").append(entrada.toString()).append("</td></tr>\n");
                    hayErrores = true;
                }
            }
        }
        if (!hayErrores) {
            html.append("<tr><td style='text-align:center;'>No hay errores registrados.</td></tr>\n");
        }
        html.append("</table>\n");

        html.append("</body>\n</html>");
        return escribirArchivoUTF8(rutaArchivo, html.toString());
    }

    public static boolean exportarAnimalesCSV(AnimalService animalService, String rutaArchivo) {
        StringBuilder csv = new StringBuilder();
        csv.append("Codigo,Nombre,Especie,Edad,EstadoClinico,EstadoAdopcion\n");
        Animal[] lista = obtenerAnimales(animalService);
        if (lista != null) {
            for (Animal a : lista) {
                if (a != null) {
                    csv.append(a.getCodigo()).append(",")
                       .append(a.getNombre()).append(",")
                       .append(a.getEspecie()).append(",")
                       .append(a.getEdadEstimada()).append(",")
                       .append(a.getEstadoClinico()).append(",")
                       .append(a.getEstadoAdopcion()).append("\n");
                }
            }
        }
        return escribirArchivoUTF8(rutaArchivo, csv.toString());
    }

    private static Animal[] obtenerAnimales(AnimalService service) {
        if (service == null) return new Animal[0];
        try {
            for (Method m : service.getClass().getMethods()) {
                if (m.getReturnType().equals(Animal[].class) && m.getParameterCount() == 0) {
                    return (Animal[]) m.invoke(service);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Animal[0];
    }

    private static boolean escribirArchivoUTF8(String ruta, String contenido) {
        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(ruta), StandardCharsets.UTF_8)) {
            osw.write(contenido);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}