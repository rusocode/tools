package genericos2;

import java.util.ArrayList;
import java.util.List;

/**
 * La ventaja de los genericos, es que se pueden usar para crear objetos de diferentes tipos usando la misma clase.
 *
 * <p> Se especifican con operadores diamantes <> y un nombre (convecional T, U, o K) dentro.
 *
 * <p> Este ejemplo crea dos objetos de diferentes tipos desde la clase generica {@code GenericPrinter}.
 *
 * <p> Tambien se utiliza la clase generica {@code GenericAnimal} para poder crear objetos que solo extiendan de la
 * clase {@code Animal}, utilizando la palabra reservada {@code extends} dentro de la especificacion generica.
 *
 * <p> Es conveniente evitar usar Object como tipo, ya que causa problemas de seguridad, ademas de tener que castear
 * cada objecto.
 *
 * <pre> {@code
 * ArrayList<Object> cats = new ArrayList<>();
 * cats.add(new Cat());
 * Cat michi = cats.get(0); // Error de tipo ya que es una lista de Object
 * Cat michi = (Cat) cats.get(0); // Castea a un objeto de tipo Cat
 * } </pre>
 *
 * <br> uentes: <a href="https://www.youtube.com/watch?v=K1iu1kXkVoA">Genericos</a>
 */

public class Launcher {

	public static void main(String[] args) {

		GenericPrinter<Integer> integerPrinter = new GenericPrinter<>(23);
		integerPrinter.print();

		GenericPrinter<Double> doublePrinter = new GenericPrinter<>(33.5);
		doublePrinter.print();

		GenericAnimal<Cat> michi = new GenericAnimal<>(new Cat());
		GenericAnimal<Dog> pipi = new GenericAnimal<>(new Dog());
		// El tipo Integer no esta dentro de los limites de GenericAnimal
		// GenericAnimal<Integer> numero = new GenericAnimal<>(new Integer());

		// genericMethod("Juan", 28);

		List<Integer> intList = new ArrayList<>();
		intList.add(4);
		printList(intList);

	}

	/**
	 * Metodo generico que puede recibir cualquier tipo de parametro y devolverlo.
	 *
	 * @param thing      parametro generico.
	 * @param otherThing otro parametro generico.
	 * @return un generico.
	 */
	private static <T, V> T genericMethod(T thing, V otherThing) {
		System.out.println(thing + "!");
		System.out.println(otherThing + "!");
		return thing;
	}

	/**
	 * Metodo comodin que obtiene una lista de cualquier tipo de objeto y la imprime.
	 *
	 * <p> El comodin en los genericos se utiliza para poder aceptar cualquier tipo de objeto, ya que es algo
	 * desconocido. Tambien se puede utilizar ese "limite" ({@code List<? extends Animal>}) en los comodines para
	 * aceptar clases especificas.
	 */
	private static void printList(List<?> myList) {

	}

}
