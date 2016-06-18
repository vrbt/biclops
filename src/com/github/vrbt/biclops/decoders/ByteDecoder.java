package com.github.vrbt.biclops.decoders;

import com.github.vrbt.biclops.ordering.BitOrder;
import com.github.vrbt.biclops.ordering.Endianness;

import java.nio.ByteBuffer;

/**
 * Created by Robert on 2016-06-12.
 */
public class ByteDecoder extends AbstractNumberDecoder<Byte> {
    @Override
    protected int getTypeBitLength() {
        return BYTE_LENGTH;
    }

    @Override
    public Byte decode(ByteBuffer buffer, Endianness byteOrder, BitOrder bitOrder, int length) {
        return rawDecode(buffer, byteOrder, bitOrder, length).byteValue();
    }
}
