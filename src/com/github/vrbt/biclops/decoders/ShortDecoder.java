package com.github.vrbt.biclops.decoders;

import com.github.vrbt.biclops.ordering.BitOrder;
import com.github.vrbt.biclops.ordering.Endianness;

import java.nio.ByteBuffer;

/**
 * Created by Robert on 2016-06-12.
 */
public class ShortDecoder extends AbstractNumberDecoder<Short> {
    private static final int SHORT_LENGTH = 16;

    @Override
    protected int getTypeBitLength() {
        return SHORT_LENGTH;
    }

    @Override
    public Short decode(ByteBuffer buffer, Endianness byteOrder, BitOrder bitOrder, int length, boolean signed) {
        return rawDecode(buffer, byteOrder, bitOrder, length, signed).shortValue();
    }
}
