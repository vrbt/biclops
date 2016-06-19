package com.github.vrbt.biclops.decoders;

import com.github.vrbt.biclops.ordering.BitOrder;
import com.github.vrbt.biclops.ordering.Endianness;

import java.nio.ByteBuffer;

/**
 * Created by Robert on 2016-06-19.
 */
public class CharDecoder extends AbstractNumberDecoder<Character> {
    private static final int CHAR_LENGTH = 16;

    @Override
    protected int getTypeBitLength() {
        return CHAR_LENGTH;
    }

    @Override
    public Character decode(ByteBuffer buffer, Endianness byteOrder, BitOrder bitOrder, int length, boolean signed) {
        return (char) rawDecode(buffer, byteOrder, bitOrder, length, signed).shortValue();
    }
}
