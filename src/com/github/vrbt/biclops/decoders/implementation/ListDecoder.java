package com.github.vrbt.biclops.decoders.implementation;

import com.github.vrbt.biclops.decoders.interfaces.Decoder;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 2016-06-26.
 */
class ListDecoder<U> extends AbstractCollectionDecoder<List<U>, U> {

    ListDecoder(final Decoder singleDecoder, final int size) {
        super(singleDecoder, size);
    }

    @Override
    public List<U> decode(ByteBuffer buffer) {
        return new ArrayList<U>(rawDecode(buffer));
    }
}
