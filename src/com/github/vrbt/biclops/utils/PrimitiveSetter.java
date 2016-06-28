package com.github.vrbt.biclops.utils;

import java.lang.reflect.Field;
import java.util.stream.Stream;

/**
 * Created by Robert on 2016-06-28.
 */
public enum PrimitiveSetter {
    INT((Field f, Object t, Object v) -> f.setInt(t, (Integer) v)),
    SHORT((Field f, Object t, Object v) -> f.setShort(t, (Short) v)),
    LONG((Field f, Object t, Object v) -> f.setLong(t, (Long) v)),
    BYTE((Field f, Object t, Object v) -> f.setByte(t, (Byte) v)),
    BOOLEAN((Field f, Object t, Object v) -> f.setBoolean(t, (Boolean) v));

    private final TriConsumer<Field, Object, Object> setter;

    PrimitiveSetter(final TriConsumer<Field, Object, Object> setter) {
        this.setter = setter;
    }

    public static PrimitiveSetter forName(final String name) {
        return Stream.of(values()).filter(instance -> instance.name().equalsIgnoreCase(name)).findFirst().orElseThrow(() -> new EnumConstantNotPresentException(PrimitiveSetter.class, name));
    }

    public TriConsumer getFieldSetter() {
        return setter;
    }
}
