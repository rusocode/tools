package com.punkipunk._lab;

/**
 * <b>Math.random()</b>:
 * <ul>
 * <li>Es un metodo estatico de la clase Math.
 * <li>Retorna un double entre 0.0 (inclusivo) y 1.0 (exclusivo).
 * <li>Internamente, usa una instancia estatica de Random.
 * <li>Es mas simple de usar para casos basicos.
 * <li>No se puede establecer una semilla directamente.
 * </ul>
 * <b>Random</b>:
 * <ul>
 * <li>Es una clase completa con varios metodos para generar diferentes tipos de numeros aleatorios.
 * <li>Permite generar ints, longs, floats, doubles, y booleanos.
 * <li>Puedes crear multiples instancias con diferentes semillas.
 * <li>Ofrece mas control y flexibilidad.
 * <li>Es thread-safe en su uso concurrente.
 * </ul>
 * Principales diferencias y consideraciones:
 * <ol>
 * <li>Versatilidad: Random es mas versatil, ofreciendo metodos para generar diferentes tipos de numeros aleatorios.
 * <li>Control: Con Random, puedes establecer una semilla, lo que es util para pruebas o cuando necesitas reproducibilidad.
 * <li>Rendimiento: Para usos intensivos, crear una instancia de Random y reutilizarla es mas eficiente que llamar repetidamente a
 * Math.random().
 * <li>Rango: Math.random() siempre da un double entre 0.0 y 1.0, mientras que Random puede generar numeros en diferentes rangos
 * directamente.
 * <li>Thread-safety: Aunque Math.random() es seguro para uso concurrente, usar una instancia compartida de Random entre hilos
 * puede llevar a problemas de contencion. Para uso intensivo en entornos multi-hilo, se recomienda {@code ThreadLocalRandom}.
 * </ol>
 * <p>
 * En general, Math.random() es conveniente para necesidades simples y rapidas de numeros aleatorios, mientras que la clase Random
 * ofrece mas control y es preferible para usos mas avanzados o cuando se necesita generar muchos numeros aleatorios
 * eficientemente.
 */

public class Random {

    public static void main(String[] args) {
        java.util.Random random = new java.util.Random();
        // Genera 5 numeros aleatorios entre 0 y 9 (ambos incluidos)
        for (int i = 0; i < 5; i++)
            System.out.print(random.nextInt(10) + ", ");

        // La semilla es el valor inicial del estado interno del generador de numeros pseudoaleatorios que se mantiene mediante el metodo next
        java.util.Random randomWithSeed = new java.util.Random(5);
        // for (int i = 0; i < 5; i++)
        // System.out.print(randomWithSeed.nextInt() + ", ");

    }

    /**
     * <p>
     * El metodo {@code Math.random()} genera numeros pseudoaleatorios de tipo double entre 0.0 (incluido) y 1.0 (excluido),
     * distribuidos uniformemente. Aunque util, este rango limitado a menudo no es directamente aplicable en situaciones
     * practicas, como simular una tirada de dados. Para adaptar estos valores a rangos especificos, como numeros enteros del 1 al
     * 6 para un dado, se requiere una transformacion. Esto se logra multiplicando el resultado de Math.random() por el numero
     * maximo deseado (en este caso, 6), sumando 1 para evitar el cero, y convirtiendo el resultado a un entero. Esta tecnica
     * permite generar numeros aleatorios en rangos personalizados a partir del metodo basico Math.random(), ampliando su utilidad
     * en diversas aplicaciones.
     */
    public static int rollDice() {
        return (int) (Math.random() * 6 + 1);
    }

}
