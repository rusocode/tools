package rxjava.operators;

import io.reactivex.rxjava3.core.Observable;

/**
 * ¿Recuerdas que hablamos de nuestros dos heroes? Ya presentamos Observer y Observable, que son la base de cualquier
 * marco reactivo. Pero en esta oportunidad vamos a presentar a Operator, nuestro tercer heroe que tiene un papel muy
 * importante.
 * <p>
 * Hemos comenzado nuestra serie de Operadores RxJava con map() y filter(). Aqui puede comprobar como map() y filter()
 * funcionan individualmente y tambien entre si encadenando multiples operadores.
 */

public class MapFilter {

    public static void main(String[] args) {
        mapOperator();
        mapOperatorReturnsDifferentData();
        filterOperator();
        combineMapAndFilter();
    }

    /**
     * Utiliza el operador map() para transformar el valor intermedio antes de que llegue al observador.
     */
    private static void mapOperator() {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5);
        observable
                .map(item -> item * 2)
                .subscribe(System.out::println);
    }

    /**
     * Utiliza el operador map() para transformar el valor intermedio antes de que llegue al observador y aqui map()
     * emite un tipo de datos diferente y el observador solo necesita ajustarse o aceptar el mismo tipo de emision.
     */
    private static void mapOperatorReturnsDifferentData() {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5);
        observable
                .map(item -> "Hello World!")
                .subscribe(System.out::println);
    }

    /**
     * Utiliza el operador filter() para filtrar el valor intermedio, que no cumple con la logica especificada en filter,
     * y filter() puede no emitir ningun elemento si ningun elemento coincide con ese criterio.
     */
    private static void filterOperator() {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5);
        observable
                .filter(item -> item % 2 == 0)
                .subscribe(System.out::println);
    }

    /**
     * Combina el operador map() y filter() juntos y, como map() y filter(), ambos no son mas que un Observable y
     * tambien funcionan como un Observer, por lo que podemos encadenarlos, pero el orden de operacion si importa aqui.
     * Aqui filter() se activara primero y map() funcionara en la emision filtrada, y no en toda la emision en general.
     */
    private static void combineMapAndFilter() {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5);
        observable
                .filter(item -> item % 2 == 0)
                .map(item -> item * 2)
                .subscribe(System.out::println);
    }


}
