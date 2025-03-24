package com.punkipunk.functional.v6_lambdas.interfaces;

@FunctionalInterface
public interface Arithmetic<T, U, R> {

    R calculate(T value1, U value2);

}
