package _lab;

/**
 * Los datos en una computadora internamente se representan en codigo binario. El microprocesador solo entiende de ceros
 * y unos. Luego, mediante una serie de procesos, nosotros vemos a este codigo ya transformado en numeros, caracteres,
 * imagenes y sonidos. Pero en realidad en la trastienda todo sigue siendo binario.
 * <p>
 * Una operacion bit a bit opera sobre numeros binarios a nivel de sus bits individuales. Es una accion primitiva
 * rapida, soportada directamente por los procesadores. En procesadores simples de bajo costo, las operaciones
 * de bit a bit, junto con los de adicion y sustraccion, son tipicamente sustancialmente mas rapidas que la
 * multiplicacion y la division, mientras que en los modernos procesadores de alto rendimiento usualmente las
 * operaciones se realizan a la misma velocidad.
 * <p>
 * El metodo mas sencillo de representacion son los numeros naturales. Por ejemplo, si tengo el numero 85 en decimal,
 * solo tengo que llevarlo a binario y obtengo una serie de unos y ceros: 1010101 = 85 en decimal.
 * <p>
 * Java proporciona diferentes operadores para realizar operaciones a nivel de bits para todos los tipos integrales
 * (byte, char, short, int, long):
 * <ul>
 * <li>{@code a&b} realiza la operacion binaria AND bit a bit.
 * <li>{@code a|b} realiza la operacion binaria OR bit a bit.
 * <li>{@code a^b} realiza al operacion binaria XOR (o exclusivo) bit a bit.
 * <li>{@code ~a (complemento)} transforma los 0s en 1s y los 1s en 0s en la representacion binaria. Por ejemplo, si el
 * byte a contiene 00000001 (0x01), ~a sera 11111110 (0xFE).
 * </ul>
 * <p>
 * Tambien tenemos operaciones para hacer desplazamientos:
 * <li>{@code a<<n (left shift)} desplaza el patron de bits n posiciones hacia la izquierda (rellena con ceros).
 * <li>{@code a>>n (signed right shift)} desplaza el patron de bits n posiciones hacia la derecha (rellena con el bit de signo).
 * <li>{@code a>>>n (unsigned left shift)} desplaza el patron de bits n posiciones hacia la derecha (rellena con ceros).
 * </ul>
 * <p>
 * Para operar a nivel de bit es necesario tomar toda la longitud predefinida para el tipo de dato. Estamos
 * acostumbrados a desechar los ceros a la izquierda en nuestra representacion de numeros. Pero aqui es importante. Si
 * trabajamos una variable de tipo int con un valor de 3, esta representada de la siguiente manera:
 * <p>
 * {@code 0000_0000_0000_0000_0000_0000_0000_0011}
 * <p>
 * Aqui los 32 bits de un int se tienen en cuenta.
 * <p>Recursos:
 * <a href="https://es.wikibooks.org/wiki/Programaci%C3%B3n_en_Java/Operadores_de_bits">Operadores de bits</a>
 * <a href="https://cual-es-mi-ip.online/herramientas/conversores-numericos/conversor-decimal-a-binario/">Conversor decimal a binario</a>
 *
 * @author Juan Debenedetti
 */

public class Bits {

    private static final int LONGITUD_BINARIA = 8;
    private static final int BASE = 2;

    public static void main(String[] args) {

        byte[] binary = {1, 1, 1, 1, 1, 1, 1, 1};

        // Operadores logicos
        int a = 1, b = 3; // Operandos
        // System.out.println(and(a, b));
        // System.out.println(or(a, b));
        // System.out.println(xor(a, b));
        // System.out.println(not(a));

        // Desplazamientos
        int c = 1, d = 4, x = -1; // Operandos
        // System.out.println(leftShift(c));
        // System.out.println(signedRightShift(d));
        // System.out.println(signedRightShiftNegative(x));
        System.out.println(unsignedRightShift(x));

    }

    /* Operadores logicos de bits
     * Estos operadores extienden las operaciones booleanas a los enteros. Para comprender como trabajan debemos
     * descomponer los enteros en un conjunto de bits. El operador aplicara una operacion logica bit por bit, tomando el
     * valor 1 como verdadero y el 0 como falso. De un operando toma un bit y aplica la operacion al bit que tiene la
     * misma posicion del segundo operando. Como resultado obtenemos otro bit. */

    /**
     * Si ambos bits comparados son 1, el resultado es 1. De lo contrario es 0.
     * <p>____1 = 0001
     * <p>____3 = 0011
     * <p>1 & 3 = 0001 = 1
     */
    private static int and(int a, int b) {
        return a & b;
    }

    /**
     * Si por lo menos uno de los dos bits comparados es 1, el resultado es 1. De lo contrario es 0.
     * <p>____1 = 0001
     * <p>____3 = 0011
     * <p>1 | 3 = 0011 = 3
     */
    private static int or(int a, int b) {
        return a | b;
    }

    /**
     * Si uno de los bits comparados es 0 y el otro 1, el resultado es 1. Si ambos bits comparados son iguales, el
     * resultado es 0.
     * <p>____1 = 0001
     * <p>____3 = 0011
     * <p>1 ^ 3 = 0010 = 2
     */
    private static int xor(int a, int b) {
        return a ^ b;
    }

    /**
     * Solo invierte los bits, es decir, convierte los ceros en unos y viceversa. Es el unico de esta familia que tiene
     * un solo operando.
     * <p>_1 = 0001
     * <p>~1 = 1110 = 2 = -2
     */
    private static int not(int a) {
        return ~a;
    }

    /* Desplazamientos de bits
     * Los operadores de desplazamiento, mueven los bits a la izquierda o a la derecha. El primer operando sera la
     * victima a sacudir. El segundo indicara cuantas posiciones. */

    /**
     * Desplaza los bits a la izquierda.
     * <p>Los "huecos" que quedan a la derecha tras correr los bits se rellenan con ceros. Los bits a la izquierda se
     * pierden, no es una operacion de rotacion. Esta operacion multiplica el numero decimal tantas veces como
     * posiciones se ha desplazado. Hay casos en las que el signo del numero puede cambiar tras una operacion como la
     * siguiente: 1 << 31 = -2147483648.
     * <p>_____1 = 0001
     * <p>1 << 2 = 0100 = 4
     */
    private static int leftShift(int c) {
        return c << 2;
    }

    /**
     * Desplaza los bits a la derecha teniendo en cuenta el signo
     * <p>En este caso, cuando se desplaza 3 veces hacia la derecha un bit positivo, se pierde. Esto quiere decir que
     * los "huecos" que quedan a la izquierda se rellenan con ceros y los bits a la derecha se pierden. Esta operacion
     * divide el numero decimal tantas veces como posiciones se ha desplazado.
     * <p>_____4 = 0100
     * <p>4 >> 3 = 0000 = 0
     */
    private static int signedRightShift(int d) {
        return d >> 3;
    }

    /**
     * Desplaza los bits a la derecha utilizando un numero negativo
     * <p>Si tengo una variable de tipo int con el valor –1, internamente esta almacenada de la siguiente forma:
     * 1111_1111_1111_1111_1111_1111_1111_1111
     * <p>La representacion de este numeros es de complemento a 2.
     *
     * <p>Este operador desplaza el conjunto de bits a la derecha y agrega a la izquierda los bits que faltan segun el
     * bit de signo, o sea el mas significativo. Si se encuentra con un numero positivo, el bit de signo vale 0,
     * entonces agrega ceros, en cambio si es negativo, el bit de signo vale 1. Este proceso denominado extension de
     * signo, mantiene el signo del numero como si se tratara de una division. Por esto se lo conoce como desplazamiento
     * con signo. Por lo tanto, el resultado del desplazamiento (corriendo el numero tantas veces como quiera) no se
     * altera.
     */
    private static int signedRightShiftNegative(int x) {
        return x >> 20;
    }

    /**
     * Desplaza los bits a la derecha sin tener en cuenta el signo
     * Ahora nos damos cuenta que se han agregado dos ceros a la izquierda. Este operador desplaza a la derecha, pero no
     * tiene en cuenta el signo. Siempre agrega bit con el valor cero, por lo que se llama desplazamiento sin signo.
     * Este operador suele ser mas adecuado que el >> cuando queremos manipular los bits mismos, no su representacion
     * numerica.
     *
     * <p>______-1 = 11111111111111111111111111111111
     * <p>-1 >>> 2 = 00111111111111111111111111111111 = 1073741823
     */
    private static int unsignedRightShift(int x) {
        return x >>> 2;
    }

    private static String decimalToBinary(int decimal) {
        StringBuilder binary = new StringBuilder();
        StringBuilder reverse = new StringBuilder();

        for (int i = 0; i < LONGITUD_BINARIA; i++) {
            if (decimal % 2 == 0) binary.append("0");
            else binary.append("1");
            decimal /= 2;
        }

        for (int i = LONGITUD_BINARIA - 1; i >= 0; i--)
            reverse.append(binary.charAt(i));

        return reverse.toString();
    }

    private static int binaryToDecimal(byte[] binary) {
        int decimal = 0;

        for (int i = binary.length - 1; i >= 0; i--)
            // Si el bit es 1, entonces calcula la potencia con base 2 y exponente del bit actual
            if (binary[i] == 1) decimal += Math.pow(BASE, 7 - i);

        return decimal;
    }

}
