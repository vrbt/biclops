package com.github.vrbt.biclops.decoders.implementation;

import com.github.vrbt.biclops.decoders.interfaces.Decoder;
import com.github.vrbt.biclops.decoders.interfaces.ObjectDecoderBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 2016-06-26.
 */
class DefaultObjectDecoderBuilder implements ObjectDecoderBuilder<DefaultObjectDecoder, DefaultObjectDecoderBuilder> {

    protected final List<Decoder> decoders = new ArrayList<>();
    protected final Class clazz;

    DefaultObjectDecoderBuilder(final Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public DefaultObjectDecoderBuilder addDecoder(Decoder singleDecoder) {
        decoders.add(singleDecoder);
        return this;
    }

    @Override
    public DefaultObjectDecoder build() {
        return new DefaultObjectDecoder(clazz, decoders);
    }
}
