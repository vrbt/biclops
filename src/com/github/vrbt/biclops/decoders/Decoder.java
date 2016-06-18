package com.github.vrbt.biclops.decoders;

import com.github.vrbt.biclops.ordering.BitOrder;
import com.github.vrbt.biclops.ordering.Endianness;

import java.nio.ByteBuffer;

/**
 * Created by Robert on 2016-06-11.
 */
public interface Decoder<T> {
    T decode(final ByteBuffer buffer);

    T decode(final ByteBuffer buffer, final Endianness byteOrder);

    T decode(final ByteBuffer buffer, final BitOrder bitOrder);

    T decode(final ByteBuffer buffer, final int length);

    T decode(final ByteBuffer buffer, final Endianness byteOrder, final BitOrder bitOrder);

    T decode(final ByteBuffer buffer, final Endianness byteOrder, final int length);

    T decode(final ByteBuffer buffer, final BitOrder bitOrder, final int length);

    T decode(final ByteBuffer buffer, final Endianness byteOrder, final BitOrder bitOrder, final int length);
}
