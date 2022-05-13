package _LABORATORIO;

/**
 * Como sabran, los datos en una computadora internamente se representan en codigo binario. El microprocesador solo
 * entiende de ceros y unos. Luego, mediante una serie de procesos, nosotros vemos a este codigo ya transformado en
 * numeros, caracteres, imagenes y sonidos. Pero en realidad en la trastienda todo sigue siendo binario.
 *
 * Una operación bit a bit opera sobre números binarios a nivel de sus bits individuales. Es una acción primitiva
 * rápida, soportada directamente por los procesadores. En procesadores simples de bajo costo, las operaciones
 * de bit a bit, junto con los de adición y sustracción, son típicamente sustancialmente más rápidas que la
 * multiplicación y la división, mientras que en los modernos procesadores de alto rendimiento usualmente las
 * operaciones se realizan a la misma velocidad.
 *
 * El metodo mas sencillo de representacion son los numeros naturales. Por ejemplo, si tengo el numero 85 en decimal,
 * solo tengo que llevarlo a binario y obtengo una serie de unos y ceros:
 * 1010101 = 85 en binario
 *
 * Java proporciona operadores para realizar operaciones a nivel de bits para todos los tipos integrales (byte, char,
 * short, int, long):
 * - ~a (complemento) transforma los 0s en 1s y los 1s en 0s en la representacion binaria. Por ejemplo, si el byte b
 * contiene 00000001 (0x01), ~b sera 11111110 (0xFE).
 * - a&b (AND) realiza la operacion binaria AND bit a bit.
 * - a|b (OR) realiza la operacion binaria OR bit a bit.
 * - a^b (XOR) realiza al operacion binaria XOR (o exclusivo) bit a bit.
 * Tambien tenemos operaciones para hacer desplazamientos:
 * - a<<n (left shift) desplaza el patron de bits n posiciones hacia la izquierda (rellena con ceros).
 * - a>>n (signed right shift) desplaza el patron de bits n posiciones hacia la derecha (rellena con el bit de signo).
 * - a>>>n (unsigned left shift) desplaza el patron de bits n posiciones hacia la derecha (rellena con ceros).
 *
 * Para operar a nivel de bit es necesario tomar toda la longitud predefinida para el tipo de dato. Estamos
 * acostumbrados a desechar los ceros a la izquierda en nuestra representacion de numeros. Pero aqui es importante. Si
 * trabajamos una variable de tipo int con un valor de 3, esta representada de la siguiente manera:
 *
 * 00000000000000000000000000000011
 *
 * Aqui los 32 bits de un int se tienen en cuenta.
 *
 * Fuente > <a href="https://es.wikibooks.org/wiki/Programaci%C3%B3n_en_Java/Operadores_de_bits">...</a>
 *
 * @author Juan Debenedetti aka Ru$o
 *
 */

public class Bits {

	private static final int LONGITUD_BINARIA = 8;
	private static final int BASE = 2;

	public static void main(String[] args) {

		// Operandos
		int a = 132, b = 144;

		int decimal = 1;

		// 1 << 4
		// 0000_0000_0000_0000_0000_0000_0000_0001 = 1
		// 0000_0000_0000_0000_0000_0000_0001_0000 = 16

		byte[] binary = {0, 0, 0, 1, 0, 0, 0, 0};

		System.out.println(leftShift(decimal));
		System.out.println(binaryToDecimal(binary));
		System.out.println(and(a, b));

	}

	/*
	 * -Operadores logicos de bits
	 * Estos operadores extienden las operaciones booleanas a los enteros. Para comprender como trabajan debemos descomponer
	 * los enteros en un conjunto de bits. El operador aplicara una operacion logica bit por bit, tomando el valor de uno
	 * como verdadero y el valor de cero como falso. De un operando toma un bit y aplica la operacion al bit que tiene la
	 * misma posicion del segundo operando. Como resultado obtenemos otro entero.
	 */

	/** Si ambos bits comparados son 1, establece el resultado en 1. De lo contrario da como resultado 0. */
	private static int and(int a, int b) {
		// 132______ = 00000000000000000000000010000100
		// 144______ = 00000000000000000000000010010000
		// 132 & 144 = 00000000000000000000000010000000 = 128
		return a & b;
	}

	/**
	 * Si por lo menos uno de los dos bits comparados es 1, establece el resultado en 1. De lo contrario da como resultado
	 * 0.
	 */
	private static int or(int a, int b) {
		// 132______ = 00000000000000000000000010000100
		// 144______ = 00000000000000000000000010010000
		// 132 | 144 = 00000000000000000000000010010100 = 148
		return a | b;
	}

	/**
	 * Si uno de los bits comparados es 0 y el otro 1, el resultado es 1. Si ambos bits comparados son iguales, el resultado
	 * es 0.
	 */
	private static int xor(int a, int b) {
		// 132______ = 00000000000000000000000010000100
		// 144______ = 00000000000000000000000010010000
		// 132 ^ 144 = 00000000000000000000000000010100 = 20
		return a ^ b;
	}

	/**
	 * Solo invierte los bits, es decir, convierte los ceros en unos y viceversa. Observemos que es el unico de esta familia
	 * que tiene un solo operando.
	 */
	private static int not(int a) {
		// 132_ = 00000000000000000000000010000100
		// ~132 = 11111111111111111111111101111011 = -133
		return ~a;
	}

	/*
	 * -Desplazamientos
	 * Los operadores de desplazamiento, mueven los bits a la izquierda o a la derecha. El primer operando sera la victima a
	 * sacudir. El segundo indicara cuantas posiciones.
	 */

	/** Corre el numero 33 dos posiciones a la izquierda. */
	private static int leftShift(int decimal) {
		// 33_____ = 00000000000000000000000000100001
		// 33 << 2 = 00000000000000000000000010000100 = 132
		/* Cada "hueco" que queda a la derecha tras correr este numero se rellena con ceros. Los bits a la izquierda se pierden,
		 * no es una operacion de rotacion. Si prestamos atencion, observaremos que esta operacion multiplico al numero decimal
		 * por 2 tantas veces como posiciones se ha desplazado. En este caso se multiplico por 4 ( 2 x 2 ). Hay que notar que el
		 * signo del numero puede cambiar tras la operacion (por ejemplo 1 << 31 = -2147483648). */
		return decimal << 2;
	}

	/**
	 * Volvamos a colocar como estaban los bits del caso anterior. Queremos obtener nuevamente el numero 33. Para esto
	 * desplazamos el numero 132 dos posiciones a la derecha.
	 */
	private static int signedRightShift(int decimal) {
		// 132_____ = 00000000000000000000000010000100
		// 132 >> 2 = 00000000000000000000000000100001 = 33
		/* Podemos ver que el corrimiento a la derecha realiza una division de enteros. Divide por 2, tantas veces como
		 * posiciones desplazadas. */
		// 132 >> 3 = 00000000000000000000000000010000 = 16
		/* En este caso, cuando se desplaza 3 veces hacia la derecha un bit positivo (1) se pierde. Esto quiere decir que los
		 * "huecos" que quedan a la izquierda se llena con ceros y los bits a la derecha se pierden. */
		return decimal >> 2;
	}

	/**
	 * Veamos que ocurre si pretendemos realizar un desplazamiento a la derecha con un numero negativo. Tengan en cuenta que
	 * la representacion de numeros es de complemento a 2. Si tengo una variable de tipo int con el valor –1 , internamente
	 * esta almacenada de la siguiente forma:
	 * 11111111111111111111111111111111 = -1 complemento a 2
	 */
	private static void signedRightShiftNegative() {
		int x = -1;
		int y = x >> 1;
		System.out.println("El resultado es: " + y);

		/* Quedo exactamente igual. Prueben de correr el numero tantas posiciones como tengan ganas y obtendran el mismo
		 * resultado. Esto ocurre porque en el desplazamiento, los "huecos" que quedan a la izquierda se rellenan con el bit uno
		 * (1), quedando inalterable.
		 *
		 * Este operador desplaza el conjunto de bit a la derecha y agrega a la izquierda los bits que faltan segun el bit de
		 * signo, o sea el mas significativo. Si se encuentra con un numero positivo, el bit de signo vale 0, entonces agrega
		 * ceros, en cambio si son negativos el bit de signo vale 1, entonces agrega unos. Este proceso, denominado extension de
		 * signo mantiene el signo del numero como si se tratara de una division. Por esto se lo conoce como desplazamiento con
		 * signo. */
	}

	/** Modifiquemos ligeramente el programa anterior agregandole al operador un simbolo >. Nos queda de esta manera: */
	private static void unsignedRightShift() {
		int x = -1;
		int y = x >>> 2;
		System.out.println("El resultado es: " + y);

		// Si ejecutamos el programa nos dice lo siguiente:
		// El resultado es: 1073741823

		// Veamos de donde salio este numero raro. Si lo llevamos a binario tenemos:
		// 00111111111111111111111111111111 = 1073741823 en binario

		/* Ahora nos damos cuenta que se han agregado dos ceros a la izquierda. Este operador desplaza a la derecha, pero no
		 * tiene en cuenta el signo. Siempre agrega bit con el valor cero, por lo que se llama desplazamiento sin signo. Este
		 * operador suele ser mas adecuado que el >> cuando queremos manipular los bits mismos, no su representacion
		 * numerica. */
	}

	// https://cual-es-mi-ip.online/herramientas/conversores-numericos/conversor-decimal-a-binario/
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
