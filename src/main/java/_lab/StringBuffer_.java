package _lab;

import java.util.Date;

/**
 * <p>
 * StringBuffer es una clase en Java diseñada para manipular cadenas de caracteres de manera eficiente. A diferencia de la clase
 * String, StringBuffer es mutable, lo que significa que su contenido puede ser modificado sin crear nuevos objetos. Esta
 * caracteristica la hace mas rapida que String en escenarios donde se requieren multiples modificaciones de una cadena. Al igual
 * que String, StringBuffer es thread-safe (sincronizada), lo que la hace segura para su uso en aplicaciones multihilo.
 * <p>
 * Se recomienda usar StringBuffer cuando:
 * <ol>
 * <li>Se necesitan realizar multiples modificaciones a una cadena.
 * <li>La cadena sera modificada desde varios hilos simultaneamente.
 * </ol>
 * <p>
 * Sin embargo, es importante añadir que:
 * <ol>
 * <li>Si la aplicacion no es multihilo, se recomienda usar StringBuilder en lugar de StringBuffer.
 * <li>StringBuilder es similar a StringBuffer pero no es thread-safe, lo que la hace mas rapida en entornos de un solo hilo.
 * <li>La eleccion entre StringBuffer y StringBuilder depende del contexto de uso: multihilo vs. un solo hilo.
 * </ol>
 * <p>
 * En resumen, StringBuffer es una herramienta util para manipular cadenas de manera eficiente en entornos multihilo, mientras que
 * StringBuilder es preferible en aplicaciones de un solo hilo debido a su mayor rendimiento.
 */

public class StringBuffer_ {

    public static void main(String[] args) {
        // test();
        measureTime(10000);
    }

    private static void test() {
        StringBuffer sb = new StringBuffer(1000);
        System.out.println("hashCode = " + sb.hashCode());
        // Agrega cadenas sin crear nuevos objetos (mutable)
        sb.append("rulo");
        sb.append("drogadicto");
        sb.append("estas");
        sb.append("re");
        sb.append("quemado");
        // Aacepta objetos y tipos primitivos de cualquier tipo
        sb.append('R');
        sb.append(234 + "\n");
        sb.append(new Date());
        sb.append(false);
        System.out.println("hashCode = " + sb.hashCode()); // El hashCode sigue siendo el mismo
        // System.out.println(sb.toString()); // Muestra la cadena completa
    }

    /**
     * Measures the time it takes to add a specified number of strings.
     *
     * @param count number of strings.
     */
    private static void measureTime(int count) {
        StringBuffer str = new StringBuffer();
        long startTime = System.nanoTime();
        str.append("str".repeat(count));
        System.out.println("Time = " + (System.nanoTime() - startTime) / 1e6 + " ms");
    }

}
