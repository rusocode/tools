package com.punkipunk.io;

import java.io.*;

import static com.punkipunk.utils.Global.*;

/**
 * <h3>Stream</h3>
 * <p>
 * Un stream o flujo de datos en Java es una abstraccion que representa una secuencia de datos que se pueden leer o escribir de
 * manera secuencial. Es una forma de manejar la entrada y salida de datos en programas Java, permitiendo la transferencia de
 * datos entre diferentes fuentes y destinos, como archivos, redes, o incluso entre diferentes partes de un programa.
 * <p>
 * Las caracteristicas principales de los streams en Java son:
 * <ol>
 * <li>Secuencialidad: Los datos se procesan en orden, uno tras otro.
 * <li>Unidireccionalidad: Un stream esta diseñado para leer o para escribir, no ambos simultaneamente (aunque existen excepciones
 * como RandomAccessFile).
 * <li>Abstraccion: Proporcionan una interfaz uniforme para trabajar con diferentes tipos de fuentes y destinos de datos.
 * <li>Tipos de datos: Pueden manejar datos binarios (bytes) o caracteres.
 * <li>Eficiencia: Permiten el procesamiento de datos de manera eficiente, especialmente cuando se trata de grandes volumenes de
 * informacion.
 * </ol>
 * <p>
 * Java proporciona dos tipos principales de streams:
 * <ol>
 * <li>Byte Streams: Para manejar datos binarios (InputStream y OutputStream).
 * <li>Character Streams: Para manejar datos de texto (Reader y Writer).
 * </ol>
 * <p>
 * Los streams se utilizan comunmente para operaciones como leer/escribir archivos, comunicacion en red, procesamiento de datos en
 * memoria, y mas. Su uso adecuado es fundamental para desarrollar aplicaciones Java eficientes y robustas que manejen datos de
 * manera efectiva.
 * <h3>Buffer</h3>
 * <p>
 * El concepto de buffer en programacion, especialmente en Java, se asemeja a un carrito de compras que almacena temporalmente
 * datos para procesarlos de manera mas eficiente. Su principal proposito es reducir las costosas operaciones de lectura y
 * escritura en disco, mejorando asi el rendimiento del programa. Los buffers utilizan una porcion de la memoria RAM para acceder
 * rapidamente a los datos. El funcionamiento de los buffers implica acumular datos para escritura hasta tener suficientes para
 * una operacion eficiente, y en lectura, obtener mas datos de los solicitados para almacenarlos y usarlos en futuras lecturas.
 * Esta estrategia resulta en un acceso mas eficiente al disco y un mejor rendimiento del programa, especialmente notable al
 * trabajar con archivos grandes. A diferencia de los flujos normales que operan byte por byte, los buffers manejan fragmentos de
 * datos, lo que les permite ser mas eficientes.
 * <p>
 * El tamaño recomendable para un buffer varia segun el contexto de uso y el tipo de operacion, pero existen algunas pautas
 * generales. Un tamaño comunmente utilizado es 8 KB (8192 bytes), que suele proporcionar un buen equilibrio entre rendimiento y
 * uso de memoria para muchas aplicaciones. Es beneficioso usar multiplos del tamaño de pagina del sistema, que en sistemas
 * modernos suele ser de 4 KB. Para operaciones de red, buffers mas grandes como 16 KB o 32 KB pueden ser ventajosos, mientras que
 * para operaciones de disco, tamaños de 8 KB a 64 KB suelen ser efectivos.
 * <p>
 * En general, usar un buffer de tamaño fijo (como 8192 bytes) es una practica mas robusta y escalable, especialmente cuando no
 * conoces de antemano el tamaño de los archivos que vas a manejar. Permite un equilibrio entre eficiencia de lectura y uso de
 * memoria, y funciona bien tanto para archivos pequeños como grandes.
 * <p>
 * FileInputStream es una clase basica para la entrada de datos que lee bytes directamente de un archivo sin utilizar un buffer
 * intermedio. Esto significa que cada vez que se llama a un metodo de lectura en FileInputStream, se realiza una operacion de
 * lectura directa en el disco, lo cual puede ser menos eficiente, especialmente cuando se leen pequeñas cantidades de datos
 * repetidamente.
 * <p>
 * Si se necesita un rendimiento mejorado para la lectura de archivos, especialmente para archivos grandes o cuando se realizan
 * muchas operaciones de lectura pequeñas, se recomienda envolver FileInputStream en un BufferedInputStream. Esto añade la
 * funcionalidad de buffer, lo que puede mejorar significativamente el rendimiento al reducir el numero de accesos al disco.
 * Aunque menos eficiente pero no por eso peor, tambien es posible crear un array de bytes (buffer) y utilizarlo para leer desde
 * el flujo.
 * <p>
 * Links:
 * <a href="https://www.youtube.com/watch?v=lE7HXIJOpDU">The Hard Drive Buffer</a>
 * <a href="https://www.youtube.com/watch?v=x2vegjeJICk">Buffer Cache</a>
 * <a href="https://www.youtube.com/watch?v=baHz_RmMt5I">Java IO - Buffered Streams (BufferedInputStream & BufferedOutputStream)</a>
 * <a href="https://www.quora.com/Why-are-there-1024-bytes-in-a-kilobyte">Why are there 1024 bytes in a kilobyte?</a>
 * <a href="https://www.youtube.com/watch?v=SOoJcrR4Ijo">Ethical Hacking: Buffer Overflow Basics</a>
 *
 * @author Juan Debenedetti
 */

public class Stream {

    private final File file;

    public Stream(File file) {
        this.file = file;
    }

    /**
     * Lee el archivo del flujo usando llamadas nativas, es decir, byte por byte.
     */
    private void nativeRead() {
        try (FileInputStream input = new FileInputStream(file)) {
            System.out.println("Name: " + file.getName());
            System.out.println("Path: " + file.getPath());
            // file.length() es mas consistente y eficiente que input.getChannel().size() y input.available()
            System.out.println("Size: " + (int) file.length() + " bytes");

            System.out.println("Reading...");

            // Itera mientras la lectura del archivo no retorne -1, que indica el final
            long startTime = System.nanoTime();
            while (input.read() != -1)
                System.out.println("Native reading");
            System.out.print("Reading finished in " + (System.nanoTime() - startTime) / 1e6 + " ms\n");

        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }

    /**
     * Lee el archivo del flujo usando un array de bytes, es decir, un buffer.
     */
    private void read() {
        try (FileInputStream input = new FileInputStream(file)) {

            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

            System.out.println("Name: " + file.getName());
            System.out.println("Path: " + file.getPath());
            System.out.println("Size: " + (int) file.length() + " bytes");

            System.out.println("Reading...");

            long startTime = System.nanoTime();
            while (input.read(buffer) != -1)
                System.out.println("Reading from buffer");
            System.out.print("Reading finished in " + (System.nanoTime() - startTime) / 1e6 + " ms\n"); // 1e6 significa 1 multiplicado por 10 elevado a 6, que es 1.000.000 https://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html#jls-3.10.2

        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }

    /**
     * Decodifica los bytes del archivo del flujo.
     */
    private void decode() {
        try (FileInputStream input = new FileInputStream(file)) {

            System.out.println("Name: " + file.getName());
            System.out.println("Path: " + file.getPath());
            System.out.println("Size: " + (int) file.length() + " bytes");

            System.out.println("Decoding...");

            int b, i = 0;
            /* En cada iteracion, el byte leido se imprime como un caracter, como un byte con signo, y como un byte sin signo. El
             * metodo read() del flujo obtiene codepoints como enteros entre 0 y 255 (1 byte). Por ejemplo, el codepoint U+0054
             * representa el caracter 'T', que es 84 en decimal y 01010100 en binario. UTF-8 realiza esta transformacion binaria
             * para que las maquinas puedan entender y transmitir el caracter por la red. */
            while ((b = input.read()) != -1)
                System.out.printf("byte[%d] > char = %c, Int8 = %d, UInt8 = %d%n", i++, (char) b, (byte) b, b);

        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }

    /**
     * Escribe en el archivo existente los bytes del texto desde el flujo de salida.
     */
    private void write() {
        // true si los bytes se escribiran al final del archivo en lugar de al principio
        boolean append = true;
        try (FileOutputStream output = new FileOutputStream(file, append)) {
            output.write("nashe".getBytes()); // Obtiene los bytes del texto codificados por el charset predeterminado de la plataforma
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Stream stream = new Stream(new File(TEXT));
        // stream.nativeRead();
        // stream.read();
        // stream.decode();
    }

}
