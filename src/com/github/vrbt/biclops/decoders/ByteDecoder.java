package com.github.vrbt.biclops.decoders;

import com.github.vrbt.biclops.ordering.BitOrder;
import com.github.vrbt.biclops.utils.ByteBufferUtils;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by Robert on 2016-06-12.
 */
public class ByteDecoder implements Decoder<Byte, BitOrder> {
    private static final byte BYTE_LENGTH = 8;

    @Override
    public Byte decode(ByteBuffer buffer) {
        return decode(buffer, BYTE_LENGTH);
    }

    @Override
    public Byte decode(ByteBuffer buffer, BitOrder endian) {
        return decode(buffer, BYTE_LENGTH);
    }

    @Override
    public Byte decode(ByteBuffer buffer, int length) {
        final BigInteger bigInteger = new BigInteger(1, Arrays.copyOf(buffer.array(), BYTE_LENGTH / BYTE_LENGTH)).shiftRight(BYTE_LENGTH - length);
        ByteBufferUtils.leftShift(buffer, length);
        return bigInteger.byteValue();
    }

    @Override
    public Byte decode(ByteBuffer buffer, BitOrder order, int length) {
        return decode(buffer, length);
    }
}
