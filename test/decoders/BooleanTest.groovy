package decoders

import com.github.vrbt.biclops.annotations.Bit
import com.github.vrbt.biclops.decoders.BooleanDecoder
import org.apache.commons.lang3.StringUtils
import spock.lang.Specification
import spock.lang.Unroll

import java.lang.annotation.Annotation
import java.nio.ByteBuffer

/**
 * Created by Robert on 2016-06-11.
 */
class BooleanTest extends Specification {

    private class BoxedBooleanObject {
        @Bit
        private Boolean flag;
    }

    def 'detect @Bit annotation on Boolean field'() {
        given:
        def flagField = BoxedBooleanObject.class.declaredFields.find { field -> StringUtils.equalsIgnoreCase(field.getName(), 'flag') }

        when:
        def contains = flagField.annotations.any { Annotation annotation -> StringUtils.containsIgnoreCase(annotation.annotationType().name, 'Bit') }

        then:
        contains
    }

    private class BoxedBooleanPrimitive {
        @Bit
        private boolean flag;
    }

    def 'detect @Bit annotation on boolean field'() {
        given:
        def flagField = BoxedBooleanObject.class.declaredFields.find { field -> StringUtils.equalsIgnoreCase(field.getName(), 'flag') }

        when:
        def contains = flagField.annotations.any { Annotation annotation -> StringUtils.containsIgnoreCase(annotation.annotationType().name, 'Bit') }

        then:
        contains
    }

    @Unroll
    def 'decode single bit from buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap(byteValue)
        def decoder = new BooleanDecoder()

        when:
        def decoded = decoder.decode(buffer)

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
        def ByteBuffer buffer = ByteBuffer.wrap(byteValue)
        def decoder = new BooleanDecoder()

        when:
        def decoded = decoder.decode(buffer)
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
}