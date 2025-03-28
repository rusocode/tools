package com.punkipunk.functional.v6_lambdas;

import com.punkipunk.functional.v6_lambdas.interfaces.*;

import java.util.ArrayList;
import java.util.List;

public class Flujo<T> {

    private final List<T> values;

    public Flujo(List<T> values) {
        this.values = values;
    }

    public static <T> Flujo<T> get(int size, Getter<T> getter) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < size; i++)
            list.add(getter.get());
        return new Flujo<>(list);
    }

    public Flujo<T> filter(Predicate<T> predicate) {
        List<T> list = new ArrayList<>();
        for (T value : values)
            if (predicate.test(value)) list.add(value);
        return new Flujo<>(list);
    }

    public <R> Flujo<R> transform(Transformer<T, R> transformer) {
        List<R> list = new ArrayList<>();
        for (T value : values)
            list.add(transformer.transform(value));
        return new Flujo<>(list);
    }

    public Flujo<T> actuar(Consumer<T> consumer) {
        for (T value : values)
            consumer.consume(value);
        return new Flujo<>(values);
    }

    /**
     * Este metodo al no retornar nada se le conoce como una operacion terminal. Se llama asi porque despues de llamar a
     * este metodo, no se va a poder "enganchar" con ninguna otra llamada.
     */
    public void consume(Consumer<T> consumer) {
        for (T value : values)
            consumer.consume(value);
    }

    /**
     * Este metodo tampoco va a poder engarcharse con otras llamadas ya que no retorna ningun flujo. Por lo tanto se lo
     * considera una operacion terminal.
     */
    public T calculate(T id, OperadorBianario<T> arithmetic) {
        T total = id;
        for (T value : values)
            total = arithmetic.calculate(total, value);
        return total;
    }

    @Override
    public String toString() {
        return values.toString();
    }
}
