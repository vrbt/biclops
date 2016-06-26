package com.github.vrbt.biclops.decoders;

import com.github.vrbt.biclops.ordering.BitOrder;
import com.github.vrbt.biclops.ordering.Endianness;

import java.nio.ByteBuffer;

/**
 * Created by Robert on 2016-06-12.
 */
class LongDecoder extends AbstractNumberDecoder<Long> {

    LongDecoder(Endianness byteOrder, BitOrder bitOrder, int valueLengthInBits, boolean signed, int typeLengthInBits) {
        super(byteOrder, bitOrder, valueLengthInBits, signed, typeLengthInBits);
    }

    @Override
    public Long decode(ByteBuffer buffer) {
        return rawDecode(buffer).longValue();
    }
}
