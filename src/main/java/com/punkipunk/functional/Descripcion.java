package com.punkipunk.functional;

/**
 * Clase de prueba para crear un objeto utilizando el method reference.
 */

public class Descripcion {

    private final Integer value;

    public Descripcion(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "V(" + value + ")";
    }
}
