package com.punkipunk.nio;

/**
 * <p>
 * ByteBuffer es una clase en Java que proporciona operaciones para manejar secuencias de bytes. Caracteristicas principales:
 * <ol>
 * <li>Definicion: ByteBuffer es una clase del paquete java.com.punkipunk.nio que encapsula un buffer de bytes, permitiendo operaciones de
 * lectura y escritura.
 * <li>Caracteristicas principales:
 * <ul>
 * <li>Es una subclase de Buffer, especializada para trabajar con bytes.
 * <li>Puede ser directo (memoria fuera del heap de Java) o no directo (memoria dentro del heap).
 * <li>Permite operaciones de lectura y escritura tanto en modo big-endian como little-endian.
 * </ul>
 * <li>Creacion: Se puede crear de varias formas, por ejemplo:
 * <pre>{@code
 * ByteBuffer buffer = ByteBuffer.allocate(1024); // Crea un buffer no directo
 * ByteBuffer directBuffer = ByteBuffer.allocateDirect(1024); // Crea un buffer directo
 * }</pre>
 * <li>Operaciones principales:
 * <ul>
 * <li>put(): Escribe bytes en el buffer.
 * <li>get(): Lee bytes del buffer.
 * <li>flip(): Prepara el buffer para una operacion de lectura despues de escribir.
 * <li>clear(): Prepara el buffer para una nueva secuencia de operaciones de escritura.
 * </ul>
 * <li>Ventajas:
 * <ul>
 * <li>Eficiente para operaciones de I/O, especialmente con grandes volumenes de datos.
 * <li>Permite trabajar con datos binarios de manera mas eficiente que los arrays de bytes tradicionales.
 * </ul>
 * <li>Usos comunes:
 * <ul>
 * <li>Operaciones de I/O de archivos y redes.
 * <li>Procesamiento de datos binarios.
 * <li>Implementacion de protocolos de red personalizados.
 * </ul>
 * <li>Consideraciones de rendimiento: Los buffers directos pueden ofrecer mejor rendimiento en algunas operaciones de I/O, pero
 * tienen un costo de creacion mas alto.
 * <li>Seguridad de tipos: Proporciona metodos para leer y escribir tipos primitivos especificos (getInt(), putLong(), etc.),
 * manteniendo la seguridad de tipos.
 * </ol>
 * <p>
 * ByteBuffer es especialmente util en aplicaciones que requieren un manejo eficiente de datos binarios o en situaciones donde se
 * necesita un control preciso sobre la memoria y las operaciones de I/O.
 * <p>
 * Un ByteBuffer puede manejar enteros completos (int) de 32 bits, no solo bytes. Cuando agregas un int a un ByteBuffer, se
 * mantienen los 32 bits completos, no se castea automaticamente a byte. El metodo {@code buffer.putInt(int value);} escribe un
 * entero completo de 32 bits y {@code buffer.getInt()} lee un entero completo de 32 bits.
 * <p>
 * Links:
 * <a href="https://www.youtube.com/watch?v=iwSCtxMbBLI">Beyond ByteBuffers by Brian Goetz</a>
 *
 * @author Juan Debenedetti
 */

public class ByteBuffer {

    public static void main(String[] args) {

        /* La diferencia clave entre flip() y clear() esta en como manejan el limite. El metodo flip() establece el limite en la
         * posicion actual, preservando la cantidad de datos escritos. El metodo clear() establece el limite en la capacidad total,
         * permitiendo sobrescribir todo el buffer. Esto permite escribir mas bytes despues de clear() que los que se escribieron
         * originalmente. flip() preserva la cantidad de datos escritos, mientras que clear() permite utilizar toda la capacidad
         * del buffer nuevamente. */

        final int capacity = 10;

        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(capacity);

        // Escribir solo 3 bytes
        // buffer.put((byte) 1).put((byte) 2);
        // buffer.put((byte) 3);

        System.out.println("Despues de agregar los bytes...");
        System.out.println("Posicion: " + buffer.position() + ", Limite: " + buffer.limit());

        // Usando flip()
        buffer.flip();
        System.out.println("\nDespues de flip():");
        System.out.println("Posicion: " + buffer.position() + ", Limite: " + buffer.limit());
        System.out.print("Contenido: ");
        while (buffer.hasRemaining())
            System.out.print(buffer.get() + " ");

        /* Como el limite se establecio en la ultima posicion del byte agregado, el buffer no puede agregar mas bytes. En caso de
         * que se agrege otro byte se lanza BufferOverflowException. Por eso se dice que al usar flip, el buffer solo se puede
         * leer y no escribir en el, ya que su unica funcionalidad es voltearlo para poder leerlo (modo lectura). */
        // buffer.put((byte) 4); // BufferOverflowException

        // Usando clear()
        buffer.clear();
        System.out.println("\n\nDespues de clear():");
        System.out.println("Posicion: " + buffer.position() + ", Limite: " + buffer.limit());

        // Escribe 5 bytes despues de clear
        for (int i = 1; i <= 5; i++)
            /* Ahora el buffer puede agregar otros bytes ya que los anteriores bytes se "descartaron" al usar clear, aunque los
             * datos antiguos siguen ahi (modo escritura). */
            buffer.put((byte) i);

        buffer.flip();
        System.out.println("\nDespues de escribir 5 bytes y flip():");
        System.out.println("Posicion: " + buffer.position() + ", Limite: " + buffer.limit());
        System.out.print("Contenido: ");
        while (buffer.hasRemaining())
            System.out.print(buffer.get() + " ");

    }
}
