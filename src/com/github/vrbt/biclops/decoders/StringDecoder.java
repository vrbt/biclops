package com.github.vrbt.biclops.decoders;

import com.github.vrbt.biclops.ordering.BitOrder;
import com.github.vrbt.biclops.ordering.Endianness;
import com.google.common.base.Charsets;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by Robert on 2016-06-19.
 */
public class StringDecoder implements Decoder<String> {

    private final static Charset DEFAULT_CHARSET = Charsets.US_ASCII;
    private final static int DEFAULT_STRING_LENGTH = 1;
    private final static int BYTES_PER_CHARACTER = 1;

    @Override
    public String decode(ByteBuffer buffer) {
        final byte[] byteArray = new byte[DEFAULT_STRING_LENGTH];
        buffer.get(byteArray);
        return new String(byteArray, DEFAULT_CHARSET);
    }

    @Override
    public String decode(ByteBuffer buffer, Endianness byteOrder) {
        return null;
    }

    @Override
    public String decode(ByteBuffer buffer, BitOrder bitOrder) {
        return null;
    }

    @Override
    public String decode(ByteBuffer buffer, int length) {
        return null;
    }

    @Override
    public String decode(ByteBuffer buffer, Endianness byteOrder, BitOrder bitOrder) {
        return null;
    }

    @Override
    public String decode(ByteBuffer buffer, Endianness byteOrder, int length) {
        return null;
    }

    @Override
    public String decode(ByteBuffer buffer, BitOrder bitOrder, int length) {
        return null;
    }

    @Override
    public String decode(ByteBuffer buffer, Endianness byteOrder, BitOrder bitOrder, int length) {
        return null;
    }
}
