package com.punkipunk.functional.v11_streams_operaciones_terminales_de_consulta;

/**
 * La clase Stream tambien proporciona una serie de metodos de consulta sobre los elementos de un stream,
 * denominadas operaciones de cortocircuito (short-circuit terminal operations). Se llaman asi porque se deja
 * de procesar el resto de elementos si con los elementos que ya han sido procesados se es capaz de determinar
 * el resultado.
 * <p>
 * Tenemos un conjunto de metodos que permiten consultar, respectivamente, si todos, ninguno o algunos de los
 * elementos del stream cumplen con un determinado predicado, retornando un valor booleano. Son los metodos
 * allMatch(predicate), noneMatch(predicate) y anyMatch(predicate).
 * <pre>{@code
 * boolean todosImpares = Stream.of(1, 2, 3)
 *      .allMatch(n -> n % 2 != 0);
 * }</pre>
 * Por otro lado tenemos los metodos findFirst() y findAny(), que retornan un Optional<T> con, respectivamente,
 * el primer elemento del stream, o algun elemento del stream (no esta indicado cual), si es que existe. Un
 * aspecto curioso es que estos metodos no reciben ningun predicado con el que indicar la "condicion de busqueda",
 * por lo que normalmente se usan despues de haber ejecutado el metodo filter(predicado) sobre el stream.
 * <pre>{@code
 * Optional<Integer> algunImpares = Stream.of(1, 2, 3)
 *      .filter(n -> n % 2 != 0)
 *      .findAny();
 * }</pre>
 * Un aspecto curioso es que no se proporciona ningun metodo para obtener el ultimo elemento de un stream. Sin embargo,
 * podemos obtenerlo usando el metodo skip() de la siguiente manera (siempre y cuando se trate un stream finito):
 * <pre>{@code
 * long count = listOfIntegers.stream().count()
 * Optional<Integer> lastElemet = listOfIntegers.stream()
 *      .skip(count - 1)
 *      .findFirst();
 * }</pre>
 */

public class Launcher {
}
