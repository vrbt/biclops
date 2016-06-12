package com.github.vrbt.biclops.decoders;

import com.github.vrbt.biclops.ordering.DataOrder;

import java.nio.ByteBuffer;

/**
 * Created by Robert on 2016-06-11.
 */
public interface Decoder<T, U extends DataOrder> {
    T decode(final ByteBuffer buffer);

    T decode(final ByteBuffer buffer, final U order);

    T decode(final ByteBuffer buffer, final int length);

    T decode(final ByteBuffer buffer, final U order, final int length);
}
