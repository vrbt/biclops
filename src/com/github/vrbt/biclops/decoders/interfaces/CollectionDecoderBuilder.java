package com.github.vrbt.biclops.decoders.interfaces;

import java.util.Collection;

/**
 * Created by Robert on 2016-06-26.
 */
public interface CollectionDecoderBuilder<T extends CollectionDecoder, U extends CollectionDecoderBuilder, V extends Collection> extends CompositeDecoderBuilder<T, U> {
    U size(final int size);
}
