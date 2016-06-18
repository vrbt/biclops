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
public abstract class AbstractNumberDecoder<T extends Number> implements Decoder<Number> {

    protected static final byte BYTE_LENGTH = 8;

    protected abstract int getTypeBitLength();

    @Override
    public T decode(ByteBuffer buffer) {
        return decode(buffer, BIG_ENDIAN, MOST_SIGNIFICANT_BIT_FIRST, getTypeBitLength());
    }

    @Override
    public T decode(ByteBuffer buffer, Endianness byteOrder) {
        return decode(buffer, byteOrder, MOST_SIGNIFICANT_BIT_FIRST, getTypeBitLength());
    }

    @Override
    public T decode(ByteBuffer buffer, BitOrder bitOrder) {
        return decode(buffer, BIG_ENDIAN, bitOrder, getTypeBitLength());
    }

    @Override
    public T decode(ByteBuffer buffer, int length) {
        return decode(buffer, BIG_ENDIAN, MOST_SIGNIFICANT_BIT_FIRST, length);
    }

    @Override
    public T decode(ByteBuffer buffer, Endianness byteOrder, BitOrder bitOrder) {
        return decode(buffer, byteOrder, bitOrder, getTypeBitLength());
    }

    @Override
    public T decode(ByteBuffer buffer, Endianness byteOrder, int length) {
        return decode(buffer, byteOrder, MOST_SIGNIFICANT_BIT_FIRST, length);
    }

    @Override
    public T decode(ByteBuffer buffer, BitOrder bitOrder, int length) {
        return decode(buffer, BIG_ENDIAN, bitOrder, length);
    }

    @Override
    public abstract T decode(ByteBuffer buffer, Endianness byteOrder, BitOrder bitOrder, int length);

    protected BigInteger rawDecode(ByteBuffer buffer, Endianness byteOrder, BitOrder bitOrder, int length) {
        final BigInteger bigInteger = new BigInteger(1, Arrays.copyOf(buffer.array(), getTypeBitLength() / BYTE_LENGTH)).shiftRight(getTypeBitLength() - length);
        ByteBufferUtils.leftShift(buffer, length);
        return ByteBufferUtils.reorder(bigInteger, byteOrder, bitOrder, length);
    }
}
