package com.punkipunk.functional.v11_streams_reduccion;

import java.util.stream.Stream;

/**
 * <h2>Metodos estandar de reduccion</h2>
 * La clase Stream y las clases especificas DoubleStream, IntStream y LongStream implementan algunos metodos que son
 * operaciones terminales de reduccion especializadas.
 * <p>
 * La primera de ellas es count(), que retorna un long con el numero de elementos de un stream. Por ejemplo:
 * <pre>{@code
 * long cuantosSonPares = Stream.of(1, 2, 3)
 *      .filter(n -> n % 2 == 0)
 *      .count();
 * }</pre>
 * retorna 1, ya que despues de filtrar el stream para quedarnos solo con los numeros pares tan solo queda uno.
 * <p>
 * Las clases IntStream, LongStream y DoubleStream, correspondientes a streams de elementos de tipos primitivos
 * numericos, disponen del metodo sum() para calcular la suma de los elementos del stream.
 * <p>
 * Estas clases de streams de elementos de tipo primitivo numerico tambien disponen de metodos para obtener el valor
 * maximo, max(), el valor minimo, min() y la medida aritmetica, average(), de los elementos numericos del stream. Dado
 * que el stream sobre el que se apliquen puede estar vacio, estos metodos retornan un Optional, es decir una valor
 * opcional de tipo correspondiente.
 * <pre>{@code
 * OptionalDouble maxImpares = IntStream.of(1, 2, 3)
 *      .filter(n -> n % 2 != 0)
 *      .max();
 * OptionalDouble minImpares = IntStream.of(1, 2, 3)
 *      .filter(n -> n % 2 != 0)
 *      .min();
 * OptionalDouble mediaImpares = IntStream.of(1, 2, 3)
 *      .filter(n -> n % 2 != 0)
 *      .average();
 * }</pre>
 * Como veremos mas adelante, el problema de estas operaciones, tanto la de obtener el valor maximo o el minimo como la
 * de obtener la media aritmetica es que el resultado no parte de un valor inicial fijo, al que se conoce como identidad,
 * al contrario de como sucede con otros tipos de operaciones, como la de contar o la de sumar donde la entidad de
 * dichas operaciones es 0. Por este motivo estos metodos retornan un Optional, dado que es posible que el valor
 * resultado no este presente.
 */

public class Launcher {

    public Launcher() {
        // IntStream.range(1, 10).sum();

        /* En el caso de sumar una lista de enteros obtenida a partir del metodo of, entonces se debe transformar esa
         * lista con un metodo de transformacion para convertir el strem en enteros. */
        // int result = Stream.of(1, 2, 3).mapToInt(Integer::intValue).sum();
        // System.out.println(result);

        // long result = Stream.of(1, 2, 3).mapToInt(Integer::intValue).count();
        // System.out.println(result);

        // Metodo reduce
        // El tipo de retorno es Optional ya que el metodo reduce devuelve eso
        // Stream.of(1, 2, 3)
        // .filter(valor -> valor > 5)
        // .reduce((acumulador, valor) -> acumulador * valor)
        // .ifPresentOrElse(System.out::println, () -> System.out.println("El stream se ha quedado vacio y no hay reduccion!"));

        // En este caso no hace falta utilizar un Optional ya que el metodo reduce recibe un valor inicial que sirve en caso de que el strem se quede vacio
        // Integer result = Stream.of(2, 3, 4)
        // .filter(valor -> valor > 5)
        // .reduce(1, (acumulador, valor) -> acumulador * valor);
        // System.out.println(result);

        // Utiliza el metodo reduce con el tercer argumento como combinador de distintas combinaciones parciales
        Integer result = Stream.of(
                new Student("Rulo", 22),
                new Student("Ruso", 23)
        ).reduce(0, (acumulador, student) -> acumulador + student.getAge(), Integer::sum);
        System.out.println(result);

    }

    public static void main(String[] args) {
        new Launcher();
    }

}