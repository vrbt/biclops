package com.github.vrbt.biclops.decoders;

import com.github.vrbt.biclops.ordering.BitOrder;
import com.github.vrbt.biclops.ordering.Endianness;

import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * Created by Robert on 2016-06-12.
 */
public class BigIntegerDecoder extends AbstractNumberDecoder<BigInteger> {

    private static final int BIG_INTEGER_DEFAULT_LENGTH = 128;

    @Override
    protected int getTypeBitLength() {
        return BIG_INTEGER_DEFAULT_LENGTH;
    }

    @Override
    public BigInteger decode(ByteBuffer buffer, Endianness byteOrder, BitOrder bitOrder, int length, boolean signed) {
        return rawDecode(buffer, byteOrder, bitOrder, length, signed);
    }
}
