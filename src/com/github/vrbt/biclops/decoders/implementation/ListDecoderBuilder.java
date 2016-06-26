package com.github.vrbt.biclops.decoders.implementation;

import com.github.vrbt.biclops.decoders.interfaces.Decoder;

import java.util.List;

/**
 * Created by Robert on 2016-06-26.
 */
class ListDecoderBuilder extends AbstractCollectionDecoderBuilder<ListDecoder, ListDecoderBuilder, List> {

    public ListDecoderBuilder(final Decoder singleDecoder) {
        super(singleDecoder);
    }

    @Override
    public ListDecoder build() {
        return new ListDecoder(decoder, size);
    }
}
