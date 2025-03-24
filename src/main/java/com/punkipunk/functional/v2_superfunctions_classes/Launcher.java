package com.punkipunk.functional.v2_superfunctions_classes;

import com.punkipunk.functional.v2_superfunctions_classes.classes.*;

import java.util.List;

/**
 * La programacion funcional significa que el codigo en si (cualquier instruccion) se puede tratar como cualquier otro
 * objeto. Es decir, se puede almacenar en una variable y tambien se puede pasar como argumento dinamicamente.
 * <p>
 * Las interfaces representan el codigo que se pasa dinamicamente como argumento a la superfuncion. Cada clase que
 * implemente la interfaz tendra una funcion diferente.
 */

public class Launcher {

    public Launcher() {
        List<Integer> numbers = Superfunctions.get(10, new Aleatorio());
        System.out.println(numbers);

        /* Utilizando la superfuncion filter() que es capas de recibir codigo como argumento desde el segundo parametro,
         * se estaria aplicando la programacion funcional. La ventaja de esto es que sin tocar la funcion, puede obtener
         * numeros pares o impares dependiendo del tipo de codigo que le pase como argumento a la superfuncion. Es decir
         * que la funcion en este caso, acepta solo las clases que implementan la interfaz Predicate. */
        List<Integer> filtereds = Superfunctions.filter(numbers, new Odd());
        System.out.println(filtereds);

        List<Integer> transformed = Superfunctions.transform(filtereds, new Squared());
        System.out.println(transformed);

        Superfunctions.consume(transformed, new Printer());

        System.out.println("Suma: " + Superfunctions.calculate(transformed, 0, new Adder()));
    }

    public static void main(String[] args) {
        new Launcher();
    }

}
