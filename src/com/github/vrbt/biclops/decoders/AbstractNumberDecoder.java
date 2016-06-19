package com.github.vrbt.biclops.decoders;

import com.github.vrbt.biclops.ordering.BitOrder;
import com.github.vrbt.biclops.ordering.Endianness;
import com.github.vrbt.biclops.utils.ByteBufferUtils;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static com.github.vrbt.biclops.ordering.BitOrder.MOST_SIGNIFICANT_BIT_FIRST;
import static com.github.vrbt.biclops.ordering.Endianness.BIG_ENDIAN;

/**
 * Created by Robert on 2016-06-18.
 */
public abstract class AbstractNumberDecoder<T> implements Decoder<T> {

    protected static final byte BYTE_LENGTH = 8;

    protected abstract int getTypeBitLength();

    @Override
    public T decode(ByteBuffer buffer) {
        return decode(buffer, BIG_ENDIAN, MOST_SIGNIFICANT_BIT_FIRST, getTypeBitLength(), true);
    }

    @Override
    public T decode(ByteBuffer buffer, Endianness byteOrder) {
        return decode(buffer, byteOrder, MOST_SIGNIFICANT_BIT_FIRST, getTypeBitLength(), true);
    }

    @Override
    public T decode(ByteBuffer buffer, BitOrder bitOrder) {
        return decode(buffer, BIG_ENDIAN, bitOrder, getTypeBitLength(), true);
    }

    @Override
    public T decode(ByteBuffer buffer, int length) {
        return decode(buffer, BIG_ENDIAN, MOST_SIGNIFICANT_BIT_FIRST, length, true);
    }

    public T decode(ByteBuffer buffer, boolean signed) {
        return decode(buffer, BIG_ENDIAN, MOST_SIGNIFICANT_BIT_FIRST, getTypeBitLength(), signed);
    }

    @Override
    public T decode(ByteBuffer buffer, Endianness byteOrder, BitOrder bitOrder) {
        return decode(buffer, byteOrder, bitOrder, getTypeBitLength(), true);
    }

    @Override
    public T decode(ByteBuffer buffer, Endianness byteOrder, int length) {
        return decode(buffer, byteOrder, MOST_SIGNIFICANT_BIT_FIRST, length, true);
    }

    public T decode(ByteBuffer buffer, Endianness byteOrder, boolean signed) {
        return decode(buffer, byteOrder, MOST_SIGNIFICANT_BIT_FIRST, getTypeBitLength(), signed);
    }

    @Override
    public T decode(ByteBuffer buffer, BitOrder bitOrder, int length) {
        return decode(buffer, BIG_ENDIAN, bitOrder, length, true);
    }

    public T decode(ByteBuffer buffer, BitOrder bitOrder, boolean signed) {
        return decode(buffer, BIG_ENDIAN, bitOrder, getTypeBitLength(), signed);
    }

    public T decode(ByteBuffer buffer, int length, boolean signed) {
        return decode(buffer, BIG_ENDIAN, MOST_SIGNIFICANT_BIT_FIRST, length, signed);
    }

    @Override
    public T decode(ByteBuffer buffer, Endianness byteOrder, BitOrder bitOrder, int length) {
        return decode(buffer, byteOrder, bitOrder, length, true);
    }

    public T decode(ByteBuffer buffer, Endianness byteOrder, BitOrder bitOrder, boolean signed) {
        return decode(buffer, byteOrder, bitOrder, getTypeBitLength(), signed);
    }

    public T decode(ByteBuffer buffer, Endianness byteOrder, int length, boolean signed) {
        return decode(buffer, byteOrder, MOST_SIGNIFICANT_BIT_FIRST, length, signed);
    }

    public T decode(ByteBuffer buffer, BitOrder bitOrder, int length, boolean signed) {
        return decode(buffer, BIG_ENDIAN, bitOrder, length, signed);
    }

    public abstract T decode(ByteBuffer buffer, Endianness byteOrder, BitOrder bitOrder, int length, boolean signed);

    protected BigInteger rawDecode(ByteBuffer buffer, Endianness byteOrder, BitOrder bitOrder, int length, boolean signed) {
        final BigInteger bigInteger = signed ? new BigInteger(Arrays.copyOf(buffer.array(), getTypeBitLength() / BYTE_LENGTH)).shiftRight(getTypeBitLength() - length) : new BigInteger(1, Arrays.copyOf(buffer.array(), getTypeBitLength() / BYTE_LENGTH)).shiftRight(getTypeBitLength() - length);
        ByteBufferUtils.leftShift(buffer, length);
        return ByteBufferUtils.reorder(bigInteger, byteOrder, bitOrder, length, signed);
    }
}
