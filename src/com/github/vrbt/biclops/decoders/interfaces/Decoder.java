package com.github.vrbt.biclops.decoders.interfaces;

import java.nio.ByteBuffer;

/**
 * Created by Robert on 2016-06-11.
 */
public interface Decoder<T> {
    int BYTE_LENGTH = 8;
    T decode(final ByteBuffer buffer);
}
