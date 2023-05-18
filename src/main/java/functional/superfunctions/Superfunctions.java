package functional.superfunctions;

import functional.superfunctions.interfaces.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Funciones genericas utilizadas para aplicar el concepto de programacion funcional.
 */

public class Superfunctions {

    /**
     * Filtra una lista de valores.
     *
     * @param values    lista de valores.
     * @param predicado codigo a ejecutar.
     * @return la lista de valores filtrados.
     */
    public static List<Integer> filter(List<Integer> values, Predicate predicado) {
        List<Integer> list = new ArrayList<>();
        for (Integer value : values)
            if (predicado.test(value)) list.add(value);
        return list;
    }

    /**
     * Obtiene una lista de valores.
     *
     * @param size   cantidad de valores.
     * @param getter codigo a ejecutar.
     * @return la lista de valores.
     */
    public static List<Integer> get(int size, Getter getter) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++)
            list.add(getter.get());
        return list;
    }

    /**
     * Transforma una lista de valores.
     *
     * @param values      lista de valores.
     * @param transformer codigo a ejecutar.
     * @return la lista de valores transformados.
     */
    public static List<Integer> transform(List<Integer> values, Transformer transformer) {
        List<Integer> list = new ArrayList<>();
        for (Integer value : values)
            list.add(transformer.transform(value));
        return list;
    }

    /**
     * Consume una lista de valores.
     *
     * @param values   lista de valores.
     * @param consumer codigo a ejecutar.
     */
    public static void consume(List<Integer> values, Consumer consumer) {
        for (Integer value : values)
            consumer.consume(value);
    }

}
