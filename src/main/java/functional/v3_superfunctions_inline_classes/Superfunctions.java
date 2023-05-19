package functional.v3_superfunctions_inline_classes;

import functional.v3_superfunctions_inline_classes.interfaces.*;

import java.util.ArrayList;
import java.util.List;

public class Superfunctions {

    public static List<Integer> filter(List<Integer> values, Predicate predicado) {
        List<Integer> list = new ArrayList<>();
        for (Integer value : values)
            if (predicado.test(value)) list.add(value);
        return list;
    }

    public static List<Integer> get(int size, Getter getter) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++)
            list.add(getter.get());
        return list;
    }

    public static List<Integer> transform(List<Integer> values, Transformer transformer) {
        List<Integer> list = new ArrayList<>();
        for (Integer value : values)
            list.add(transformer.transform(value));
        return list;
    }

    public static void consume(List<Integer> values, Consumer consumer) {
        for (Integer value : values)
            consumer.consume(value);
    }

    public static Integer calculate(List<Integer> values, Integer id, Arithmetic arithmetic) {
        int total = id;
        for (Integer value : values)
            total = arithmetic.calculate(total, value);
        return total;
    }

}
