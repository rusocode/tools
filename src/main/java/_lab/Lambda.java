package _lab;

import interfaces.Printable;

/**
 * Fuentes: <a href="https://www.youtube.com/watch?v=tj5sLSFjVj4">Expresiones lambda</a>
 */

public class Lambda {

	public static void main(String[] args) {

		// printThing(new Cat());

		/* En vez de mandar un objeto Cat, se puede enviar la accion usando el operador lambda, dejando el codigo mas
		 * limpio. Es importante aclarar que al ser una sola expresion, se pueden omitir las llaves. */
		printThing((s) -> System.out.println("Meow" + s));
	}

	/**
	 * @param printable objeto que implementa la interface Printable.
	 */
	private static void printThing(Printable printable) {
		printable.print("!");
	}

}
