package functional.v10_optional;

import functional.*;

import java.util.Random;

public class Launcher {

    Random random = new Random();

    public Launcher() {
        /* Al declarar la variable max de tipo Optional implica que el codigo o desarrollador que llama a esa funcion
         * que retorna un Optional este obligado a tratar la posibilidad de que la funcion retorne un Optional vacio. En
         * este caso se trata con el metodo ifPresentOrElse(). */
        // Optional<Integer> max =
        Flujo.get(10, this::randomInt)
                /* Como solo se generan numeros aleatorios entre 0 y 9 (ambos incluidos) y la condicion de la
                 * superfuncion filter es filtrar solo los valores mayores a 10, entonces retorna una lista vacia. Esto
                 * genera un NoSuchElementException al querer calcular el valor maximo desde la funcion max() de la
                 * clase Collections. Por lo tanto es necesario manejar esa posible excepcion. */
                .filter(value -> value >= 10)
                .sort(Integer::compareTo)
                .transform(NumberUtils::squaring)
                .transform(Descripcion::new)
                .actuar(System.out::println)
                .transform(Descripcion::getValue)
                /* Se puede obtener el maximo usando lambda (value1, value2) -> value2 - value1, compareTo de la clase
                 * Integer o Comparator.naturalOrder() de la interfaz funcional. */
                .max(Integer::compare)
                .ifPresentOrElse(value -> System.out.println("Maximo: " + value.doubleValue()),
                        () -> System.out.println("No hay maximo porque el flujo esta vacio!")); // Encadena la comprobacion del Optional
        // double maxDouble = max.orElse(0); // Si un valor esta presente, devuelve el valor; de lo contrario, devuelve 0
        // double maxDouble = max.orElseThrow(); // Si hay un valor presente, devuelve el valor; de lo contrario, lanza NoSuchElementException
        // double maxDouble = max.orElseGet(() -> getValorSiNoHayMaximo().doubleValue); // Si un valor esta presente, devuelve el valor; de lo contrario, devuelve el resultado producido por la funcion supplying de la interfaz Supplier
        // max.ifPresent(value -> System.out.println("Maximo: " + value.doubleValue())); // Si un valor esta presente, realiza la accion dada con el valor, de lo contrario no hace nada
    }

    private int randomInt() {
        return random.nextInt(10);
    }

    public static void main(String[] args) {
        new Launcher();
    }

}
