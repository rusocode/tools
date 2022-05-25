package concurrency;

/**
 * Las condiciones de carrera ocurren solo si varios subprocesos acceden al mismo recurso y uno o más subprocesos
 * escriben en el recurso. Si varios subprocesos leen el mismo recurso, no se producen condiciones de carrera.
 * <br>
 * Fuentes:
 * <a href="http://tutorials.jenkov.com/java-concurrency/thread-safety-and-immutability.html">jenkov.com</a>
 *
 * @author Ruso
 */

public class ImmutableValue {

	/* Podemos asegurarnos de que los objetos compartidos entre subprocesos nunca sean actualizados por ninguno de los
	 * subprocesos haciendo que los objetos compartidos sean inmutables y, por lo tanto, seguros para subprocesos. Aqui hay
	 * un ejemplo: */

	private int value;

	public ImmutableValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

	/* Si necesita realizar operaciones en la instancia de ImmutableValue, puede hacerlo devolviendo una nueva
	 * instancia con el valor resultante de la operacion.
	 *
	 * Observe como el metodo add() devuelve una nueva instancia de ImmutableValue con el resultado de la operacion de
	 * adicion, en lugar de agregar el valor a si mismo. */
	public ImmutableValue add(int valueToAdd) {
		return new ImmutableValue(this.value + valueToAdd);
	}

	public static void main(String[] args) {

		/* Observe como se pasa el valor de la instancia ImmutableValue en el constructor. Observe tambien que no
		 * existe un metodo de establecimiento. Una vez que se crea una instancia de ImmutableValue, no puede
		 * cambiar su valor. Es inmutable. Sin embargo, puede leerlo utilizando el metodo getValue(). */
		ImmutableValue immutable = new ImmutableValue(4);
		System.out.println(immutable.getValue());

	}
}

/**
 * <h3>¡La referencia no es segura para subprocesos!</h3>
 * Es importante recordar que incluso si un objeto es inmutable y, por lo tanto, seguro para subprocesos, es posible que
 * la referencia a este objeto no sea seguro para subprocesos.
 * <p>
 * La clase Calculator contiene una referencia a una instancia de ImmutableValue. Observe como es posible cambiar esa
 * referencia a traves de los metodos setValue() y add(). Por lo tanto, incluso si la clase Calculadora usa un objeto
 * inmutable internamente, no es inmutable en si mismo y, por lo tanto, no es seguro para subprocesos. En otras
 * palabras: la clase ImmutableValue es segura para subprocesos, pero su uso no lo es. Esto es algo a tener en cuenta al
 * intentar lograr la seguridad de los hilos mediante la inmutabilidad.
 * <p>
 * Para hacer que la clase Calculator sea segura, podria haber declarado sincronizados los metodos getValue(),
 * setValue() y add(). Eso habria hecho el truco.
 */
class Calculator {

	private ImmutableValue currentValue = null;

	public ImmutableValue getValue() {
		return currentValue;
	}

	public void setValue(ImmutableValue newValue) {
		this.currentValue = newValue;
	}

	public void add(int newValue) {
		this.currentValue = this.currentValue.add(newValue);
	}
}
