package com.punkipunk.generic;

/**
 * <p>
 * Como en el metodo generico se especifico dos tipos de parametros, tambien se puede hacer los mismo en una clase, ejemplo:
 * {@code public class GenericPrinter<T, V>}
 */

public class GenericPrinter<T> {

    // Atributo generico
    T thingToPrint;
    // V otherThing;

    public GenericPrinter(T thingToPrint) {
        this.thingToPrint = thingToPrint;
    }

    public void print() {
        System.out.println(thingToPrint);
    }

}
