package genericos2;

/**
 * La ventaja de usar genericos, es que se pueden usar para cualquier tipo.
 *
 * <p> Se especifican usando operadores diamantes <> con un nombre convecional T, U, K, etc.
 *
 * <p> Crea dos objetos de diferentes tipos desde la clase generica. Es conveniente especificar un tipo, ya que usar Object
 * como tipo causa problemas de seguridad.
 */

public class Launcher {

	public static void main(String[] args) {

		GenericPrinter<Integer> integerPrinter = new GenericPrinter<>(23);
		integerPrinter.print();

		GenericPrinter<Object> doublePrinter = new GenericPrinter<>(33.5);
		doublePrinter.print();


	}

}
