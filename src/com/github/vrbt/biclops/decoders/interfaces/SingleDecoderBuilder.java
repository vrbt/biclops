package com.github.vrbt.biclops.decoders.interfaces;

import com.github.vrbt.biclops.ordering.BitOrder;
import com.github.vrbt.biclops.ordering.Endianness;

/**
 * Created by Robert on 2016-06-26.
 */
public interface SingleDecoderBuilder<T extends SingleDecoder, U extends SingleDecoderBuilder> extends DecoderBuilder<T, U> {
    U byteOrder(final Endianness byteOrder);

    U bitOrder(final BitOrder bitOrder);

    U valueLengthInBits(final int length);

}
