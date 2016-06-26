package com.github.vrbt.biclops.decoders.interfaces;

/**
 * Created by Robert on 2016-06-26.
 */
public interface ObjectDecoderBuilder<T extends ObjectDecoder, U extends ObjectDecoderBuilder> extends CompositeDecoderBuilder<T, U> {
    U addDecoder(final Decoder singleDecoder);
}
