package com.punkipunk.functional.v6_lambdas.interfaces;

/**
 * Una interfaz funcional es una interfaz que contiene un unico metodo abstracto.
 * <p>
 * La anotacion {@link FunctionalInterface} informa al compilador de que dicha interfaz es funcional, y por lo tanto
 * tiene un unico metodo abstracto. El objetivo de esta anotacion es que se produzca un error de compilacion si le
 * añadimos un segundo metodo abstracto a la interfaz.
 * <p>
 * La forma de tratar una funcion como un codigo literal para almacenar o pasarlo como argumento es declarando una
 * interfaz funcional.
 */

@FunctionalInterface
public interface Consumer<T> {

    void consume(T value);

}
