package com.github.vrbt.biclops.decoders.implementation;

import com.github.vrbt.biclops.decoders.interfaces.Decoder;
import com.github.vrbt.biclops.decoders.interfaces.ObjectDecoder;
import com.github.vrbt.biclops.utils.PrimitiveSetter;
import com.github.vrbt.biclops.utils.TriConsumer;
import com.google.common.collect.ImmutableList;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.github.vrbt.biclops.annotations.FieldAnnotation.containsFieldAnnotation;
import static java.util.stream.Collectors.toList;

/**
 * Created by Robert on 2016-06-26.
 */
class DefaultObjectDecoder<T> implements ObjectDecoder<T> {

    private final List<Decoder> decoders;
    private final Class<T> clazz;

    DefaultObjectDecoder(final Class<T> clazz, final List<? extends Decoder> decoders) {
        this.clazz = clazz;
        this.decoders = ImmutableList.copyOf(decoders);
    }

    @Override
    public T decode(ByteBuffer buffer) {
        final List<Object> fieldValues = decoders.stream().map(decoder -> decoder.decode(buffer)).collect(toList());
        Objenesis objenesis = new ObjenesisStd();
        ObjectInstantiator creator = objenesis.getInstantiatorOf(clazz);
        final T decodedObject = clazz.cast(creator.newInstance());
        final List<Field> fields = Stream.of(clazz.getDeclaredFields())
                .filter(field -> containsFieldAnnotation(Arrays.asList(field.getDeclaredAnnotations()))).collect(toList());
        final int numberOfFields = fields.size();
        for (int i = 0; i < numberOfFields; i++) {
            Field field = fields.get(i);
            field.setAccessible(true);
            try {
                if (field.getType().isPrimitive()) {
                    setPrimitiveField(field, decodedObject, fieldValues.get(i));
                } else {
                    field.set(decodedObject, fieldValues.get(i));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return decodedObject;
    }

    private void setPrimitiveField(final Field field, final T target, final Object value) throws IllegalAccessException {
        TriConsumer<Field, T, Object> consumer = Optional.ofNullable(PrimitiveSetter.forName(field.getType().getTypeName())).map(setter -> setter.getFieldSetter()).orElse(null);
        if (consumer != null) {
            consumer.apply(field, target, value);
        }
    }


}
