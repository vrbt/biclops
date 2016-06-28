package com.github.vrbt.biclops.utils;

/**
 * Created by Robert on 2016-06-28.
 */
@FunctionalInterface
public interface TriConsumer<T, U, S> {
    void apply(T t, U u, S s) throws IllegalAccessException;
}
