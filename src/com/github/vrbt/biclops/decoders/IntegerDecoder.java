package com.github.vrbt.biclops.decoders;

import com.github.vrbt.biclops.ordering.Endianness;
import com.github.vrbt.biclops.utils.ByteBufferUtils;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Created by Robert on 2016-06-12.
 */
public class IntegerDecoder implements Decoder<Integer, Endianness> {

    private static final int INTEGER_LENGTH = 32;
    private static final byte BYTE_LENGTH = 8;

    @Override
    public Integer decode(final ByteBuffer buffer) {
        return decode(buffer, INTEGER_LENGTH);
    }

    @Override
    public Integer decode(final ByteBuffer buffer, final Endianness endian) {
        final ByteOrder byteOrder = Endianness.BIG_ENDIAN == endian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
        buffer.order(byteOrder);
        return buffer.getInt();
    }

    @Override
    public Integer decode(final ByteBuffer buffer, final int length) {
        final BigInteger bigInteger = new BigInteger(1, Arrays.copyOf(buffer.array(), INTEGER_LENGTH / BYTE_LENGTH)).shiftRight(INTEGER_LENGTH - length);
        ByteBufferUtils.leftShift(buffer, length);
        return bigInteger.intValue();
    }

    @Override
    public Integer decode(final ByteBuffer buffer, final Endianness endian, final int length) {
        return null;
    }
}
