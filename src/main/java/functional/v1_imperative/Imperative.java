package functional.v1_imperative;

import java.util.*;

/**
 * Ejemplo de programacion imperativa en donde se especifican las instrucciones de un programa simple paso por paso.
 */

public class Imperative {

    public Imperative() {
        // 1. Crea una lista de numeros inmutables
        List<Integer> numbers = createList();
        System.out.println(numbers);
        // 2. Filtra los numeros pares
        List<Integer> evens = filterEvens(numbers);
        System.out.println(evens);
        // 3. Calcula el cuadrado de cada numero par
        List<Integer> squares = squaring(evens);
        System.out.println(squares);
        // 4. Muestra cada cuadrado por pantalla
        showList(squares);
        // 5. Calcula la suma de los cuadrados
        int total = addSquaring(squares);
        System.out.println("Suma de cuadrados: " + total);
    }

    /**
     * Crea una lista inmutable de numeros.
     *
     * @return una lista inmutable de numeros.
     */
    private List<Integer> createList() {
        return List.of(0, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144);
    }

    /**
     * Filtra los numeros pares de la lista de numeros.
     *
     * @param numbers lista de numeros.
     * @return una lista de numeros pares.
     */
    private List<Integer> filterEvens(List<Integer> numbers) {
        List<Integer> result = new ArrayList<>();
        for (Integer number : numbers)
            if (number % 2 == 0) result.add(number);
        return result;
    }

    /**
     * Calcula el cuadrado de la lista de numeros.
     *
     * @param numbers lista de numeros.
     * @return una lista con el cuadrado de cada numero.
     */
    private List<Integer> squaring(List<Integer> numbers) {
        List<Integer> result = new ArrayList<>();
        for (Integer number : numbers)
            result.add(number * number);
        return result;
    }

    /**
     * Muestra los numeros por cuadro.
     *
     * @param numbers lista de numeros.
     */
    private void showList(List<Integer> numbers) {
        for (Integer number : numbers)
            System.out.println(number);
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
        new Imperative();
    }

}
