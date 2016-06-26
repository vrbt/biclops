package com.github.vrbt.biclops.decoders;

import com.github.vrbt.biclops.ordering.BitOrder;
import com.github.vrbt.biclops.ordering.Endianness;

/**
 * Created by Robert on 2016-06-26.
 */
public interface DecoderBuilder<T extends Decoder> {
    DecoderBuilder<T> byteOrder(final Endianness byteOrder);

    DecoderBuilder<T> bitOrder(final BitOrder bitOrder);

    DecoderBuilder<T> valueLengthInBits(final int length);

    T build();
}
