package com.github.vrbt.biclops.decoders.implementation;

/**
 * Created by Robert on 2016-06-26.
 */
class LongDecoderBuilder extends AbstractNumberDecoderBuilder<LongDecoder, LongDecoderBuilder> {

    private static final int DEFAULT_TYPE_LENGTH_IN_BITS = 64;

    LongDecoderBuilder() {
        super();
        typeLengthInBits(DEFAULT_TYPE_LENGTH_IN_BITS);
        valueLengthInBits(DEFAULT_TYPE_LENGTH_IN_BITS);
    }

    @Override
    public LongDecoder build() {
        return new LongDecoder(byteOrder, bitOrder, valueLengthInBits, signed, typeLengthInBits);
    }
}
