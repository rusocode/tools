package interfaces;

/**
 * Las interfaces funcionales solo pueden tener un metodo abstracto y se definen con la anotacion {@code @FunctionalInterface}.
 */

@FunctionalInterface
public interface Printable {
	void print(String suffix);
}
