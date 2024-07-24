package io;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static utils.Global.*;

/**
 * <p>
 * InputStreamReader es una clase que funciona como puente entre flujos de bytes y caracteres, convirtiendo bytes a caracteres
 * segun una codificacion especifica. Se usa para manejar texto en diferentes codificaciones (como UTF-8 o ISO-8859-1), lo que lo
 * hace mas adecuado que InputStream para leer archivos de texto, especialmente con caracteres no ASCII. En el ejemplo dado, se
 * utiliza explicitamente UTF-8 como codificacion y se incluye un caracter griego (α) para probar la decodificacion de caracteres
 * especiales.
 *
 * @author Juan Debenedetti
 */

public class InputStreamReader {

    /**
     * Lee el archivo del flujo usando un InputStreamReader para decodificar el byte en el formato especificado.
     *
     * @param file archivo.
     */
    private void read(File file) {
        /* Envuelve el FileInputStream en un InputStreamReader, que convierte bytes a caracteres. Finalmente, envuelve el
         * InputStreamReader en un BufferedReader para una lectura mas eficiente. */
        try (BufferedReader buffer = new BufferedReader(new java.io.InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            System.out.println("Reading...");

            // Lee el archivo de texto linea por linea. Esto ocurre hasta que readLine() devuelve null, que indica el final (EOF).
            String line;
            while ((line = buffer.readLine()) != null)
                System.out.println(line);

        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new InputStreamReader().read(new File(TEXT));
    }

}
