package com.github.vrbt.biclops.decoders.implementation;

import com.github.vrbt.biclops.decoders.interfaces.Decoder;
import com.github.vrbt.biclops.decoders.interfaces.ObjectDecoder;
import com.google.common.collect.ImmutableList;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Robert on 2016-06-26.
 */
class DefaultObjectDecoder implements ObjectDecoder<Object> {

    private final List<Decoder> decoders;
    private final Class clazz;

    DefaultObjectDecoder(final Class clazz, final List<Decoder> decoders) {
        this.clazz = clazz;
        this.decoders = ImmutableList.copyOf(decoders);
    }

    @Override
    public Object decode(ByteBuffer buffer) {
        final List<Object> fieldValues = decoders.stream().map(decoder -> decoder.decode(buffer)).collect(Collectors.toList());
        Objenesis objenesis = new ObjenesisStd();
        ObjectInstantiator creator = objenesis.getInstantiatorOf(clazz);
        final Object decodedObject = creator.newInstance();
        final List<Field> fields = Arrays.asList(clazz.getDeclaredFields()).stream().filter(f -> f.getDeclaredAnnotations().length > 0).collect(Collectors.toList());
        final int numberOfFields = fields.size();
        for (int i = 0; i < numberOfFields; i++) {
            Field field = fields.get(i);
            field.setAccessible(true);
            try {
                if (field.getType().isPrimitive()) {
                    switch (field.getType().getTypeName()) {
                        case "int":
                            field.setInt(decodedObject, (Integer) fieldValues.get(i));
                            break;
                        case "short":
                            field.setShort(decodedObject, (Short) fieldValues.get(i));
                            break;
                        case "long":
                            field.setLong(decodedObject, (Long) fieldValues.get(i));
                            break;
                        case "byte":
                            field.setByte(decodedObject, (Byte) fieldValues.get(i));
                            break;
                        case "boolean":
                            field.setBoolean(decodedObject, (Boolean) fieldValues.get(i));
                            break;
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return decodedObject;
    }
}
