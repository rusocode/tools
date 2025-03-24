package com.punkipunk.functional.v2_superfunctions_classes;

import com.punkipunk.functional.v2_superfunctions_classes.interfaces.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Superfunciones utilizadas para aplicar el concepto de programacion funcional.
 */

public class Superfunctions {

    /**
     * Obtiene la lista de valores.
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
     * Filtra la lista de valores.
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
     * Transforma la lista de valores.
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
     * Consume la lista de valores.
     *
     * @param values   lista de valores.
     * @param consumer codigo a ejecutar.
     */
    public static void consume(List<Integer> values, Consumer consumer) {
        for (Integer value : values)
            consumer.consume(value);
    }

    /**
     * Calcula la lista de valores.
     * <p>
     * El id del Multiplier debe que ser 1 para evitar la primera multiplicacion por 0. Y para el Adder debe ser 0.
     *
     * @param values     lista de valores.
     * @param id         identificador de la operacion aritmetica.
     * @param arithmetic codigo a ejecutar.
     * @return el total calculado de la lista de valores.
     */
    public static Integer calculate(List<Integer> values, Integer id, Arithmetic arithmetic) {
        int total = id;
        for (Integer value : values)
            total = arithmetic.calculate(total, value);
        return total;
    }

}
