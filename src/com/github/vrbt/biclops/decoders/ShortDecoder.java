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
public class ShortDecoder implements Decoder<Short, Endianness> {
    private static final int SHORT_LENGTH = 16;
    private static final byte BYTE_LENGTH = 8;

    @Override
    public Short decode(ByteBuffer buffer) {
        return decode(buffer, SHORT_LENGTH);
    }

    @Override
    public Short decode(ByteBuffer buffer, Endianness endian) {
        final ByteOrder byteOrder = Endianness.BIG_ENDIAN == endian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
        buffer.order(byteOrder);
        return buffer.getShort();
    }

    @Override
    public Short decode(ByteBuffer buffer, int length) {
        final BigInteger bigInteger = new BigInteger(1, Arrays.copyOf(buffer.array(), SHORT_LENGTH / BYTE_LENGTH)).shiftRight(SHORT_LENGTH - length);
        ByteBufferUtils.leftShift(buffer, length);
        return bigInteger.shortValue();
    }

    @Override
    public Short decode(ByteBuffer buffer, Endianness order, int length) {
        return null;
    }
}
