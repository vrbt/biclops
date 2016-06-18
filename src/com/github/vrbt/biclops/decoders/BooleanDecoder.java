package com.github.vrbt.biclops.decoders;

import com.github.vrbt.biclops.ordering.BitOrder;
import com.github.vrbt.biclops.ordering.Endianness;
import com.github.vrbt.biclops.utils.ByteBufferUtils;

import java.nio.ByteBuffer;
import java.util.BitSet;

import static com.github.vrbt.biclops.ordering.BitOrder.MOST_SIGNIFICANT_BIT_FIRST;
import static com.github.vrbt.biclops.ordering.Endianness.BIG_ENDIAN;

/**
 * Created by Robert on 2016-06-11.
 */
public class BooleanDecoder implements Decoder<Boolean> {

    private static final byte BYTE_LENGTH = 8;
    private static final int DEFAULT_BIT_FIELD_LENGTH = 1;

    @Override
    public Boolean decode(final ByteBuffer buffer) {
        return decode(buffer, BIG_ENDIAN, MOST_SIGNIFICANT_BIT_FIRST, DEFAULT_BIT_FIELD_LENGTH);
    }

    @Override
    public Boolean decode(ByteBuffer buffer, Endianness byteOrder) {
        return decode(buffer, BIG_ENDIAN, MOST_SIGNIFICANT_BIT_FIRST, DEFAULT_BIT_FIELD_LENGTH);
    }

    @Override
    public Boolean decode(final ByteBuffer buffer, final BitOrder bitOrder) {
        return decode(buffer, BIG_ENDIAN, bitOrder, MOST_SIGNIFICANT_BIT_FIRST == bitOrder ? DEFAULT_BIT_FIELD_LENGTH : BYTE_LENGTH);
    }

    @Override
    public Boolean decode(final ByteBuffer buffer, final int length) {
        return decode(buffer, BIG_ENDIAN, MOST_SIGNIFICANT_BIT_FIRST, length);
    }

    @Override
    public Boolean decode(ByteBuffer buffer, Endianness byteOrder, BitOrder bitOrder) {
        return decode(buffer, byteOrder, bitOrder, DEFAULT_BIT_FIELD_LENGTH);
    }

    @Override
    public Boolean decode(ByteBuffer buffer, Endianness byteOrder, int length) {
        return decode(buffer, byteOrder, MOST_SIGNIFICANT_BIT_FIRST, length);
    }

    @Override
    public Boolean decode(final ByteBuffer buffer, final BitOrder bitOrder, final int length) {
        return decode(buffer, BIG_ENDIAN, bitOrder, length);
    }

    @Override
    public Boolean decode(ByteBuffer buffer, Endianness byteOrder, BitOrder bitOrder, int length) {
        final BitSet set = BitSet.valueOf(buffer);
        final int mostSignificantBitIndex = MOST_SIGNIFICANT_BIT_FIRST == bitOrder ? buffer.array().length * BYTE_LENGTH - 1 : 0;
        final boolean result = set.get(mostSignificantBitIndex);
        ByteBufferUtils.leftShift(buffer, length);
        return result;
    }
}
