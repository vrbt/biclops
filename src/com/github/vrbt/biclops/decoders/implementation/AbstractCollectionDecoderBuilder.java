package com.github.vrbt.biclops.decoders.implementation;

import com.github.vrbt.biclops.decoders.interfaces.CollectionDecoderBuilder;
import com.github.vrbt.biclops.decoders.interfaces.Decoder;

import java.util.Collection;

/**
 * Created by Robert on 2016-06-26.
 */
abstract class AbstractCollectionDecoderBuilder<T extends AbstractCollectionDecoder, U extends AbstractCollectionDecoderBuilder, V extends Collection> implements CollectionDecoderBuilder<T, U, V> {

    protected int size = 1;
    protected Decoder decoder;

    protected AbstractCollectionDecoderBuilder(final Decoder singleDecoder) {
        this.decoder = singleDecoder;
    }

    @Override
    public U size(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Size cannot be negative");
        }
        this.size = size;
        return (U) this;
    }
}
