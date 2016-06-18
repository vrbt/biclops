package com.github.vrbt.biclops.decoders;

import com.github.vrbt.biclops.ordering.BitOrder;
import com.github.vrbt.biclops.ordering.Endianness;

import java.nio.ByteBuffer;

/**
 * Created by Robert on 2016-06-12.
 */
public class IntegerDecoder extends AbstractNumberDecoder<Integer> {

    private static final int INTEGER_LENGTH = 32;

    @Override
    protected int getTypeBitLength() {
        return INTEGER_LENGTH;
    }
    @Override
    public Integer decode(ByteBuffer buffer, Endianness byteOrder, BitOrder bitOrder, int length) {
        return rawDecode(buffer, byteOrder, bitOrder, length).intValue();
    }
}
