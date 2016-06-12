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
public class LongDecoder implements Decoder<Long, Endianness> {
    private static final int LONG_LENGTH = 64;
    private static final byte BYTE_LENGTH = 8;

    @Override
    public Long decode(ByteBuffer buffer) {
        return decode(buffer, LONG_LENGTH);
    }

    @Override
    public Long decode(ByteBuffer buffer, Endianness endian) {
        final ByteOrder byteOrder = Endianness.BIG_ENDIAN == endian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
        buffer.order(byteOrder);
        return buffer.getLong();
    }

    @Override
    public Long decode(ByteBuffer buffer, int length) {
        final BigInteger bigInteger = new BigInteger(1, Arrays.copyOf(buffer.array(), LONG_LENGTH / BYTE_LENGTH)).shiftRight(LONG_LENGTH - length);
        ByteBufferUtils.leftShift(buffer, length);
        return bigInteger.longValue();
    }

    @Override
    public Long decode(ByteBuffer buffer, Endianness order, int length) {
        return null;
    }
}
