package com.github.vrbt.biclops.decoders.implementation;

import com.github.vrbt.biclops.decoders.interfaces.CollectionDecoder;
import com.github.vrbt.biclops.decoders.interfaces.Decoder;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Robert on 2016-06-26.
 */
abstract class AbstractCollectionDecoder<T extends Collection, U> implements CollectionDecoder<T, U> {

    private final Decoder<U> singleDecoder;
    private final int size;

    protected AbstractCollectionDecoder(final Decoder singleDecoder, final int size) {
        this.singleDecoder = singleDecoder;
        this.size = size;
    }

    protected Collection<U> rawDecode(final ByteBuffer buffer) {
        Collection<U> rawCollection = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            rawCollection.add(singleDecoder.decode(buffer));
        }
        return rawCollection;
    }
}
