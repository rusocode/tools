package functional.v5_flujo;

import functional.v5_flujo.interfaces.*;

import java.util.ArrayList;
import java.util.List;

/**
 * El flujo trabaja con una lista de datos propia, por lo tanto se omiten las listas recibidas por parametro de cada
 * metodo. A su vez los metodos dejan de ser estaticos y retornan un nuevo flujo a partir de la lista resultante. Es
 * decir que los metodos ahora se ejecutan sobre un flujo (el propio de la clase) con la posibilidad de poder
 * encadenarse entre si.
 */

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

    public Flujo<T> filter(Predicate<T> predicado) {
        List<T> list = new ArrayList<>();
        for (T value : values)
            if (predicado.test(value)) list.add(value);
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
