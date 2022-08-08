package genericos2;

/**
 * La ventaja de usar genericos, es que se pueden usar para cualquier tipo.
 *
 * <p> Se especifican usando operadores diamantes <> con un nombre convecional T, U, K, etc.
 */

public class Printer<T> {

	T thingToPrint;

	public Printer(T thingToPrint) {
		this.thingToPrint = thingToPrint;
	}

	public void print() {
		System.out.println(thingToPrint);
	}

}
