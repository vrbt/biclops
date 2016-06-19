package com.github.vrbt.biclops.utils;

import com.github.vrbt.biclops.ordering.BitOrder;
import com.github.vrbt.biclops.ordering.Endianness;
import com.google.common.primitives.Ints;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.stream.Stream.of;
import static org.apache.commons.lang3.ArrayUtils.toObject;
import static org.apache.commons.lang3.ArrayUtils.toPrimitive;

/**
 * Created by Robert Wróblewski on 2016-06-11.
 */
public class ByteBufferUtils {
    private static final byte BYTE_LENGTH = 8;

    private ByteBufferUtils() {
    }

    public static ByteBuffer leftShift(final ByteBuffer buffer, int shift) {
        final BigInteger bigInt = new BigInteger(buffer.array());
        final byte[] shiftedBytes = bigInt.shiftLeft(shift).and(allOnes((buffer.remaining()) * BYTE_LENGTH)).toByteArray();
        final int resultLength = buffer.capacity();
        final int sourceOffset = resultLength >= shiftedBytes.length ? 0 : 1;
        final int destinationOffset = resultLength - shiftedBytes.length > 0 ? resultLength - shiftedBytes.length : 0;
        Arrays.fill(buffer.array(), (byte) 0);
        System.arraycopy(shiftedBytes, sourceOffset, buffer.array(), destinationOffset, shiftedBytes.length - sourceOffset);
        return buffer;
    }

    private static BigInteger allOnes(final int length) {
        return BigInteger.ZERO.setBit(length).subtract(BigInteger.ONE);
    }

    public static BigInteger reorder(final BigInteger input, final Endianness byteOrder, final BitOrder bitOrder, final int bitLength, final boolean signed) {
        final int byteLength = bitLength / BYTE_LENGTH + (bitLength % BYTE_LENGTH > 0 ? 1 : 0);
        final byte[] paddedBytes = new byte[byteLength];
        final byte[] inputBytes = input.toByteArray();
        final int sourceOffset = inputBytes.length - byteLength > 0 ? inputBytes.length - byteLength : 0;
        final int copiedLength = inputBytes.length - sourceOffset;
        final int destinationOffset = byteLength - inputBytes.length > 0 ? byteLength - inputBytes.length : 0;
        System.arraycopy(input.toByteArray(), sourceOffset, paddedBytes, destinationOffset, copiedLength);
        if (byteOrder != Endianness.BIG_ENDIAN) {
            if (bitOrder != BitOrder.MOST_SIGNIFICANT_BIT_FIRST) {
                if (signed) {
                    return new BigInteger(toPrimitive(fullReverse(toObject(paddedBytes)))).shiftRight(byteLength * 8 - bitLength);
                } else {
                    return new BigInteger(1, toPrimitive(fullReverse(toObject(paddedBytes)))).shiftRight(byteLength * 8 - bitLength);
                }
            } else {
                if (signed) {
                    return new BigInteger(toPrimitive(reverseBytes(toObject(paddedBytes))));
                } else {
                    return new BigInteger(1, toPrimitive(reverseBytes(toObject(paddedBytes))));
                }
            }
        } else {
            if (bitOrder != BitOrder.MOST_SIGNIFICANT_BIT_FIRST) {
                if (signed) {
                    return new BigInteger(toPrimitive(reverseBits(toObject(paddedBytes)))).shiftRight(byteLength * 8 - bitLength);
                } else {
                    return new BigInteger(1, toPrimitive(reverseBits(toObject(paddedBytes)))).shiftRight(byteLength * 8 - bitLength);
                }

            } else {
                return input;
            }
        }
    }

    public static BigInteger fullReverse(final BigInteger input, final int length) {
        return reorder(input, Endianness.LITTLE_ENDIAN, BitOrder.MOST_SIGNIFICANT_BIT_LAST, length, true);
    }

    public static BigInteger fullReverse(final BigInteger input, final int length, final boolean signed) {
        return reorder(input, Endianness.LITTLE_ENDIAN, BitOrder.MOST_SIGNIFICANT_BIT_LAST, length, signed);
    }

    public static BigInteger reverseBytes(final BigInteger input, final int length) {
        return reorder(input, Endianness.LITTLE_ENDIAN, BitOrder.MOST_SIGNIFICANT_BIT_FIRST, length, true);
    }

    public static BigInteger reverseBytes(final BigInteger input, final int length, final boolean signed) {
        return reorder(input, Endianness.LITTLE_ENDIAN, BitOrder.MOST_SIGNIFICANT_BIT_FIRST, length, signed);
    }

    public static BigInteger reverseBits(final BigInteger input, final int length) {
        return reorder(input, Endianness.BIG_ENDIAN, BitOrder.MOST_SIGNIFICANT_BIT_LAST, length, true);
    }

    public static BigInteger reverseBits(final BigInteger input, final int length, final boolean signed) {
        return reorder(input, Endianness.BIG_ENDIAN, BitOrder.MOST_SIGNIFICANT_BIT_LAST, length, signed);
    }

    public static Byte[] fullReverse(final Byte[] input) {
        final Byte[] reversedBytes = reverseBytes(input);
        return of(reversedBytes).map(ByteBufferUtils::reverseBits).collect(Collectors.toList()).toArray(new Byte[0]);
    }

    public static Byte[] reverseBytes(final Byte[] input) {
        final List<Byte> reversed = asList(input);
        Collections.reverse(reversed);
        return reversed.toArray(new Byte[0]);
    }

    public static Byte[] reverseBits(final Byte[] input) {
        return of(input).map(ByteBufferUtils::reverseBits).collect(Collectors.toList()).toArray(new Byte[0]);
    }

    public static byte reverseBits(final byte input) {
        final int input32 = input;
        return Ints.toByteArray(Integer.reverse(input32 << 24) & 0xff)[3];
    }

    public static Byte reverseBits(final Byte input) {
        return reverseBits(input.byteValue());
    }
}
