package com.github.vrbt.biclops.decoders.interfaces;

/**
 * Created by Robert on 2016-06-11.
 */
public interface DecoderFactory {
    Decoder build(final Class clazz);
}
