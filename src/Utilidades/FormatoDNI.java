/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilidades;

/**
 *
 * @author luisa
 */
public class FormatoDNI {
    
    private static final String LETRAS_DNI = "TRWAGMYFPDXBNJZSQVHLCKE";

    public static String formatearIdentificacion(String identificacion) {
        if (identificacion == null || identificacion.isEmpty()) {
            return "";
        }

        identificacion = identificacion.toUpperCase();
        if (identificacion.startsWith("X") || identificacion.startsWith("Y") || identificacion.startsWith("Z")) {
            return formatearNie(identificacion);
        } else {
            return formatearDni(identificacion);
        }
    }

    private static String formatearDni(String dni) {
        int numeroDni;
        try {
            numeroDni = Integer.parseInt(dni);
        } catch (NumberFormatException e) {
            return dni; // Devuelve el DNI original si no es un número válido
        }

        // Rellenar con ceros a la izquierda si es necesario
        String dniConCeros = String.format("%08d", numeroDni);

        // Calcular la letra del DNI
        char letra = calcularLetraDni(numeroDni);

        return dniConCeros + letra;
    }

    private static String formatearNie(String nie) {
        // Reemplazar la letra inicial por un número
        int numeroInicial = nie.startsWith("X") ? 0 : (nie.startsWith("Y") ? 1 : 2);
        String nieNumeros = numeroInicial + nie.substring(1);

        // Calcular la letra del NIE
        char letra = calcularLetraDni(Integer.parseInt(nieNumeros));

        return nieNumeros + letra;
    }

    private static char calcularLetraDni(int numeroDni) {
        int resto = numeroDni % 23;
        return LETRAS_DNI.charAt(resto);
    }
}
