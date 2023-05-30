package functional.v9_method_reference_avanzado;

import functional.Descripcion;
import functional.NumberUtils;

import java.util.Random;

public class Launcher {

    Random random = new Random();

    public Launcher() {
        Integer total = Flujo.get(10, this::randomInt)
                .filter(NumberUtils::isPrime)
                .sort(Integer::compareTo) // (value1, value2) -> value1.compareTo(value2)
                .transform(NumberUtils::squaring)
                /* Obtiene un flujo de objetos Descripcion. En otras palabras, construye un objeto Description a partir
                 * de la lista transformada. El metodo de referencia Descripcion::new representa el metodo constructor
                 * de la clase Descripcion. Esto es posible siempre y cuando la cantidad de argumentos sea la misma a la
                 * del constructor. */
                .transform(Descripcion::new) // value -> new Descripcion(value) / El (o los) argumento que se pasa a la lambda, se pasa como argumento del constructor de la clase especificada
                .actuar(System.out::println)
                .transform(Descripcion::getValue) // descripcion -> descripcion.getValue() / Convierte a Integer el flujo para poder calcular la suma
                .calculate(0, (Integer::sum));
        System.out.println("Suma: " + total);
    }

    private int randomInt() {
        return random.nextInt(10);
    }

    public static void main(String[] args) {
        new Launcher();
    }

}
