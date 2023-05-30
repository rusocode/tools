package functional.v11_streams_en_el_proyecto_original;

import functional.Descripcion;
import functional.NumberUtils;

import java.util.Comparator;
import java.util.Random;

public class Launcher {

    Random random = new Random();

    public Launcher() {
        // Realiza las operaciones a traves de la clase Stream de la API de Java
        // int result =
        random.ints(10, 1, 11)
                .boxed()
                .filter(valor -> valor >= 5)
                .sorted(Comparator.naturalOrder())
                .map(NumberUtils::squaring)
                .map(Descripcion::new)
                // .peek(System.out::println)
                .map(Descripcion::getValue)
                .forEach(valor -> System.out.print(valor + " "));
        // .reduce(0, Integer::sum); // .mapToInt(Integer::intValue).sum();
        // System.out.println(result);
        // .max(Comparator.naturalOrder())
        // .ifPresentOrElse(
        //        value -> System.out.println("Maximo: " + value.doubleValue()),
        //        () -> System.out.println("No hay maximo porque el flujo esta vacio!")
        //  );

        // Realiza las operaciones a traves de la clase Stream de la clase propia
        //  Flujo.get(10, this::randomInt)
        //      .filter(value -> value >= 0)
        //      .sort(Integer::compareTo)
        //      .transform(NumberUtils::squaring)
        //      .transform(Descripcion::new)
        //      .actuar(System.out::println)
        //      .transform(Descripcion::getValue)
        //      .max(Integer::compare)
        //      .ifPresentOrElse(
        //              value -> System.out.println("Maximo: " + value.doubleValue()),
        //              () -> System.out.println("No hay maximo porque el flujo esta vacio!")
        //      );
    }

    public static void main(String[] args) {
        new Launcher();
    }
}
