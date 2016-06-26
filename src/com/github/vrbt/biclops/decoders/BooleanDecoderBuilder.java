package com.github.vrbt.biclops.decoders;

import com.github.vrbt.biclops.ordering.BitOrder;
import com.github.vrbt.biclops.ordering.Endianness;

import static com.github.vrbt.biclops.ordering.BitOrder.MOST_SIGNIFICANT_BIT_FIRST;

/**
 * Created by Robert on 2016-06-26.
 */
public class BooleanDecoderBuilder implements DecoderBuilder<BooleanDecoder> {

    private BitOrder bitOrder = MOST_SIGNIFICANT_BIT_FIRST;
    private int valueLengthInBits = 1;
    private Endianness byteOrder;

    @Override
    public DecoderBuilder<BooleanDecoder> byteOrder(Endianness byteOrder) {
        this.byteOrder = byteOrder;
        return this;
    }

    @Override
    public DecoderBuilder<BooleanDecoder> bitOrder(BitOrder bitOrder) {
        this.bitOrder = bitOrder;
        return this;
    }

    @Override
    public DecoderBuilder<BooleanDecoder> valueLengthInBits(int valueLengthInBits) {
        this.valueLengthInBits = valueLengthInBits;
        return this;
    }

    @Override
    public BooleanDecoder build() {
        return new BooleanDecoder(bitOrder, valueLengthInBits);
    }
}
