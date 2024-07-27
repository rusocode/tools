package concurrency;

/**
 * <p>
 * Las condiciones de carrera ocurren solo si varios subprocesos acceden al mismo recurso <b>y</b> uno o mas subprocesos <b>escriben</b>
 * en el recurso. Si varios subprocesos leen el mismo recurso, no se producen condiciones de carrera.
 * <p>
 * Podemos asegurarnos de que los objetos compartidos entre subprocesos nunca sean actualizados por ninguno de los subprocesos
 * haciendo que los objetos compartidos sean inmutables y, por lo tanto, seguros para subprocesos. Aqui hay un ejemplo:
 * <pre>{@code
 * public class ImmutableValue {
 *
 *     private int value = 0;
 *
 *     public ImmutableValue(int value) {
 *         this.value = value;
 *     }
 *
 *     public int getValue() {
 *         return value;
 *     }
 *
 * }
 * }</pre>
 * <p>
 * Observe como se pasa el valor de la instancia {@code ImmutableValue} en el constructor. Observe tambien que no existe un metodo
 * de establecimiento. Una vez que se crea una instancia de ImmutableValue, no puede cambiar su valor. Es inmutable. Sin embargo,
 * puedes leerlo utilizando el metodo {@code getValue()}.
 * <p>
 * Si necesita realizar operaciones en la instancia ImmutableValue puede hacerlo devolviendo una nueva instancia con el valor
 * resultante de la operacion. A continuacion se muestra un ejemplo de una operacion de adicion:
 * <pre>{@code
 * public class ImmutableValue {
 *
 *     private int value = 0;
 *
 *     public ImmutableValue(int value) {
 *         this.value = value;
 *     }
 *
 *     public int getValue() {
 *         return value;
 *     }
 *
 *     public ImmutableValue add(int valueToAdd) {
 *         return new ImmutableValue(value + valueToAdd);
 *     }
 *
 * }
 * }</pre>
 * <p>
 * Observe como el metodo {@code add()} devuelve una nueva instancia de ImmutableValue con el resultado de la operacion de adicion,
 * en lugar de agregarse el valor a si mismo.
 * <h3>¡La referencia no es segura para subprocesos!</h3>
 * <p>
 * Es importante recordar que incluso si un objeto es inmutable y, por lo tanto, seguro para subprocesos, la referencia a este
 * objeto puede no ser segura para subprocesos. Mira este ejemplo:
 * <pre>{@code
 * public class Calculator {
 *
 *     private ImmutableValue currentValue;
 *
 *     public ImmutableValue getValue() {
 *         return currentValue;
 *     }
 *
 *     public void setValue(ImmutableValue newValue) {
 *         currentValue = newValue;
 *     }
 *
 *     public void add(int newValue) {
 *         currentValue = currentValue.add(newValue);
 *     }
 *
 * }
 * }</pre>
 * <p>
 * La clase {@code Calculator} contiene una referencia a una instancia de {@code ImmutableValue}. Observe como es posible cambiar
 * esa referencia mediante los metodos {@code setValue()} y add(). Por lo tanto, incluso si la clase Calculadora usa un objeto
 * inmutable internamente, no es en si mismo inmutable y, por lo tanto, no es seguro para subprocesos. En otras palabras: la clase
 * ImmutableValue es segura para subprocesos, pero <b>su uso</b> no lo es. Esto es algo a tener en cuenta al intentar lograr la
 * seguridad de los subprocesos mediante la inmutabilidad.
 * <p>
 * Para hacer que el subproceso de la clase Calculator sea seguro, podria haber declarado sincronizados los metodos getValue(),
 * setValue() y add(). Eso habria funcionado.
 * <p>
 * Links:
 * <a href="http://tutorials.jenkov.com/java-concurrency/thread-safety-and-immutability.html">Thread Safety and Immutability</a>
 *
 * @author Juan Debenedetti
 */

public class ThreadSafetyAndImmutability {

    private final int value;

    public ThreadSafetyAndImmutability(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public ThreadSafetyAndImmutability add(int valueToAdd) {
        return new ThreadSafetyAndImmutability(value + valueToAdd);
    }

    private static class Calculator {

        private ThreadSafetyAndImmutability currentValue;

        public ThreadSafetyAndImmutability getValue() {
            return currentValue;
        }

        public void setValue(ThreadSafetyAndImmutability newValue) {
            // Cambia la referencia haciendo el uso de la clase Calculator insegura para los subprocesos
            currentValue = newValue;
        }

        public void add(int newValue) {
            currentValue = currentValue.add(newValue);
        }

    }

    public static void main(String[] args) {
        ThreadSafetyAndImmutability immutable = new ThreadSafetyAndImmutability(4);
        // Devuelve una nueva instancia, entonces el valor de la primera instancia (4) no cambia
        // immutable.add(3);
        System.out.println(immutable.getValue());
        Calculator calculator = new Calculator();
        calculator.setValue(immutable);
        calculator.getValue().add(1);
        System.out.println(immutable.getValue());
    }

}


