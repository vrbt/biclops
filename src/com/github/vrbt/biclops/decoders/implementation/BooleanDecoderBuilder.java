package com.github.vrbt.biclops.decoders.implementation;

import com.github.vrbt.biclops.decoders.interfaces.SingleDecoderBuilder;
import com.github.vrbt.biclops.ordering.BitOrder;
import com.github.vrbt.biclops.ordering.Endianness;

import static com.github.vrbt.biclops.ordering.BitOrder.MOST_SIGNIFICANT_BIT_FIRST;

/**
 * Created by Robert on 2016-06-26.
 */
class BooleanDecoderBuilder implements SingleDecoderBuilder<BooleanDecoder, BooleanDecoderBuilder> {

    private BitOrder bitOrder = MOST_SIGNIFICANT_BIT_FIRST;
    private int valueLengthInBits = 1;
    private Endianness byteOrder;

    @Override
    public BooleanDecoderBuilder byteOrder(Endianness byteOrder) {
        this.byteOrder = byteOrder;
        return this;
    }

    @Override
    public BooleanDecoderBuilder bitOrder(BitOrder bitOrder) {
        this.bitOrder = bitOrder;
        return this;
    }

    @Override
    public BooleanDecoderBuilder valueLengthInBits(int valueLengthInBits) {
        this.valueLengthInBits = valueLengthInBits;
        return this;
    }

    @Override
    public BooleanDecoder build() {
        return new BooleanDecoder(bitOrder, valueLengthInBits);
    }
}
