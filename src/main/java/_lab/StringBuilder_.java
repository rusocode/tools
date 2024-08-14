package _lab;

/**
 * <p>
 * StringBuilder es una clase en Java que proporciona una secuencia mutable de caracteres. Diseñada como una alternativa mas
 * eficiente a StringBuffer, ofrece una API similar pero sin garantias de sincronizacion. Sus principales caracteristicas son:
 * <ol>
 * <li>Mutabilidad: Permite modificar su contenido sin crear nuevos objetos.
 * <li>Almacenamiento: Como objeto, se almacena en el heap.
 * <li>Capacidad inicial: Por defecto, se crea con una capacidad de 16 caracteres, aunque esto es ampliable.
 * <li>No thread-safe: No esta sincronizada, lo que puede causar problemas de interferencia en entornos multihilo.
 * <li>Rendimiento: Es significativamente mas rapida que StringBuffer en la mayoria de las implementaciones, especialmente en
 * operaciones de un solo hilo.
 * <li>Uso recomendado: Es ideal para situaciones donde la cadena sera modificada desde un solo hilo.
 * <li>Preferencia sobre StringBuffer: Se recomienda usar StringBuilder en lugar de StringBuffer siempre que sea posible,
 * especialmente en contextos de un solo hilo, debido a su mayor rendimiento.
 * <li>Flexibilidad: Proporciona metodos para insertar, eliminar y modificar caracteres en cualquier posicion de la secuencia.
 * </ol>
 * <p>
 * En resumen, StringBuilder es la opcion preferida para manipular cadenas de forma eficiente en aplicaciones de un solo hilo,
 * ofreciendo un mejor rendimiento que StringBuffer y String en escenarios que requieren multiples modificaciones de cadenas.
 */

public class StringBuilder_ {

    public static void main(String[] args) {
        measureTime(10000);
    }

    /**
     * Measures the time it takes to add a specified number of strings.
     *
     * @param count number of strings.
     */
    private static void measureTime(int count) {
        StringBuilder str = new StringBuilder();
        long startTime = System.nanoTime();
        str.append("str".repeat(count));
        System.out.println("Time = " + (System.nanoTime() - startTime) / 1e6 + " ms");
    }

}
