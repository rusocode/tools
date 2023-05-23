package functional.v6_lambdas;

import java.util.Random;

/**
 * Las expresiones lambdas se caracterizan por sus dos partes: la declaracion y el cuerpo separados por una flecha "->".
 * <p>
 * Estas expresiones son capaces de deducir el tipo de dato que se esta utilizando con la finalidad de ahorrar codigo.
 * Cuando la lambda recibe un unico argumento, el tipo se infiere, entonces se pueden eliminar los parentesis. Lo mismo
 * sucede con las llaves en el caso de que sea una unica expresion. Ahora al utilizar la sintaxis reducida {@code return
 * value -> value % 2 == 0;} el return y el ; no tienen sentido ya que la lambda va a retornar lo que devuelva la
 * expresion, por lo tanto tambien se eliminan. Si la lambda no recibe ningun parametro entonces solo se especifican los
 * dos parentesis ().
 * <p>
 * Las lambdas pueden capturar el entorno en el que se ejecutan, es decir que pueden utilizar variables en el entorno
 * en el que estan siendo ejecutadas.
 */

public class Launcher {

    public Launcher() {
        Random random = new Random();
        Integer total = Flujo.get(10, () -> random.nextInt(10))
                .filter(value -> value % 2 == 0)
                .transform(value -> value * value)
                .actuar(value -> System.out.println(value))
                .calculate(0, ((value1, value2) -> value1 + value2));
        System.out.println("Suma: " + total);
    }

    public static void main(String[] args) {
        new Launcher();
    }

}
