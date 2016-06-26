package com.github.vrbt.biclops.decoders;

/**
 * Created by Robert on 2016-06-26.
 */
public class ShortDecoderBuilder extends AbstractNumberDecoderBuilder<ShortDecoder> {

    private static final int DEFAULT_TYPE_LENGTH_IN_BITS = 16;

    ShortDecoderBuilder() {
        super();
        typeLengthInBits(DEFAULT_TYPE_LENGTH_IN_BITS);
        valueLengthInBits(DEFAULT_TYPE_LENGTH_IN_BITS);
    }

    @Override
    public ShortDecoder build() {
        return new ShortDecoder(byteOrder, bitOrder, valueLengthInBits, signed, typeLengthInBits);
    }
}
