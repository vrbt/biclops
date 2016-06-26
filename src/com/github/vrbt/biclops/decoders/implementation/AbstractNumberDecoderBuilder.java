package com.github.vrbt.biclops.decoders.implementation;

import com.github.vrbt.biclops.decoders.interfaces.NumberDecoderBuilder;
import com.github.vrbt.biclops.ordering.BitOrder;
import com.github.vrbt.biclops.ordering.Endianness;

import static com.github.vrbt.biclops.ordering.BitOrder.MOST_SIGNIFICANT_BIT_FIRST;
import static com.github.vrbt.biclops.ordering.Endianness.BIG_ENDIAN;

/**
 * Created by Robert on 2016-06-26.
 */
abstract class AbstractNumberDecoderBuilder<T extends AbstractNumberDecoder, U extends AbstractNumberDecoderBuilder> implements NumberDecoderBuilder<T, U> {

    protected int typeLengthInBits = 32;
    protected boolean signed = true;
    protected Endianness byteOrder = BIG_ENDIAN;
    protected BitOrder bitOrder = MOST_SIGNIFICANT_BIT_FIRST;
    protected int valueLengthInBits = 32;

    protected AbstractNumberDecoderBuilder() {

    }

    @Override
    public U signed(final boolean signed) {
        this.signed = signed;
        return (U) this;
    }

    @Override
    public U typeLengthInBits(final int typeLengthInBits) {
        if (typeLengthInBits < 0) {
            throw new IllegalArgumentException("Type length in bits cannot be negative");
        }
        this.typeLengthInBits = typeLengthInBits;
        return (U) this;
    }

    @Override
    public U byteOrder(final Endianness byteOrder) {
        this.byteOrder = byteOrder;
        return (U) this;
    }

    @Override
    public U bitOrder(final BitOrder bitOrder) {
        this.bitOrder = bitOrder;
        return (U) this;
    }

    @Override
    public U valueLengthInBits(final int length) {
        if (typeLengthInBits < 0) {
            throw new IllegalArgumentException("Value length in bits cannot be negative");
        }
        this.valueLengthInBits = length;
        return (U) this;
    }
}
