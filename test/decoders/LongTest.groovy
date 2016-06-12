package decoders

import com.github.vrbt.biclops.annotations.LongField
import com.github.vrbt.biclops.decoders.LongDecoder
import com.github.vrbt.biclops.ordering.Endianness
import com.google.common.primitives.Longs
import org.apache.commons.lang3.StringUtils
import spock.lang.Specification
import spock.lang.Unroll

import java.lang.annotation.Annotation
import java.nio.ByteBuffer

/**
 * Created by Robert on 2016-06-12.
 */
class LongTest extends Specification {
    private class BoxedLongObject {
        @LongField
        private Long field;
    }

    def 'detect @LongField annotation on a Long field'() {
        given:
        def flagField = BoxedLongObject.class.declaredFields.find { field -> StringUtils.equalsIgnoreCase(field.getName(), 'field') }

        when:
        def contains = flagField.annotations.any { Annotation annotation -> StringUtils.containsIgnoreCase(annotation.annotationType().name, 'LongField') }

        then:
        contains
    }

    private class BoxedLongPrimitive {
        @LongField
        private long field;
    }

    def 'detect @LongField annotation on a long field'() {
        given:
        def flagField = BoxedLongPrimitive.class.declaredFields.find { field -> StringUtils.equalsIgnoreCase(field.getName(), 'field') }

        when:
        def contains = flagField.annotations.any { Annotation annotation -> StringUtils.containsIgnoreCase(annotation.annotationType().name, 'LongField') }

        then:
        contains
    }

    @Unroll
    def 'decode a whole long from the buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap Longs.toByteArray(intValue)
        def decoder = new LongDecoder()

        when:
        def decoded = decoder.decode buffer

        then:
        decoded == decodedValue

        where:
        intValue                  | decodedValue
        0 as long                 | 0 as long
        1 as long                 | 1 as long
        Integer.MAX_VALUE as long | Integer.MAX_VALUE as long
        Integer.MIN_VALUE as long | Integer.MIN_VALUE as long
        Byte.MAX_VALUE as long    | Byte.MAX_VALUE as long
        Byte.MIN_VALUE as long    | Byte.MIN_VALUE as long
        Short.MAX_VALUE as long   | Short.MAX_VALUE as long
        Short.MIN_VALUE as long   | Short.MIN_VALUE as long
        Long.MAX_VALUE as long    | Long.MAX_VALUE as long
        Long.MIN_VALUE as long    | Long.MIN_VALUE as long
    }

    @Unroll
    def 'decode a whole long from the buffer - little endian'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap Longs.toByteArray(intValue)
        def decoder = new LongDecoder()

        when:
        def decoded = decoder.decode buffer, Endianness.LITTLE_ENDIAN

        then:
        decoded == decodedValue

        where:
        intValue                  | decodedValue
        0 as long                 | 0 as long
        1 as long                 | 72_057_594_037_927_936 as long
        Integer.MAX_VALUE as long | -554_050_781_184 as long
        Integer.MIN_VALUE as long | 554_050_781_183 as long
        Short.MAX_VALUE as long   | -36_310_271_995_674_624 as long
        Short.MIN_VALUE as long   | 36_310_271_995_674_623 as long
        Long.MAX_VALUE as long    | -129 as long
        Long.MIN_VALUE as long    | 128 as long
    }

    @Unroll
    def 'decode a 4-bit part of a long from the buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap Longs.toByteArray(intValue)
        def decoder = new LongDecoder()

        when:
        def decoded = decoder.decode buffer, 4

        then:
        decoded == decodedValue

        where:
        intValue                                                                                  | decodedValue
        0b0001_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long | 1 as long
        0b0010_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long | 2 as long
        0b0100_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long | 4 as long
        0b1000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long | 8 as long
        0b0011_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long | 3 as long
        0b0110_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long | 6 as long
        0b1001_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long | 9 as long
    }

    @Unroll
    def 'decode a 60-bit part of a long from the buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap Longs.toByteArray(intValue)
        def decoder = new LongDecoder()

        when:
        def decoded = decoder.decode buffer, 60

        then:
        decoded == decodedValue

        where:
        intValue                                                                                  | decodedValue
        0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0001_0000 as long | 1 as long
        0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0010_0000 as long | 2 as long
        0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0100_0000 as long | 4 as long
        0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_1000_0000 as long | 8 as long
        0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0011_0000 as long | 3 as long
        0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0110_0000 as long | 6 as long
        0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_1001_0000 as long | 9 as long
    }

    @Unroll
    def 'decode a various length part of a long from the buffer and shift the buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap Longs.toByteArray(intValue)
        def decoder = new LongDecoder()

        when:
        decoder.decode buffer, bitsUsed
        def shiftedInt = Longs.fromByteArray(buffer.array())

        then:
        shiftedInt == shiftedValue

        where:
        intValue                                                                                  | bitsUsed | shiftedValue
        0b0001_0001_0001_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long | 1        | 0b0010_0010_0010_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b0010_0000_0001_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long | 2        | 0b1000_0000_0100_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b0100_0000_0001_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long | 3        | 0b0000_0000_1000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b0011_0000_0000_0000_0000_0001_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long | 5        | 0b0000_0000_0000_0000_0010_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b0110_0000_0000_0000_0000_0000_0001_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long | 6        | 0b0000_0000_0000_0000_0000_0100_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b1001_0000_0000_0011_0000_0000_0000_0001_0000_0000_0000_0000_0000_0000_0000_0000 as long | 7        | 0b0000_0001_1000_0000_0000_0000_1000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b1001_0000_0000_0000_0000_0000_0000_0001_0000_0000_0000_0000_0000_0000_0000_0000 as long | 8        | 0b0000_0000_0000_0000_0000_0001_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b1001_0000_0100_0000_0000_0000_0000_0001_0000_0000_0000_0000_0000_0000_0000_0000 as long | 10       | 0b0000_0000_0000_0000_0000_0100_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b1001_0000_0000_0100_0110_0000_0000_0001_0000_0000_0000_0000_0000_0000_0000_0000 as long | 12       | 0b0100_0110_0000_0000_0001_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b1001_0000_0000_0000_0000_0000_0000_0001_0000_0000_0000_0000_0000_0000_0000_0000 as long | 16       | 0b0000_0000_0000_0001_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b1001_0000_0110_0000_0000_0101_0000_0001_0000_0000_0000_0000_0000_0000_0000_0000 as long | 20       | 0b0101_0000_0001_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b1001_0000_1000_0000_0001_0000_0010_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long | 23       | 0b0001_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b1001_0000_0000_0011_0000_0000_0100_0001_0000_0000_0000_0000_0000_0000_0000_0000 as long | 28       | 0b0001_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b1001_0100_0000_1111_0000_0000_0110_0001_0000_0000_0000_0000_0000_0000_0000_0000 as long | 29       | 0b0010_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b1001_0000_0000_1100_0000_1100_0000_1001_0000_0000_0000_0000_0000_0000_0000_0000 as long | 31       | 0b1000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b1001_0000_1110_0000_0000_0000_0110_0011_0000_0000_0000_0000_0000_0000_0000_0000 as long | 32       | 0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b1001_0000_1110_0000_0000_0000_0110_0011_0110_0000_0000_0000_0010_0000_0000_0000 as long | 48       | 0b0010_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b1001_0000_1110_0000_0000_0000_0110_0011_0000_0000_0000_0110_0000_0011_0000_0110 as long | 61       | 0b1100_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b1001_0000_1110_0000_0000_0000_0110_0011_0000_0110_0000_0000_1000_0000_0000_0001 as long | 63       | 0b1000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b1001_0000_1110_0000_0000_0000_0110_0011_0000_0000_0110_0000_0000_0000_0000_1100 as long | 64       | 0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
    }
}