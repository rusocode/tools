package multiprocesos;

/**
 * Las condiciones de carrera ocurren solo si varios subprocesos acceden al mismo recurso y uno o más subprocesos
 * escriben en el recurso. Si varios subprocesos leen el mismo recurso, no se producen condiciones de carrera.
 * 
 * Podemos asegurarnos de que los objetos compartidos entre subprocesos nunca sean actualizados por ninguno de los
 * subprocesos haciendo que los objetos compartidos sean inmutables y, por lo tanto, seguros para subprocesos.
 * 
 * Fuentes:
 * http://tutorials.jenkov.com/java-concurrency/thread-safety-and-immutability.html
 * 
 * @author Ru$o
 * 
 */

public class ThreadSafetyAndImmutability {

	/* Observe como se pasa el valor de la instancia ThreadSafetyAndImmutability en el constructor. Observe tambien que no
	 * existe un metodo de establecimiento. Una vez que se crea una instancia de ThreadSafetyAndImmutability, no puede
	 * cambiar su valor. Es inmutable. Sin embargo, puede leerlo utilizando el metodo getValue(). */

	private int value = 0;

	public ThreadSafetyAndImmutability(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

}
