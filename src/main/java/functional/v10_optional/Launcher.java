package functional.v10_optional;

import java.util.Random;

public class Launcher {

    Random random = new Random();

    public Launcher() {
        Integer max = Flujo.get(10, this::randomInt)
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
                .max(Integer::compare);
        // Para evitar que al desarrollador se le olvide controlar si el maximo es null

        System.out.println("Maximo: " + max);
    }

    private int randomInt() {
        return random.nextInt(10);
    }

    public static void main(String[] args) {
        new Launcher();
    }

}
