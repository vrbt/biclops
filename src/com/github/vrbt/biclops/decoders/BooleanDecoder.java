package com.github.vrbt.biclops.decoders;

import com.github.vrbt.biclops.utils.ByteBufferUtils;

import java.nio.ByteBuffer;
import java.util.BitSet;

/**
 * Created by Robert on 2016-06-11.
 */
public class BooleanDecoder implements Decoder<Boolean> {

    private static final byte BITS_PER_BYTE = 8;

    @Override
    public Boolean decode(final ByteBuffer buffer) {
        final BitSet set = BitSet.valueOf(buffer);
        final boolean result = set.get(buffer.array().length * 8 - 1);
        ByteBufferUtils.leftShift(buffer, 1);
        return result;
    }
}
