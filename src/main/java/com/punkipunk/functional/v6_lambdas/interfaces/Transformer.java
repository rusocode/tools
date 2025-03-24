package com.punkipunk.functional.v6_lambdas.interfaces;

@FunctionalInterface
public interface Transformer<T, R> {

    R transform(T value);

}
