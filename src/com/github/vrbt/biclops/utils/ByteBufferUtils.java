package com.github.vrbt.biclops.utils;

import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * Created by Robert Wróblewski on 2016-06-11.
 */
public class ByteBufferUtils {
    private static final byte BITS_PER_BYTE = 8;

    private ByteBufferUtils() {
    }

    public static ByteBuffer leftShift(final ByteBuffer buffer, int shift) {
        final BigInteger bigInt = new BigInteger(buffer.array());
        final byte[] shiftedBytes = bigInt.shiftLeft(shift).and(allOnes(buffer.remaining() * BITS_PER_BYTE)).toByteArray();
        final int resultLength = buffer.capacity();
        final int sourceOffset = resultLength >= shiftedBytes.length ? 0 : 1;
        final int destinationOffset = resultLength - shiftedBytes.length > 0 ? resultLength - shiftedBytes.length : 0;
        System.arraycopy(shiftedBytes, sourceOffset, buffer.array(), destinationOffset, shiftedBytes.length - sourceOffset);
        return buffer;
    }

    private static BigInteger allOnes(final int length) {
        return BigInteger.ZERO.setBit(length).subtract(BigInteger.ONE);
    }
}
