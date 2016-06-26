package com.github.vrbt.biclops.decoders.implementation;

import com.github.vrbt.biclops.decoders.interfaces.SingleDecoder;
import com.github.vrbt.biclops.ordering.BitOrder;
import com.github.vrbt.biclops.utils.ByteBufferUtils;

import java.nio.ByteBuffer;
import java.util.BitSet;

import static com.github.vrbt.biclops.ordering.BitOrder.MOST_SIGNIFICANT_BIT_FIRST;

/**
 * Created by Robert on 2016-06-11.
 */
class BooleanDecoder implements SingleDecoder<Boolean> {

    private static final byte BYTE_LENGTH = 8;
    private final BitOrder bitOrder;
    private final int length;

    BooleanDecoder(final BitOrder bitOrder, final int length) {
        this.bitOrder = bitOrder;
        this.length = length;
    }

    @Override
    public Boolean decode(ByteBuffer buffer) {
        final BitSet set = BitSet.valueOf(buffer);
        final int mostSignificantBitIndex = MOST_SIGNIFICANT_BIT_FIRST == bitOrder ? buffer.array().length * BYTE_LENGTH - 1 : 0;
        final boolean result = set.get(mostSignificantBitIndex);
        ByteBufferUtils.leftShift(buffer, MOST_SIGNIFICANT_BIT_FIRST == bitOrder ? length : BYTE_LENGTH);
        return result;
    }
}
