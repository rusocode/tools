package _lab;

/**
 * <p>
 * Los operadores logicos de bits en Java son operadores que realizan operaciones a nivel de bits sobre valores enteros. Estos
 * operadores trabajan con la representacion binaria de los numeros.
 * Usos comunes:
 * <ul>
 * <li>Manipulacion de flags y mascaras de bits.
 * <li>Optimizacion de ciertas operaciones matematicas.
 * <li>Trabajo con protocolos de red y formatos de archivo de bajo nivel.
 * </ul>
 * <p>
 * Para realizar operaciones a nivel de bit en Java, es crucial considerar la longitud completa predefinida por el tipo de dato.
 * Cada tipo de dato entero tiene una representacion binaria especifica:
 * <ul>
 * <li>byte: 8 bits
 * <li>short: 16 bits
 * <li>int: 32 bits
 * <li>long: 64 bits
 * </ul>
 * <p>
 * Por ejemplo, para un valor 3 de tipo int, su representacion binaria completa es:
 * <p>
 * {@code 00000000 00000000 00000000 00000011}
 * <p>
 * Esta notacion muestra los 32 bits del int, agrupados en bloques de 8 para mayor legibilidad. Aunque los bits significativos
 * estan solo en el extremo derecho, es importante considerar todos los bits al realizar operaciones bitwise, ya que estas afectan
 * a la totalidad de la representacion.
 * <p>
 * Es importante notar que en Java, cuando se realizan operaciones de bits, los tipos byte y short se <a href="https://stackoverflow.com/questions/27122610/why-does-the-java-api-use-int-instead-of-short-or-byte">promocionan</a>
 * automaticamente a int.
 * <h3>Int8 y UInt8</h3>
 * <p>
 * Int8 e UInt8 son tipos de datos utilizados en programacion, especialmente en lenguajes de bajo nivel y en contextos donde se
 * necesita un control preciso sobre el tamaño y el rango de los valores numericos. En UInt8 la "U" significa "Unsigned" (sin
 * signo). "Int" es abreviatura de "Integer" (entero) y "8" indica que usa 8 bits. UInt8 representa un entero sin signo de 8 bits.
 * Esto significa que puede almacenar valores enteros positivos de 0 a 255 (2^8 - 1). Int8 representa un entero con signo de 8
 * bits. Puede almacenar valores enteros tanto positivos como negativos, en el rango de <a href="https://stackoverflow.com/questions/3621067/why-is-the-range-of-bytes-128-to-127-in-java#:~:text=8%20Answers&text=The%20answer%20is%20two">-128 a 127</a>.
 * <p>
 * La principal diferencia entre ambos es que UInt8 no puede representar numeros negativos, pero puede representar numeros
 * positivos mas grandes que Int8.
 * <p>
 * Java en realidad no utiliza Int8 ni UInt8 como tipos de datos primitivos. Java utiliza el tipo 'byte' para representar enteros
 * de 8 bits usando el <a href="https://es.wikipedia.org/wiki/Complemento_a_dos">complemento a dos</a>.
 * <p>
 * Links:
 * <a href="https://es.wikibooks.org/wiki/Programaci%C3%B3n_en_Java/Operadores_de_bits">Operadores de bits</a>
 *
 * @author Juan Debenedetti
 */

public class BitOperators {

    /**
     * Compara cada bit y devuelve 1 si ambos bits son 1, de lo contrario 0.
     * <pre>
     * 1 = 00000001
     * 3 = 00000011
     * 1&3 = 00000001 = 1 </pre>
     */
    private static int and(int a, int b) {
        return a & b;
    }

    /**
     * Devuelve 1 si al menos uno de los bits comparados es 1.
     * <pre>
     * 1 = 00000001
     * 3 = 00000011
     * 1|3 = 00000011 = 3 </pre>
     */
    private static int or(int a, int b) {
        return a | b;
    }

    /**
     * Devuelve 1 si los bits comparados son diferentes, 0 si son iguales.
     * <pre>
     * 1 = 00000001
     * 3 = 00000011
     * 1^3 = 00000010 = 2 </pre>
     */
    private static int xor(int a, int b) {
        return a ^ b;
    }

    /**
     * Invierte todos los bits (complemento a uno). El resultado de ~1 en Java es -2. Vamos a desglosar por que:
     * <ol>
     * <li>Primero, el 1 en representacion binaria de 32 bits es: 00000000 00000000 00000000 00000001
     * <li>El operador ~ invierte todos los bits: 11111111 11111111 11111111 11111110
     * <li>Esta representacion en complemento a dos corresponde al numero -2 en decimal.
     * </ol>
     * Esto tiene sentido matematicamente porque:
     * <ul>
     * <li>En un sistema sin signo, este seria el numero mas grande posible (4,294,967,295).
     * <li>En el sistema de complemento a dos, representa -1.
     * </ul>
     * <p>
     * Es importante notar que aunque Java usa internamente el complemento a dos para representar numeros negativos, el
     * operador ~ realiza la operacion de complemento a uno.
     * <p>
     * Entonces, si calculo el complemento a uno del valor 1 en representacion binaria de 32 bits, esto me da un valor binario
     * de 11111111 11111111 11111111 11111110 ¿Que pasan con los primeros 24 bits con valor 1? ¿Se descartan?
     * <p>
     * Vamos a aclarar esto paso a paso:
     * <ol>
     * <li>Complemento a uno de 1 (32 bits): 11111111 11111111 11111111 11111110
     * <li>Este valor en complemento a dos representa -2.
     * <li>La representacion de -2 en 8 bits seria: 11111110
     * </ol>
     * Ahora, sobre la pregunta acerca de los primeros 24 bits:
     * <p>
     * No se descartan. La razon por la que parece que se "pierden" es por como se manejan los numeros de diferentes tamaños en
     * los sistemas computacionales:
     * <ol>
     * <li>Extension de signo: Cuando se trabaja con numeros de diferentes tamaños (por ejemplo, convertir de 32 bits a 8 bits),
     * se utiliza la tecnica de "extension de signo". Esto significa que el bit mas significativo (el bit de signo) se repite para
     * llenar los bits adicionales.
     * <li>Para numeros negativos en complemento a dos, el bit de signo es 1. Por lo tanto, al reducir de 32 bits a 8 bits, todos
     * los bits adicionales se llenan con 1.
     * <li>Asi, tanto 11111111 11111111 11111111 11111110 (32 bits) como 11111110 (8 bits) representan el mismo numero: -2.
     * <li>Los bits adicionales no se "descartan" en el sentido de perder informacion. Mas bien, son redundantes para la
     * representacion del numero en un formato mas pequeño.
     * <li>Esta propiedad permite que los numeros negativos mantengan su valor correctamente cuando se mueven entre diferentes
     * tamaños de representacion.
     * </ol>
     */
    private static int not(int a) {
        return ~a;
    }

    /**
     * Desplaza los bits a la izquierda, llenando con ceros por la derecha. Los bits a la izquierda se pierden, no es una
     * operacion de rotacion. Hay casos en las que el signo del numero puede cambiar tras una operacion como la siguiente: {@code 1 << 31 = -2147483648}
     * <pre>
     * 1 = 00000001
     * 1 << 2 = 00000100 = 4 </pre>
     */
    private static int leftShift(int a) {
        return a << 2;
    }

    /**
     * Desplaza los bits a la derecha, manteniendo el bit de signo.
     * <pre>
     * -8 = 11111000
     * -8 >> 1 = 11111100 = -4 </pre>
     */
    private static int signedRightShift(int c) {
        return c >> 1;
    }

    /**
     * Desplaza los bits a la derecha, llenando con ceros por la izquierda. Este operador desplaza a la derecha, pero no tiene en
     * cuenta el signo. Siempre agrega el bit con el valor cero, por lo que se llama desplazamiento sin signo.
     * <pre>
     * -8 = 11111111 11111111 11111111 11111000
     * -8 >>> 1 = 01111111 11111111 11111111 11111100 = 2147483644 </pre>
     * Al desplazar, se introduce un 0 en el bit mas significativo, convirtiendo el numero negativo en un positivo muy grande. El
     * resultado es 2147483644, que es (2^31) - 4.
     */
    private static int unsignedRightShift(int c) {
        return c >>> 1;
    }

    public static void main(String[] args) {
        int a = 1, b = 3, c = -8; // Operandos
        // System.out.println(and(a, b));
        // System.out.println(or(a, b));
        // System.out.println(xor(a, b));
        // System.out.println(not(a));
        // System.out.println(leftShift(a));
        // System.out.println(signedRightShift(c));
        System.out.println(unsignedRightShift(c));
    }

}
