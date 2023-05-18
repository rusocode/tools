package functional.superfunctions;

import functional.superfunctions.classes.*;

import java.util.List;

/**
 * La programacion funcional significa que el codigo en si (cualquier instruccion) se puede tratar como cualquier otro
 * objeto. Es decir, se puede almacenar en una variable y tambien se puede pasar como argumento dinamicamente.
 * <p>
 * Las interfaces representan el codigo que se pasa dinamicamente como argumento a la funcion generica. Cada clase que
 * implemente la interfaz tendra una funcion diferente.
 */

public class Launcher {

    public Launcher() {
        List<Integer> numbers = Superfunctions.get(10, new Naturales());
        System.out.println(numbers);

        /* Utilizando la superfuncion filter() que es capas de recibir codigo como argumento desde el segundo parametro,
         * se estaria aplicando la programacion funcional. La ventaja de esto es que sin tocar la funcion, puede obtener
         * numeros pares o impares dependiendo del tipo de codigo que le pase como argumento a la funcion generica. */
        List<Integer> filtereds = Superfunctions.filter(numbers, new Even());
        System.out.println(filtereds);

        List<Integer> transformed = Superfunctions.transform(filtereds, new Squared());
        System.out.println(transformed);

        Superfunctions.consume(transformed, new Printer());

        System.out.println("Suma de cuadrados: " + addSquaring(transformed));
    }

    /**
     * Calcula la suma de la lista de numeros.
     *
     * @param numbers lista de numeros.
     * @return la suma de la lista de numeros.
     */
    private int addSquaring(List<Integer> numbers) {
        int total = 0;
        for (Integer number : numbers)
            total += number;
        return total;
    }

    public static void main(String[] args) {
        new Launcher();
    }

}
