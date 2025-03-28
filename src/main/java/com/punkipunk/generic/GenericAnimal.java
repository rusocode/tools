package com.punkipunk.generic;

/**
 * Esta clase generica solo acepta clases que extiendan de {@code Animal}, actuando como una especie de limite.
 * <p>
 * Ademas de extender clases, tambien puede extender interfaces usando {@code extends} y no {@code implements}.
 * <p>
 * Para agregar una o varias interfaces despues de especificar la clase, se utiliza &.
 * <pre>{@code
 * <T extends Serializable> // No error
 * <T extends Animal & Serializable> // No error
 * <T extends Animal & Serializable & Set> // No error
 * <T extends Serializable & Animal> // Error
 * }</pre>
 * <p>
 * No se puede extender mas de una clase, ya que java no admite la herencia multiple.
 * <p>
 * {@code <T extends Animal, Human> // No se acepta}
 */

public class GenericAnimal<T extends Animal> {

    T thingToPrint;

    public GenericAnimal(T thingToPrint) {
        this.thingToPrint = thingToPrint;
    }

    public void print() {
        thingToPrint.eat();
    }

}
