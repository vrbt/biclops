package com.github.vrbt.biclops.decoders.interfaces;

/**
 * Created by Robert on 2016-06-26.
 */
public interface CompositeDecoderBuilder<T extends CompositeDecoder, U extends CompositeDecoderBuilder> extends DecoderBuilder<T, U> {
}
