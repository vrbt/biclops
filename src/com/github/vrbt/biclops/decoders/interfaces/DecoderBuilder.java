package com.github.vrbt.biclops.decoders.interfaces;

/**
 * Created by Robert on 2016-06-26.
 */
public interface DecoderBuilder<T extends Decoder, U extends DecoderBuilder> {
    T build();
}
