package com.github.vrbt.biclops.decoders.interfaces;

/**
 * Created by Robert on 2016-06-26.
 */
public interface NumberDecoderBuilder<T extends NumberDecoder, U extends NumberDecoderBuilder> extends SingleDecoderBuilder<T, U> {
    U signed(final boolean signed);

    U typeLengthInBits(final int typeLengthInBits);
}
