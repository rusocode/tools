package generic;

import java.util.*;

/**
 * La ventaja de los genericos, es que se pueden usar para crear objetos de diferentes tipos usando la misma clase.
 * <p>
 * Se especifican con operadores diamantes <> y un nombre (convecional T, U, o K) dentro.
 * <p>
 * Este ejemplo crea dos objetos de diferentes tipos desde {@code GenericPrinter}. Tambien se utiliza {@code GenericAnimal} para
 * poder crear objetos que solo extiendan de la clase {@code Animal}.
 * <p>
 * Es conveniente evitar usar Object como tipo, ya que causa problemas de seguridad y ejecucion, ademas de tener que castear cada
 * uno.
 * <pre>{@code
 * ArrayList<Object> cats = new ArrayList<>();
 * cats.add(new Cat());
 * Cat michi = cats.get(0); // Error de tipo ya que es una lista de Object
 * Cat michi = (Cat) cats.get(0); // Castea al objeto Cat
 * }</pre>
 * <p>
 * Links: <a href="https://www.youtube.com/watch?v=K1iu1kXkVoA">Generics In Java</a>
 */

public class Launcher {

    public static void main(String[] args) {

        GenericPrinter<Integer> integerPrinter = new GenericPrinter<>(23);
        integerPrinter.print();

        GenericPrinter<Double> doublePrinter = new GenericPrinter<>(33.5);
        doublePrinter.print();

        GenericAnimal<Cat> michi = new GenericAnimal<>(new Cat());
        GenericAnimal<Dog> pipi = new GenericAnimal<>(new Dog());
        // El tipo Integer no esta dentro de los limites de GenericAnimal
        // GenericAnimal<Integer> numero = new GenericAnimal<>(new Integer());

        // Almacena el tipo de retorno generico
        String result = genericMethod("Juan", 28);
        // Integer result2 = genericMethod(28, "Juan");
        System.out.println(result);

        List<Double> intList = new ArrayList<>();
        intList.add(4.43);
        intList.add(3.4);
        // printList(intList);

    }

    /**
     * <p>
     * Al igual que las clases, los metodos que retornan un generico se especifican con la letra de conveniencia T sin los <>.
     * Para indicar mas de un parametro, se utilizan las letras convecionales separados por comas dentro los <> antes del tipo de
     * retorno siempre que la clase no se declare como generica, ademas de especificar con la letra de conveniencia T, V o K la
     * lista de parametros.
     * <p>
     * Este metodo se adapta a cualquier tipo de dato, cumpliendo con las reglas anteriores.
     *
     * @param thing      parametro generico.
     * @param otherThing otro parametro generico.
     * @return un generico.
     */
    private static <T, V> T genericMethod(T thing, V otherThing) {
        System.out.println("Este es un metodo generico que recibe dos parametros genericos y devuelve un generico.");
        System.out.println(thing + "!");
        System.out.println(otherThing + "!");
        return thing;
    }

    private static <T, V> void otherGenericMethod(T thing, V otherThing) {
        System.out.println("Este es un metodo generico que recibe dos parametros genericos y no devuelve nada.");
    }

    private static <T> void otherGenericMethod(T thing) {
        System.out.println("Este es un metodo generico que recibe un parametro generico y no devuelve nada.");
    }

    private static <T> int otherGenericMethod2(T thing) {
        System.out.println("Este es un metodo generico que recibe un parametro generico y devuelve un int.");
        return 0;
    }

    /**
     * En caso de querer obviar la especificacion del metodo generico tanto para retorno como para parametro, es
     * necesario declarar la clase como generica y que el metodo no sea estatico.
     */
    // private void otherGenericMethod3(T thing) {}

    /**
     * Metodo comodin que obtiene una lista de cualquier tipo de objeto y la imprime.
     * <p>
     * El comodin en los genericos se utiliza para poder aceptar cualquier tipo de objeto, ya que es algo desconocido. Tambien se
     * puede utilizar ese "limite" ({@code List<? extends Animal>}) en los comodines para aceptar clases especificas.
     */
    private static void printList(List<?> myList) {
        myList.forEach(System.out::println);
    }

}
