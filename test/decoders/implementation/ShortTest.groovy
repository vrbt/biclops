package decoders.implementation

import com.github.vrbt.biclops.annotations.ShortField
import com.github.vrbt.biclops.decoders.implementation.ShortDecoderBuilder
import com.google.common.primitives.Shorts
import org.apache.commons.lang3.StringUtils
import spock.lang.Specification
import spock.lang.Unroll

import java.lang.annotation.Annotation
import java.nio.ByteBuffer

import static com.github.vrbt.biclops.ordering.BitOrder.MOST_SIGNIFICANT_BIT_LAST
import static com.github.vrbt.biclops.ordering.Endianness.LITTLE_ENDIAN
/**
 * Created by Robert on 2016-06-12.
 */
class ShortTest extends Specification {
    private class BoxedShortObject {
        @ShortField
        private Short field;
    }

    def 'detect @ShortField annotation on a Short field'() {
        given:
        def flagField = BoxedShortObject.class.declaredFields.find { field -> StringUtils.equalsIgnoreCase(field.getName(), 'field') }

        when:
        def contains = flagField.annotations.any { Annotation annotation -> StringUtils.containsIgnoreCase(annotation.annotationType().name, 'ShortField') }

        then:
        contains
    }

    private class BoxedShortPrimitive {
        @ShortField
        private short field;
    }

    def 'detect @ShortField annotation on a short field'() {
        given:
        def flagField = BoxedShortPrimitive.class.declaredFields.find { field -> StringUtils.equalsIgnoreCase(field.getName(), 'field') }

        when:
        def contains = flagField.annotations.any { Annotation annotation -> StringUtils.containsIgnoreCase(annotation.annotationType().name, 'ShortField') }

        then:
        contains
    }

    @Unroll
    def 'decode a whole short from the buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap Shorts.toByteArray(intValue)
        def decoder = new ShortDecoderBuilder().build()

        when:
        def decoded = decoder.decode buffer

        then:
        decoded == decodedValue

        where:
        intValue                 | decodedValue
        0 as short               | 0 as short
        1 as short               | 1 as short
        Byte.MAX_VALUE as short  | Byte.MAX_VALUE as short
        Byte.MIN_VALUE as short  | Byte.MIN_VALUE as short
        Short.MAX_VALUE as short | Short.MAX_VALUE as short
        Short.MIN_VALUE as short | Short.MIN_VALUE as short
    }

    @Unroll
    def 'decode a whole short from the buffer - little endian'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap Shorts.toByteArray(intValue)
        def decoder = new ShortDecoderBuilder().byteOrder LITTLE_ENDIAN build()

        when:
        def decoded = decoder.decode buffer

        then:
        decoded == decodedValue

        where:
        intValue                       | decodedValue
        0b0000_0000_0000_0000 as short | 0b0000_0000_0000_0000 as short
        0b0000_0000_0000_0001 as short | 0b0000_0001_0000_0000 as short
        0b0001_0011_0100_0001 as short | 0b0100_0001_0001_0011 as short
        0b0111_1111_1111_1111 as short | 0b1111_1111_0111_1111 as short
        0b1000_0000_0000_0000 as short | 0b0000_0000_1000_0000 as short
    }

    @Unroll
    def 'decode a whole short from the buffer - most significant bit last'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap Shorts.toByteArray(intValue)
        def decoder = new ShortDecoderBuilder().bitOrder MOST_SIGNIFICANT_BIT_LAST build()

        when:
        def decoded = decoder.decode buffer

        then:
        decoded == decodedValue

        where:
        intValue                       | decodedValue
        0b0000_0000_0000_0000 as short | 0b0000_0000_0000_0000 as short
        0b0000_0000_0000_0001 as short | 0b0000_0000_1000_0000 as short
        0b0001_0011_0100_0001 as short | 0b1100_1000_1000_0010 as short
        0b0111_1111_1111_1111 as short | 0b1111_1110_1111_1111 as short
        0b1000_0000_0000_0000 as short | 0b0000_0001_0000_0000 as short
    }

    @Unroll
    def 'decode a whole short from the buffer - little endian, most significant bit last'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap Shorts.toByteArray(intValue)
        def decoder = new ShortDecoderBuilder().byteOrder LITTLE_ENDIAN bitOrder MOST_SIGNIFICANT_BIT_LAST build()

        when:
        def decoded = decoder.decode buffer

        then:
        decoded == decodedValue

        where:
        intValue                       | decodedValue
        0b0000_0000_0000_0000 as short | 0b0000_0000_0000_0000 as short
        0b0000_0000_0000_0001 as short | 0b1000_0000_0000_0000 as short
        0b0001_0011_0100_0001 as short | 0b1000_0010_1100_1000 as short
        0b0111_1111_1111_1111 as short | 0b1111_1111_1111_1110 as short
        0b1000_0000_0000_0000 as short | 0b0000_0000_0000_0001 as short
    }

    @Unroll
    def 'decode 4-bit part of a short from the buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap Shorts.toByteArray(intValue)
        def decoder = new ShortDecoderBuilder().valueLengthInBits 4 build()

        when:
        def decoded = decoder.decode buffer

        then:
        decoded == decodedValue

        where:
        intValue                       | decodedValue
        0b0001_0000_0000_0000 as short | 1 as short
        0b0010_0000_0000_0000 as short | 2 as short
        0b0100_0000_0000_0000 as short | 4 as short
        0b1000_0000_0000_0000 as short | -8 as short
        0b0011_0000_0000_0000 as short | 3 as short
        0b0110_0000_0000_0000 as short | 6 as short
        0b1001_0000_0000_0000 as short | -7 as short
    }

    @Unroll
    def 'decode 4-bit part of a short from the buffer and shift the buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap Shorts.toByteArray(intValue)
        def decoder = new ShortDecoderBuilder().valueLengthInBits bitsUsed build()

        when:
        decoder.decode buffer
        def shiftedInt = Shorts.fromByteArray(buffer.array())

        then:
        shiftedInt == shiftedValue

        where:
        intValue                       | bitsUsed | shiftedValue
        0b0001_0001_0001_0000 as short | 1        | 0b0010_0010_0010_0000 as short
        0b0010_0000_0001_0000 as short | 2        | 0b1000_0000_0100_0000 as short
        0b0100_0000_0001_0000 as short | 3        | 0b0000_0000_1000_0000 as short
        0b0011_0000_0000_0000 as short | 5        | 0b0000_0000_0000_0000 as short
        0b0110_0000_0000_0000 as short | 6        | 0b0000_0000_0000_0000 as short
        0b1001_0000_0000_0011 as short | 7        | 0b0000_0001_1000_0000 as short
        0b1001_0000_0000_0000 as short | 8        | 0b0000_0000_0000_0000 as short
        0b1001_0000_0100_0000 as short | 10       | 0b0000_0000_0000_0000 as short
        0b1001_0000_0000_0100 as short | 12       | 0b0100_0000_0000_0000 as short
        0b1001_0000_0000_0000 as short | 16       | 0b0000_0000_0000_0000 as short
    }

    @Unroll
    def 'decode a signed 13-bit part of a short from the buffer and shift the buffer - Little Endian, Most Significant Bit Last'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap Shorts.toByteArray(intValue)
        def decoder = new ShortDecoderBuilder().byteOrder LITTLE_ENDIAN bitOrder MOST_SIGNIFICANT_BIT_LAST valueLengthInBits 13 build()

        when:
        def result = decoder.decode buffer

        then:
        result == resultValue

        where:
        intValue                       | resultValue
        0b0001_0001_0001_0000 as short | 0b0000_1000_1000_1000 as short
        0b0010_0000_0001_0000 as short | 0b0000_1000_0000_0100 as short
        0b0100_0000_0001_0000 as short | 0b0000_1000_0000_0010 as short
        0b0011_0000_0000_0000 as short | 0b0000_0000_0000_1100 as short
        0b0110_0000_0000_0000 as short | 0b0000_0000_0000_0110 as short
        0b1001_0000_0000_0011 as short | 0b0000_0000_0000_1001 as short
        0b1001_0000_0000_0000 as short | 0b0000_0000_0000_1001 as short
        0b1001_0000_0100_0000 as short | 0b0000_0010_0000_1001 as short
        0b1001_0000_0000_1100 as short | 0b1111_0000_0000_1001 as short
        0b1001_0000_1111_1111 as short | 0b1111_1111_0000_1001 as short
    }

    @Unroll
    def 'decode a signed or unsigned 13-bit part of a short from the buffer - Little Endian'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap Shorts.toByteArray(intValue)
        def decoder = new ShortDecoderBuilder().byteOrder LITTLE_ENDIAN valueLengthInBits 13 signed true build()

        when:
        def result = decoder.decode buffer

        then:
        result == resultValue

        where:
        //Cut to 13 bites signed/unsigned, pad to full bytes (16 bits in this case) with leading bits, then reverse byte order
        intValue                       | resultValue                    | signed
        0b1001_0000_0000_0011 as short | 0b0000_0000_1111_0010 as short | true
        0b1001_0000_0011_0000 as short | 0b0000_0110_1111_0010 as short | true
        0b0001_0000_0100_1000 as short | 0b0000_1001_0000_0010 as short | true
        0b1011_0000_0000_1100 as short | 0b0000_0001_1111_0110 as short | true
        0b0101_0000_1111_1111 as short | 0b0001_1111_0000_1010 as short | true
    }
}