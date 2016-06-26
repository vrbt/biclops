package decoders.implementation

import com.github.vrbt.biclops.annotations.BitField
import com.github.vrbt.biclops.decoders.implementation.BooleanDecoderBuilder
import org.apache.commons.lang3.StringUtils
import spock.lang.Specification
import spock.lang.Unroll

import java.lang.annotation.Annotation
import java.nio.ByteBuffer

import static com.github.vrbt.biclops.ordering.BitOrder.MOST_SIGNIFICANT_BIT_LAST
/**
 * Created by Robert on 2016-06-11.
 */
class BooleanTest extends Specification {

    private class BoxedBooleanObject {
        @BitField
        private Boolean flag;
    }

    def 'detect @BitField annotation on Boolean field'() {
        given:
        def flagField = BoxedBooleanObject.class.declaredFields.find { field -> StringUtils.equalsIgnoreCase field.getName(), 'flag' }

        when:
        def contains = flagField.annotations.any { Annotation annotation -> StringUtils.containsIgnoreCase annotation.annotationType().name, 'BitField' }

        then:
        contains
    }

    private class BoxedBooleanPrimitive {
        @BitField
        private boolean flag;
    }

    def 'detect @BitField annotation on boolean field'() {
        given:
        def flagField = BoxedBooleanObject.class.declaredFields.find { field -> StringUtils.equalsIgnoreCase field.getName(), 'flag' }

        when:
        def contains = flagField.annotations.any { Annotation annotation -> StringUtils.containsIgnoreCase annotation.annotationType().name, 'BitField' }

        then:
        contains
    }

    @Unroll
    def 'decode single bit from buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap byteValue
        def decoder = new BooleanDecoderBuilder().build()

        when:
        def decoded = decoder.decode buffer

        then:
        decoded == decodedValue

        where:
        byteValue           | decodedValue
        -127 as byte        | true
        0b1000_0000 as byte | true
        0b1100_0001 as byte | true
        0b0111_1111 as byte | false
        0b0000_0000 as byte | false
        0b0000_0001 as byte | false
    }

    @Unroll
    def 'decode single bit from buffer and shift buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap byteValue
        def decoder = new BooleanDecoderBuilder().build()

        when:
        def decoded = decoder.decode buffer
        def shiftedByte = buffer.array()[0]

        then:
        decoded == decodedValue
        shiftedByte == shiftedByteValue

        where:
        byteValue           | decodedValue | shiftedByteValue
        0b1000_0000 as byte | true         | 0b0000_0000 as byte
        0b1100_0000 as byte | true         | 0b1000_0000 as byte
        0b1000_0001 as byte | true         | 0b0000_0010 as byte
        0b0000_0000 as byte | false        | 0b0000_0000 as byte
        0b0100_0000 as byte | false        | 0b1000_0000 as byte
        0b0010_0000 as byte | false        | 0b0100_0000 as byte
    }

    @Unroll
    def 'decode single bit from buffer and shift buffer by 2 bits'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap byteValue
        def decoder = new BooleanDecoderBuilder().valueLengthInBits 2 build()

        when:
        def decoded = decoder.decode buffer
        def shiftedByte = buffer.array()[0]

        then:
        decoded == decodedValue
        shiftedByte == shiftedByteValue

        where:
        byteValue           | decodedValue | shiftedByteValue
        0b1000_0000 as byte | true         | 0b0000_0000 as byte
        0b1110_0000 as byte | true         | 0b1000_0000 as byte
        0b1000_0001 as byte | true         | 0b0000_0100 as byte
        0b0000_0000 as byte | false        | 0b0000_0000 as byte
        0b0010_0000 as byte | false        | 0b1000_0000 as byte
        0b0001_0000 as byte | false        | 0b0100_0000 as byte
    }

    @Unroll
    def 'decode single bit from buffer and shift buffer by 4 bits'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap byteValue
        def decoder = new BooleanDecoderBuilder().valueLengthInBits 4 build()

        when:
        def decoded = decoder.decode buffer
        def shiftedByte = buffer.array()[0]

        then:
        decoded == decodedValue
        shiftedByte == shiftedByteValue

        where:
        byteValue           | decodedValue | shiftedByteValue
        0b1000_0000 as byte | true         | 0b0000_0000 as byte
        0b1110_1000 as byte | true         | 0b1000_0000 as byte
        0b1000_0001 as byte | true         | 0b0001_0000 as byte
        0b0000_0000 as byte | false        | 0b0000_0000 as byte
        0b0000_1000 as byte | false        | 0b1000_0000 as byte
        0b0001_0000 as byte | false        | 0b0000_0000 as byte
    }

    @Unroll
    def 'decode single bit from buffer and shift buffer by whole byte'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap byteValue
        def decoder = new BooleanDecoderBuilder().valueLengthInBits 8 build()

        when:
        def decoded = decoder.decode buffer
        def shiftedByte = buffer.array()[0]

        then:
        decoded == decodedValue
        shiftedByte == shiftedByteValue

        where:
        byteValue           | decodedValue | shiftedByteValue
        0b1000_0000 as byte | true         | 0b0000_0000 as byte
        0b1110_1000 as byte | true         | 0b0000_0000 as byte
        0b1000_0001 as byte | true         | 0b0000_0000 as byte
        0b0000_0000 as byte | false        | 0b0000_0000 as byte
        0b0000_1000 as byte | false        | 0b0000_0000 as byte
        0b0001_0000 as byte | false        | 0b0000_0000 as byte
    }

    @Unroll
    def 'decode single bit from buffer with MSB last'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap byteValue
        def decoder = new BooleanDecoderBuilder().bitOrder MOST_SIGNIFICANT_BIT_LAST build()

        when:
        def decoded = decoder.decode buffer

        then:
        decoded == decodedValue

        where:
        byteValue           | decodedValue
        0b0001_0001 as byte | true
        0b1000_0001 as byte | true
        0b1100_0001 as byte | true
        0b1111_1110 as byte | false
        0b0000_0000 as byte | false
        0b1000_0000 as byte | false
    }

    @Unroll
    def 'decode single bit from buffer with MSB last and shift buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap byteValue
        def decoder = new BooleanDecoderBuilder().bitOrder MOST_SIGNIFICANT_BIT_LAST build()

        when:
        def decoded = decoder.decode buffer
        def shiftedByte = buffer.array()[0]

        then:
        decoded == decodedValue
        shiftedByte == shiftedByteValue

        where:
        byteValue           | decodedValue | shiftedByteValue
        0b0001_0001 as byte | true         | 0 as byte
        0b1000_0001 as byte | true         | 0 as byte
        0b1100_0001 as byte | true         | 0 as byte
        0b1111_1110 as byte | false        | 0 as byte
        0b0000_0000 as byte | false        | 0 as byte
        0b1000_0000 as byte | false        | 0 as byte

    }
}