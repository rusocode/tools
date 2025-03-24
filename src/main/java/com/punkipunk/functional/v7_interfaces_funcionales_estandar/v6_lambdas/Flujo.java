package com.punkipunk.functional.v7_interfaces_funcionales_estandar.v6_lambdas;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Ahora el flujo utiliza interfaces funcionales estandar de la API de java (predefinidas).
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
