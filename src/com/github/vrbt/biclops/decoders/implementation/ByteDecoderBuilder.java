package com.github.vrbt.biclops.decoders.implementation;

/**
 * Created by Robert on 2016-06-26.
 */
class ByteDecoderBuilder extends AbstractNumberDecoderBuilder<ByteDecoder, ByteDecoderBuilder> {

    private static final int DEFAULT_TYPE_LENGTH_IN_BITS = 8;

    ByteDecoderBuilder() {
        super();
        typeLengthInBits(DEFAULT_TYPE_LENGTH_IN_BITS);
        valueLengthInBits(DEFAULT_TYPE_LENGTH_IN_BITS);
    }

    @Override
    public ByteDecoder build() {
        return new ByteDecoder(byteOrder, bitOrder, valueLengthInBits, signed, typeLengthInBits);
    }
}
