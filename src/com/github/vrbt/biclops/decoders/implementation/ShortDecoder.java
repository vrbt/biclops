package com.github.vrbt.biclops.decoders.implementation;

import com.github.vrbt.biclops.ordering.BitOrder;
import com.github.vrbt.biclops.ordering.Endianness;

import java.nio.ByteBuffer;

/**
 * Created by Robert on 2016-06-12.
 */
class ShortDecoder extends AbstractNumberDecoder<Short> {

    ShortDecoder(Endianness byteOrder, BitOrder bitOrder, int valueLengthInBits, boolean signed, int typeLengthInBits) {
        super(byteOrder, bitOrder, valueLengthInBits, signed, typeLengthInBits);
    }

    @Override
    public Short decode(ByteBuffer buffer) {
        return rawDecode(buffer).shortValue();
    }

}
