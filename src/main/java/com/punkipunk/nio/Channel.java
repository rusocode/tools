package com.punkipunk.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static com.punkipunk.utils.Global.*;

/**
 * <p>
 * El paquete NIO proporciona un nuevo modelo de I/O basado en canales, buffers y selectores, siendo una mejora del flujo estandar
 * del paquete IO. Tambien habilita el no bloqueo de I/O y permite mejorar el rendimiento de las aplicaciones distribuidas
 * (principalmente para el lado del servidor).
 * <p>
 * Los canales (channels) en programacion de I/O representan conexiones abiertas a entidades como dispositivos de hardware,
 * archivos o conexiones de red. Actuan como intermediarios para operaciones de entrada/salida, utilizando buffers para mover
 * datos eficientemente. Caracteristicas principales:
 * <ol>
 * <li>Funcionan como nexos para operaciones de I/O.
 * <li>Se usan en protocolos cliente/servidor para la conexion y transferencia de datos.
 * <li>Pueden operar en modo de bloqueo o no bloqueo.
 * <li>Representan conexiones a diversas fuentes de I/O (pipes, sockets, archivos, datagramas).
 * <li>Estan diseñados para ser seguros en entornos multiproceso.
 * <li>Permiten una lectura y escritura mas eficiente de datos.
 * </ol>
 * <p>
 * Para archivos especificamente, los canales se pueden crear mediante el metodo {@code open()} o obtenerse de objetos existentes
 * como FileInputStream, FileOutputStream o RandomAccessFile usando el metodo {@code getChannel()}. El estado del canal esta
 * estrechamente vinculado al objeto que lo genero.
 *
 * @author Juan Debenedetti
 */

public class Channel {

    public static void main(String[] args) {
        /* Este codigo lee un archivo utilizando un FileChannel y un ByteBuffer. Lee el archivo en bloques de 7 bytes, imprime
         * informacion sobre cada byte leido (su posicion en el buffer, el limite del buffer y el caracter correspondiente), y
         * continua hasta que se haya leido todo el archivo. Es una demostracion del uso de canales y buffers para I/O eficiente
         * en Java. */
        try (RandomAccessFile file = new RandomAccessFile(TEXT, "rw")) {
            // Se obtiene un FileChannel del RandomAccessFile que se usara para leer el archivo
            FileChannel channel = file.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(7);
            // Lee una secuencia de bytes de este canal en el buffer dado
            while (channel.read(buffer) != -1) {
                buffer.flip();
                while (buffer.hasRemaining())
                    System.out.println("position = " + buffer.position() + ", limit = " + buffer.limit() + ", char = " + (char) buffer.get());
                buffer.clear();
            }
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }

}
