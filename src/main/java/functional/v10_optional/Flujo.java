package functional.v10_optional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.*;

/**
 *
 */

public class Flujo<T> {

    private final List<T> values;

    public Flujo(List<T> values) {
        this.values = values;
    }

    public static <T> Flujo<T> get(int size, Supplier<T> supplier) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < size; i++)
            list.add(supplier.get());
        return new Flujo<>(list);
    }

    public Flujo<T> filter(Predicate<T> predicate) {
        List<T> list = new ArrayList<>();
        for (T value : values)
            if (predicate.test(value)) list.add(value);
        return new Flujo<>(list);
    }

    public <R> Flujo<R> transform(Function<T, R> function) {
        List<R> list = new ArrayList<>();
        for (T value : values)
            list.add(function.apply(value));
        return new Flujo<>(list);
    }

    public Flujo<T> actuar(Consumer<T> consumer) {
        for (T value : values)
            consumer.accept(value);
        return new Flujo<>(values);
    }

    public void consume(Consumer<T> consumer) {
        for (T value : values)
            consumer.accept(value);
    }

    public Flujo<T> sort(Comparator<T> comparator) {
        List<T> list = new ArrayList<>(values);
        list.sort(comparator);
        return new Flujo<>(list);
    }

    /**
     * Obtiene el maximo de la lista de valores en base al comparador.
     * <p>
     * Como la lista es generica, entonces debe pasar la interfaz funcional Comparator.
     *
     * @param comparator comparador.
     * @return el maximo de tipo T.
     */
    public T max(Comparator<T> comparator) {
        try {
            return Collections.max(values, comparator);
        } catch (Exception e) {
            // Retorna null en caso de que la lista este vacia
            return null;
        }
    }

    public T calculate(T id, BinaryOperator<T> binaryOperator) {
        T total = id;
        for (T value : values)
            total = binaryOperator.apply(total, value);
        return total;
    }

    @Override
    public String toString() {
        return values.toString();
    }
}
