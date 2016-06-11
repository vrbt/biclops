package com.github.vrbt.biclops.decoders;

import java.nio.ByteBuffer;

/**
 * Created by Robert on 2016-06-11.
 */
public interface Decoder<T> {
    T decode(final ByteBuffer buffer);
}
