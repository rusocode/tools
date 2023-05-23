package functional.v4_superfunctions_generic;

import functional.v4_superfunctions_generic.interfaces.*;

import java.util.ArrayList;
import java.util.List;

public class Superfunctions {

    public static <T> List<T> get(int size, Getter<T> getter) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < size; i++)
            list.add(getter.get());
        return list;
    }

    public static <T> List<T> filter(List<T> values, Predicate<T> predicado) {
        List<T> list = new ArrayList<>();
        for (T value : values)
            if (predicado.test(value)) list.add(value);
        return list;
    }

    public static <T, R> List<R> transform(List<T> values, Transformer<T, R> transformer) {
        List<R> list = new ArrayList<>();
        for (T value : values)
            list.add(transformer.transform(value));
        return list;
    }

    public static <T> void consume(List<T> values, Consumer<T> consumer) {
        for (T value : values)
            consumer.consume(value);
    }

    public static <T> T calculate(List<T> values, T id, OperadorBianario<T> arithmetic) {
        T total = id;
        for (T value : values)
            total = arithmetic.calculate(total, value);
        return total;
    }

}
