package com.github.vrbt.biclops.decoders.implementation;

import com.github.vrbt.biclops.decoders.interfaces.Decoder;
import com.github.vrbt.biclops.decoders.interfaces.ObjectDecoderBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 2016-06-26.
 */
class DefaultObjectDecoderBuilder<T> implements ObjectDecoderBuilder<DefaultObjectDecoder<T>, DefaultObjectDecoderBuilder<T>> {

    protected final List<Decoder<T>> decoders = new ArrayList<>();
    protected final Class<T> clazz;

    DefaultObjectDecoderBuilder(final Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public DefaultObjectDecoderBuilder addDecoder(Decoder singleDecoder) {
        decoders.add(singleDecoder);
        return this;
    }

    @Override
    public DefaultObjectDecoder<T> build() {
        return new DefaultObjectDecoder(clazz, decoders);
    }
}
