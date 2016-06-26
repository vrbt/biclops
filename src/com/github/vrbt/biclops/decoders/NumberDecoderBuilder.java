package com.github.vrbt.biclops.decoders;

import com.github.vrbt.biclops.ordering.BitOrder;
import com.github.vrbt.biclops.ordering.Endianness;

/**
 * Created by Robert on 2016-06-26.
 */
public interface NumberDecoderBuilder<T extends NumberDecoder> extends DecoderBuilder<T> {
    @Override
    NumberDecoderBuilder<T> byteOrder(final Endianness byteOrder);

    @Override
    NumberDecoderBuilder<T> bitOrder(final BitOrder bitOrder);

    @Override
    NumberDecoderBuilder<T> valueLengthInBits(final int length);

    NumberDecoderBuilder<T> signed(final boolean signed);

    NumberDecoderBuilder<T> typeLengthInBits(final int typeLengthInBits);
}
