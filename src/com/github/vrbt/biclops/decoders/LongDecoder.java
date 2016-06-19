package com.github.vrbt.biclops.decoders;

import com.github.vrbt.biclops.ordering.BitOrder;
import com.github.vrbt.biclops.ordering.Endianness;

import java.nio.ByteBuffer;

/**
 * Created by Robert on 2016-06-12.
 */
public class LongDecoder extends AbstractNumberDecoder<Long> {
    private static final int LONG_LENGTH = 64;

    @Override
    protected int getTypeBitLength() {
        return LONG_LENGTH;
    }

    @Override
    public Long decode(ByteBuffer buffer, Endianness byteOrder, BitOrder bitOrder, int length, boolean signed) {
        return rawDecode(buffer, byteOrder, bitOrder, length, signed).longValue();
    }
}
