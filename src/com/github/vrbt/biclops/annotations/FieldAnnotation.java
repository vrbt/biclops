package com.github.vrbt.biclops.annotations;

import java.lang.annotation.Annotation;
import java.util.EnumSet;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by Robert on 2016-06-28.
 */
public enum FieldAnnotation {
    BIG_INTEGER_FIELD(BigIntegerField.class),
    BIT_FIELD(BitField.class),
    BYTE_FIELD(ByteField.class),
    INTEGER_FIELD(IntegerField.class),
    LONG_FIELD(LongField.class),
    SHORT_FIELD(ShortField.class),
    STRING_FIELD(StringField.class);

    private static List<? super Annotation> annotationClasses;
    private final Class<? extends Annotation> annotationClass;

    FieldAnnotation(final Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public static boolean containsFieldAnnotation(final List<? extends Annotation> annotations) {
        return annotations.stream().anyMatch(FieldAnnotation::isFieldAnnotation);
    }

    private static boolean isFieldAnnotation(final Annotation annotation) {
        return getAnnotationClasses().contains(annotation.annotationType());
    }

    private static List<? super Annotation> getAnnotationClasses() {
        if (annotationClasses == null) {
            annotationClasses = EnumSet.allOf(FieldAnnotation.class).stream().map(FieldAnnotation::getAnnotation).collect(toList());
        }
        return annotationClasses;
    }

    public Class<?> getAnnotation() {
        return annotationClass;
    }
}
