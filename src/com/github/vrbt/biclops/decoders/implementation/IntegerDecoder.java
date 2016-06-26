package com.github.vrbt.biclops.decoders.implementation;

import com.github.vrbt.biclops.ordering.BitOrder;
import com.github.vrbt.biclops.ordering.Endianness;

import java.nio.ByteBuffer;

/**
 * Created by Robert on 2016-06-12.
 */
class IntegerDecoder extends AbstractNumberDecoder<Integer> {

    IntegerDecoder(Endianness byteOrder, BitOrder bitOrder, int valueLengthInBits, boolean signed, int typeLengthInBits) {
        super(byteOrder, bitOrder, valueLengthInBits, signed, typeLengthInBits);
    }

    @Override
    public Integer decode(ByteBuffer buffer) {
        return rawDecode(buffer).intValue();
    }
}
