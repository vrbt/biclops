package com.github.vrbt.biclops.decoders;

/**
 * Created by Robert on 2016-06-26.
 */
class BigIntegerDecoderBuilder extends AbstractNumberDecoderBuilder<BigIntegerDecoder> {

    private static final int DEFAULT_TYPE_LENGTH_IN_BITS = 128;

    BigIntegerDecoderBuilder() {
        super();
        typeLengthInBits(DEFAULT_TYPE_LENGTH_IN_BITS);
        valueLengthInBits(DEFAULT_TYPE_LENGTH_IN_BITS);
    }

    @Override
    public BigIntegerDecoder build() {
        return new BigIntegerDecoder(byteOrder, bitOrder, valueLengthInBits, signed, typeLengthInBits);
    }
}
