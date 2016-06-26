package com.github.vrbt.biclops.decoders.implementation;

/**
 * Created by Robert on 2016-06-26.
 */
class IntegerDecoderBuilder extends AbstractNumberDecoderBuilder<IntegerDecoder, IntegerDecoderBuilder> {

    private static final int DEFAULT_TYPE_LENGTH_IN_BITS = 32;

    IntegerDecoderBuilder() {
        super();
        typeLengthInBits(DEFAULT_TYPE_LENGTH_IN_BITS);
        valueLengthInBits(DEFAULT_TYPE_LENGTH_IN_BITS);
    }

    @Override
    public IntegerDecoder build() {
        return new IntegerDecoder(byteOrder, bitOrder, valueLengthInBits, signed, typeLengthInBits);
    }
}
