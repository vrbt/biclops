package com.github.vrbt.biclops.decoders.implementation;

import com.github.vrbt.biclops.decoders.interfaces.NumberDecoder;
import com.github.vrbt.biclops.ordering.BitOrder;
import com.github.vrbt.biclops.ordering.Endianness;
import com.github.vrbt.biclops.utils.ByteBufferUtils;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by Robert on 2016-06-18.
 */
abstract class AbstractNumberDecoder<T extends Number> implements NumberDecoder<T> {

    private final Endianness byteOrder;
    private final BitOrder bitOrder;
    private final int valueLengthInBits;
    private final boolean signed;
    private final int typeLengthInBits;

    protected AbstractNumberDecoder(final Endianness byteOrder, final BitOrder bitOrder, final int valueLengthInBits, final boolean signed, final int typeLengthInBits) {
        this.byteOrder = byteOrder;
        this.bitOrder = bitOrder;
        this.valueLengthInBits = valueLengthInBits;
        this.signed = signed;
        this.typeLengthInBits = typeLengthInBits;
    }

    @Override
    public abstract T decode(ByteBuffer buffer);

    protected final BigInteger rawDecode(ByteBuffer buffer) {
        final BigInteger bigInteger = signed ? new BigInteger(Arrays.copyOf(buffer.array(), typeLengthInBits / BYTE_LENGTH)).shiftRight(typeLengthInBits - valueLengthInBits) : new BigInteger(1, Arrays.copyOf(buffer.array(), typeLengthInBits / BYTE_LENGTH)).shiftRight(typeLengthInBits - valueLengthInBits);
        ByteBufferUtils.leftShift(buffer, valueLengthInBits);
        return ByteBufferUtils.reorder(bigInteger, byteOrder, bitOrder, valueLengthInBits, signed);
    }
}
