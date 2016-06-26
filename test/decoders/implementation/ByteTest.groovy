package decoders.implementation

import com.github.vrbt.biclops.annotations.ByteField
import com.github.vrbt.biclops.decoders.implementation.ByteDecoderBuilder
import org.apache.commons.lang3.StringUtils
import spock.lang.Specification
import spock.lang.Unroll

import java.lang.annotation.Annotation
import java.nio.ByteBuffer

import static com.github.vrbt.biclops.ordering.BitOrder.MOST_SIGNIFICANT_BIT_LAST
/**
 * Created by Robert on 2016-06-12.
 */
class ByteTest extends Specification {
    private class BoxedByteObject {
        @ByteField
        private Byte field;
    }

    def 'detect @ByteField annotation on a Byte field'() {
        given:
        def flagField = BoxedByteObject.class.declaredFields.find { field -> StringUtils.equalsIgnoreCase(field.getName(), 'field') }

        when:
        def contains = flagField.annotations.any { Annotation annotation -> StringUtils.containsIgnoreCase(annotation.annotationType().name, 'ByteField') }

        then:
        contains
    }

    private class BoxedBytePrimitive {
        @ByteField
        private byte field;
    }

    def 'detect @ByteField annotation on a byte field'() {
        given:
        def flagField = BoxedBytePrimitive.class.declaredFields.find { field -> StringUtils.equalsIgnoreCase(field.getName(), 'field') }

        when:
        def contains = flagField.annotations.any { Annotation annotation -> StringUtils.containsIgnoreCase(annotation.annotationType().name, 'ByteField') }

        then:
        contains
    }

    @Unroll
    def 'decode a whole byte from the buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap intValue
        def decoder = new ByteDecoderBuilder().build()

        when:
        def decoded = decoder.decode buffer

        then:
        decoded == decodedValue

        where:
        intValue               | decodedValue
        0 as byte              | 0 as byte
        1 as byte              | 1 as byte
        Byte.MAX_VALUE as byte | Byte.MAX_VALUE as byte
        Byte.MIN_VALUE as byte | Byte.MIN_VALUE as byte
        Byte.MAX_VALUE as byte | Byte.MAX_VALUE as byte
        Byte.MIN_VALUE as byte | Byte.MIN_VALUE as byte
    }

    @Unroll
    def 'decode 4-bit part of a byte from the buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap intValue
        def decoder = new ByteDecoderBuilder().valueLengthInBits 4 build()

        when:
        def decoded = decoder.decode buffer

        then:
        decoded == decodedValue

        where:
        intValue            | decodedValue
        0b0001_0000 as byte | 1 as byte
        0b0010_0000 as byte | 2 as byte
        0b0100_0000 as byte | 4 as byte
        0b1000_0000 as byte | -8 as byte
        0b0011_0000 as byte | 3 as byte
        0b0110_0000 as byte | 6 as byte
        0b1001_0000 as byte | -7 as byte
    }

    @Unroll
    def 'decode 4-bit part of a byte from the buffer and shift the buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap intValue
        def decoder = new ByteDecoderBuilder().valueLengthInBits bitsUsed build()

        when:
        decoder.decode buffer
        def shiftedInt = buffer.array()[0]

        then:
        shiftedInt == shiftedValue

        where:
        intValue            | bitsUsed | shiftedValue
        0b0001_0001 as byte | 1        | 0b0010_0010 as byte
        0b0010_0000 as byte | 2        | 0b1000_0000 as byte
        0b0100_0101 as byte | 3        | 0b0010_1000 as byte
        0b0100_0011 as byte | 4        | 0b0011_0000 as byte
        0b0011_0010 as byte | 5        | 0b0100_0000 as byte
        0b0110_0011 as byte | 6        | 0b1100_0000 as byte
        0b1001_0001 as byte | 7        | 0b1000_0000 as byte
        0b1001_0110 as byte | 8        | 0b0000_0000 as byte
    }

    @Unroll
    def 'decode an unsigned 6-bit part of a byte from the buffer - most significant bit last'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap intValue
        def decoder = new ByteDecoderBuilder().valueLengthInBits 6 bitOrder MOST_SIGNIFICANT_BIT_LAST signed false build()

        when:
        def decoded = decoder.decode buffer

        then:
        decoded == decodedValue

        where:
        intValue            | decodedValue
        0b0001_1100 as byte | 0b0011_1000 as byte
        0b0010_0100 as byte | 0b0010_0100 as byte
        0b0100_1000 as byte | 0b0001_0010 as byte
        0b1110_0100 as byte | 0b0010_0111 as byte
        0b0011_1000 as byte | 0b0001_1100 as byte
        0b0110_0100 as byte | 0b0010_0110 as byte
        0b1001_1100 as byte | 0b0011_1001 as byte
    }

    @Unroll
    def 'decode a signed 6-bit part of a byte from the buffer - most significant bit last'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap intValue
        def decoder = new ByteDecoderBuilder().valueLengthInBits 6 bitOrder MOST_SIGNIFICANT_BIT_LAST build()

        when:
        def decoded = decoder.decode buffer

        then:
        decoded == decodedValue

        where:
        intValue            | decodedValue
        0b0001_1100 as byte | 0b1111_1000 as byte
        0b0010_0100 as byte | 0b1110_0100 as byte
        0b0100_1000 as byte | 0b0001_0010 as byte
        0b1110_0100 as byte | 0b1110_0111 as byte
        0b0011_1000 as byte | 0b0001_1100 as byte
        0b0110_0100 as byte | 0b1110_0110 as byte
        0b1001_1100 as byte | 0b1111_1001 as byte
    }
}