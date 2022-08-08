package genericos2;

/**
 * Crea dos objetos de diferentes tipos desde la clase generica. Es conveniente especificar un tipo, ya que usar Object
 * como tipo causa problemas de seguridad.
 */

public class GenericsTest {

	public static void main(String[] args) {

		Printer<Integer> integerPrinter = new Printer<>(23);
		integerPrinter.print();

		Printer<Object> doublePrinter = new Printer<>(33.5);
		doublePrinter.print();




	}

}
