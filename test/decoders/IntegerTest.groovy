package decoders

import com.github.vrbt.biclops.annotations.IntegerField
import com.github.vrbt.biclops.decoders.IntegerDecoder
import com.github.vrbt.biclops.ordering.Endianness
import com.google.common.primitives.Ints
import org.apache.commons.lang3.StringUtils
import spock.lang.Specification
import spock.lang.Unroll

import java.lang.annotation.Annotation
import java.nio.ByteBuffer

/**
 * Created by Robert on 2016-06-12.
 */
class IntegerTest extends Specification {
    private class BoxedIntegerObject {
        @IntegerField
        private Integer field;
    }

    def 'detect @IntegerField annotation on Integer field'() {
        given:
        def flagField = BoxedIntegerObject.class.declaredFields.find { field -> StringUtils.equalsIgnoreCase(field.getName(), 'field') }

        when:
        def contains = flagField.annotations.any { Annotation annotation -> StringUtils.containsIgnoreCase(annotation.annotationType().name, 'IntegerField') }

        then:
        contains
    }

    private class BoxedIntegerPrimitive {
        @IntegerField
        private int field;
    }

    def 'detect @IntegerField annotation on int field'() {
        given:
        def flagField = BoxedIntegerPrimitive.class.declaredFields.find { field -> StringUtils.equalsIgnoreCase(field.getName(), 'field') }

        when:
        def contains = flagField.annotations.any { Annotation annotation -> StringUtils.containsIgnoreCase(annotation.annotationType().name, 'IntegerField') }

        then:
        contains
    }

    @Unroll
    def 'decode a whole integer from the buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap Ints.toByteArray(intValue)
        def decoder = new IntegerDecoder()

        when:
        def decoded = decoder.decode buffer

        then:
        decoded == decodedValue

        where:
        intValue                 | decodedValue
        0 as int                 | 0 as int
        1 as int                 | 1 as int
        Integer.MAX_VALUE as int | Integer.MAX_VALUE as int
        Integer.MIN_VALUE as int | Integer.MIN_VALUE as int
        Byte.MAX_VALUE as int    | Byte.MAX_VALUE as int
        Byte.MIN_VALUE as int    | Byte.MIN_VALUE as int
        Short.MAX_VALUE as int   | Short.MAX_VALUE as int
        Short.MIN_VALUE as int   | Short.MIN_VALUE as int
    }

    @Unroll
    def 'decode a whole integer from the buffer - little endian'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap Ints.toByteArray(intValue)
        def decoder = new IntegerDecoder()

        when:
        def decoded = decoder.decode buffer, Endianness.LITTLE_ENDIAN

        then:
        decoded == decodedValue

        where:
        intValue                 | decodedValue
        0 as int                 | 0 as int
        1 as int                 | 16777216 as int
        Integer.MAX_VALUE as int | -129 as int
        Integer.MIN_VALUE as int | 128 as int
        Short.MAX_VALUE as int   | -8454144 as int
        Short.MIN_VALUE as int   | 8388608 as int
    }

    @Unroll
    def 'decode a 4-bit part of an integer from the buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap Ints.toByteArray(intValue)
        def decoder = new IntegerDecoder()

        when:
        def decoded = decoder.decode buffer, 4

        then:
        decoded == decodedValue

        where:
        intValue                                         | decodedValue
        0b0001_0000_0000_0000_0000_0000_0000_0000 as int | 1 as int
        0b0010_0000_0000_0000_0000_0000_0000_0000 as int | 2 as int
        0b0100_0000_0000_0000_0000_0000_0000_0000 as int | 4 as int
        0b1000_0000_0000_0000_0000_0000_0000_0000 as int | -8 as int
        0b0011_0000_0000_0000_0000_0000_0000_0000 as int | 3 as int
        0b0110_0000_0000_0000_0000_0000_0000_0000 as int | 6 as int
        0b1001_0000_0000_0000_0000_0000_0000_0000 as int | -7 as int
    }

    @Unroll
    def 'decode a various length part of an integer from the buffer and shift the buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap Ints.toByteArray(intValue)
        def decoder = new IntegerDecoder()

        when:
        decoder.decode buffer, bitsUsed
        def shiftedInt = Ints.fromByteArray(buffer.array())

        then:
        shiftedInt == shiftedValue

        where:
        intValue                                         | bitsUsed | shiftedValue
        0b0001_0001_0001_0000_0000_0000_0000_0000 as int | 1        | 0b0010_0010_0010_0000_0000_0000_0000_0000 as int
        0b0010_0000_0001_0000_0000_0000_0000_0000 as int | 2        | 0b1000_0000_0100_0000_0000_0000_0000_0000 as int
        0b0100_0000_0001_0000_0000_0000_0000_0000 as int | 3        | 0b0000_0000_1000_0000_0000_0000_0000_0000 as int
        0b0011_0000_0000_0000_0000_0001_0000_0000 as int | 5        | 0b0000_0000_0000_0000_0010_0000_0000_0000 as int
        0b0110_0000_0000_0000_0000_0000_0001_0000 as int | 6        | 0b0000_0000_0000_0000_0000_0100_0000_0000 as int
        0b1001_0000_0000_0011_0000_0000_0000_0001 as int | 7        | 0b0000_0001_1000_0000_0000_0000_1000_0000 as int
        0b1001_0000_0000_0000_0000_0000_0000_0001 as int | 8        | 0b0000_0000_0000_0000_0000_0001_0000_0000 as int
        0b1001_0000_0100_0000_0000_0000_0000_0001 as int | 10       | 0b0000_0000_0000_0000_0000_0100_0000_0000 as int
        0b1001_0000_0000_0100_0110_0000_0000_0001 as int | 12       | 0b0100_0110_0000_0000_0001_0000_0000_0000 as int
        0b1001_0000_0000_0000_0000_0000_0000_0001 as int | 16       | 0b0000_0000_0000_0001_0000_0000_0000_0000 as int
        0b1001_0000_0110_0000_0000_0101_0000_0001 as int | 20       | 0b0101_0000_0001_0000_0000_0000_0000_0000 as int
        0b1001_0000_1000_0000_0001_0000_0010_0000 as int | 23       | 0b0001_0000_0000_0000_0000_0000_0000_0000 as int
        0b1001_0000_0000_0011_0000_0000_0100_0001 as int | 28       | 0b0001_0000_0000_0000_0000_0000_0000_0000 as int
        0b1001_0100_0000_1111_0000_0000_0110_0001 as int | 29       | 0b0010_0000_0000_0000_0000_0000_0000_0000 as int
        0b1001_0000_0000_1100_0000_1100_0000_1001 as int | 31       | 0b1000_0000_0000_0000_0000_0000_0000_0000 as int
        0b1001_0000_1110_0000_0000_0000_0110_0011 as int | 32       | 0b0000_0000_0000_0000_0000_0000_0000_0000 as int
    }

    @Unroll
    def 'decode a signed or unsigned integer from the buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap Ints.toByteArray(intValue)
        def decoder = new IntegerDecoder()

        when:
        def decodedValue = decoder.decode buffer, 31, signed

        then:
        decodedValue == expectedValue

        where:
        intValue            | signed | expectedValue
        126 as int          | true   | 63 as int
        -1 as int           | true   | -1 as int
        1 as int            | true   | 0 as int
        126 as int          | false  | 63 as int
        -1 as int           | false  | 2_147_483_647 as int
        1 as int            | false  | 0 as int
        -432_226_216 as int | true   | -216_113_108 as int
        -432_226_216 as int | false  | 1_931_370_540 as int
    }
}