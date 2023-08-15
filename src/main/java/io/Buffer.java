package io;

import java.io.*;

import static utils.Global.*;

/**
 * El concepto de buffer queda muy bien explicado en el siguiente parrafo extraido del libro <b>Head First Java</b>:
 * <i>"Si no hubiera buffers, seria como comprar sin un carrito: deberia llevar los productos uno a uno hasta la caja. Los
 * buffers te dan un lugar en el que dejar temporalmente las cosas hasta que este lleno. Por ello has de hacer menos
 * viajes cuando usas el carrito."</i>
 * <p>
 * Cualquier operacion que implique acceder al disco es muy costosa, por lo que es interesante intentar reducir al
 * maximo las operaciones de lectura/escritura que realizamos sobre los archivos, haciendo que cada operacion lea o
 * escriba muchos datos a la vez. Es importante aclarar que el buffer seria una porcion de codigo que toma una parte de
 * la memoria RAM (volatil) para acceder mucho mas rapido a los datos.
 * <p>
 * Las clases FileOuputStream, FileInputStream, FileWriter y FileReader se utilizan para escribir o leer datos en
 * memoria. Las BufferedOutputStream, BufferedInputStream, BufferedWriter y BufferedReader, añaden un buffer intermedio
 * encargado de controlar el acceso a la memoria.
 * <p>
 * Caracteristicas de los buffers en java:
 * <ul>
 * <li>Si vamos escribiendo, se guardaran los datos hasta que tenga basantes como para hacer la escritura eficiente.
 * <li>Si queremos leer, la clase leera muchos datos de golpe, aunque solo nos de los que hayamos pedido. En las
 * siguientes lecturas nos dara lo que tiene almacenado, hasta que necesite leer otra vez.
 * </ul>
 * Esta forma de trabajar hace que el acceso al disco sea mas eficiente y el programa corra mas rapido. La diferencia se
 * notara mas cuanto mayor sea el archivo a leer o escribir. La clave en las clases que comienzan con Buffered es que
 * usan un buffer. Digamos que es una memoria interna que normalmente hace que esas clases sean mas eficientes, es
 * decir, es esperable que un BufferedInputStream sea mas rapido que el flujo ordinario. El flujo normal tiene que estar
 * llamando y accediendo a la memoria por cada byte que quiera devolver, resultando ineficiente y consumiendo muchos
 * recursos, siempre y cuando no se haya agregado un buffer casero. En cambio el buffer accede a la memoria una vez y
 * recolecta un array de bytes. Cuando se llama al metodo read() ya no tiene que acceder a la memoria, sino que devuelve
 * la informacion del buffer interno. En algun momento el buffer interno se agota, pero mientras esto ocurre se han
 * ahorrado un monton de procesos.
 * <p>
 * Puede agregar lectura y almacenamiento en buffers transparentes y automaticos de un array de bytes desde un
 * FileInputStream utilizando un BufferedInputStream. BufferedInputStream lee un fragmento de bytes en un array
 * de bytes del FileInputStream subyacente. Luego puede leer los bytes uno por uno desde BufferedInputStream y aun asi
 * obtener gran parte de la aceleracion que proviene de leer un array de bytes en lugar de un byte a la vez.
 * <p>
 * Es importante aclarar que un ObjectOutputStream ya realiza internamente un almacenamiento en buffer para mejorar el
 * rendimiento de la escritura en disco. Por lo tanto, no es necesario utilizar adicionalmente un BufferedOutputStream
 * en este caso.
 * <p>
 * En conclusion, los buffers manejan fragmentos de datos y los flujos normales leen/escriben bytes individuales.
 * <br><br>
 * Recursos
 * <a href="https://www.youtube.com/watch?v=baHz_RmMt5I">Java IO - Buffered Streams</a>
 * <a href="https://www.youtube.com/watch?v=lE7HXIJOpDU">The Hard Drive Buffer</a>
 * <a href="https://www.youtube.com/watch?v=x2vegjeJICk">Buffer Cache</a>
 *
 * @author Juan Debenedetti
 */

public class Buffer {

    private final File file;

    public Buffer(File file) {
        this.file = file;
    }

    public static void main(String[] args) {

        Buffer texture = new Buffer(new File(BOLA_AMARILLA2));
        texture.readTexture();

        Buffer text = new Buffer(new File(TEXT_GRANDE));
        // text.readText();
    }

    /**
     * Crea un flujo de entrada para el archivo de imagen y le agrega un buffer de 8192 bytes.
     */
    private void readTexture() {

        BufferedInputStream buffer = null;
        int UInt8, i = 0;

        try {

            buffer = new BufferedInputStream(new FileInputStream(file));

            System.out.println("Archivo: " + file.getName());
            System.out.println("Ruta: " + file.getPath());
            System.out.println("Tamaño: " + buffer.available() + " bytes / " + ((double) buffer.available() / 1024) + " Kb");

            System.out.println("Decodificando...");

            /* El metodo read() lee el siguiente byte de datos del flujo de entrada y se devuelve como un int en el
             * rango de 0 a 255. */
            long startTime = System.nanoTime();
            while ((UInt8 = buffer.read()) != -1)
                System.out.println("bloque " + (i++) + " > UInt8 = " + UInt8);
            long endTime = System.nanoTime();
            System.out.println("\nDuracion: " + (endTime - startTime) / 1e6 + " ms");

        } catch (FileNotFoundException e) {
            System.err.println("El archivo no existe!\nMas informacion...");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error de I/O!\nMas informaion...");
            e.printStackTrace();
        } finally {
            try {
                /* Llamar a close() del buffer tambien cerrara el InputStream subyacente del que esta leyendo BufferedInputStream. No
                 * es necesario que cierre explicitamente InputStream. */
                if (buffer != null) buffer.close();
            } catch (IOException e) {
                System.err.println("No se pudo cerrar el flujo de entrada!\nMas informacion...");
                e.printStackTrace();
            }
        }
    }

    private void readText() {

        BufferedReader buffer = null;
        FileInputStream input = null;

        String linea;

        byte[] bufferCasero = new byte[DEFAULT_BUFFER_SIZE];
        int codepoint;

        try {

            input = new FileInputStream(file);

            buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            /* Calcula el tiempo en leer el archivo de texto usando un BufferedReader. DIFERENCIA ENORME!
             * El metodo readLine() lee una linea de texto. Una linea se considera terminada por cualquiera de un avance de linea
             * ('\n'), un retorno de carro ('\r') o un retorno de carro seguido inmediatamente por un salto de linea. */
            long startTime = System.nanoTime();
            while ((linea = buffer.readLine()) != null)
                System.out.println(linea);
            long endTime = System.nanoTime();

            /* 1e6 significa 1 multiplicado por 10 elevado a 6, que es 1.000.000.
             * Para mas informacion: https://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html#jls-3.10.2 */
            System.out.println("\nDuracion: " + (endTime - startTime) / 1e6 + " ms");

            /* Calcula el tiempo en leer el archivo de texto usando un buffer casero. */
//			long startTime = System.nanoTime();
//			while (input.read(buffer) != -1) {
//				for (int i = 0; i < buffer.length; i++)
//					System.out.print((char) buffer[i]);
//			}
//			long endTime = System.nanoTime();
//			System.out.println("\nDuracion: " + (endTime - startTime) / 1e6 + " ms");

            /* Calcula el tiempo en leer el archivo de texto haciendo llamadas nativas. */
//			long startTime = System.nanoTime();
//			while ((codepoint = input.read()) != -1)
//				System.out.print((char) codepoint);
//			long endTime = System.nanoTime();
//			System.out.println("\nDuracion: " + (endTime - startTime) / 1e6 + " ms");

        } catch (FileNotFoundException e) {
            System.err.println("El archivo no existe!\nMas informacion...");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error de I/O!\nMas informaion...");
            e.printStackTrace();
        } finally {
            try {
                if (buffer != null) buffer.close();
            } catch (IOException e) {
                System.err.println("No se pudo cerrar el flujo de entrada!\nMas informacion...");
                e.printStackTrace();
            }
        }
    }

    private void writeText() {

        BufferedWriter buffer = null;

        try {

            buffer = new BufferedWriter(new FileWriter(file, true));

            buffer.write("Soy el mejor!");

        } catch (FileNotFoundException e) {
            System.err.println("El archivo no existe!\nMas informacion...");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error de I/O!\nMas informaion...");
            e.printStackTrace();
        } finally {
            try {
                if (buffer != null) buffer.close();
            } catch (IOException e) {
                System.err.println("No se pudo cerrar el flujo de entrada!\nMas informacion...");
                e.printStackTrace();
            }
        }

    }

}
