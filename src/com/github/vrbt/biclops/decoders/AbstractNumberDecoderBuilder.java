package com.github.vrbt.biclops.decoders;

import com.github.vrbt.biclops.ordering.BitOrder;
import com.github.vrbt.biclops.ordering.Endianness;

import static com.github.vrbt.biclops.ordering.BitOrder.MOST_SIGNIFICANT_BIT_FIRST;
import static com.github.vrbt.biclops.ordering.Endianness.BIG_ENDIAN;

/**
 * Created by Robert on 2016-06-26.
 */
abstract class AbstractNumberDecoderBuilder<T extends AbstractNumberDecoder> implements NumberDecoderBuilder<T> {

    protected int typeLengthInBits = 32;
    protected boolean signed = true;
    protected Endianness byteOrder = BIG_ENDIAN;
    protected BitOrder bitOrder = MOST_SIGNIFICANT_BIT_FIRST;
    protected int valueLengthInBits = 32;

    protected AbstractNumberDecoderBuilder() {

    }

    @Override
    public NumberDecoderBuilder<T> signed(final boolean signed) {
        this.signed = signed;
        return this;
    }

    @Override
    public NumberDecoderBuilder<T> typeLengthInBits(final int typeLengthInBits) {
        this.typeLengthInBits = typeLengthInBits;
        return this;
    }

    @Override
    public NumberDecoderBuilder<T> byteOrder(final Endianness byteOrder) {
        this.byteOrder = byteOrder;
        return this;
    }

    @Override
    public NumberDecoderBuilder<T> bitOrder(final BitOrder bitOrder) {
        this.bitOrder = bitOrder;
        return this;
    }

    @Override
    public NumberDecoderBuilder<T> valueLengthInBits(final int length) {
        this.valueLengthInBits = length;
        return this;
    }
}
