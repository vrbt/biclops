package decoders

import com.github.vrbt.biclops.annotations.BigIntegerField
import com.github.vrbt.biclops.decoders.BigIntegerDecoder
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
class BigIntegerTest extends Specification {
    private class BoxedBigIntegerFieldObject {
        @BigIntegerField
        private BigInteger field;
    }

    def 'detect @BigIntegerField annotation on a BigInteger field'() {
        given:
        def flagField = BoxedBigIntegerFieldObject.class.declaredFields.find { field -> StringUtils.equalsIgnoreCase(field.getName(), 'field') }

        when:
        def contains = flagField.annotations.any { Annotation annotation -> StringUtils.containsIgnoreCase(annotation.annotationType().name, 'BigIntegerField') }

        then:
        contains
    }

    @Unroll
    def 'decode a whole BigInteger from the buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap intValue.toByteArray()
        def decoder = new BigIntegerDecoder()

        when:
        def decoded = decoder.decode buffer, intValue.toByteArray().size() * 8

        then:
        decoded == decodedValue

        where:
        intValue                        | decodedValue
        0 as BigInteger                 | 0 as BigInteger
        1 as BigInteger                 | 1 as BigInteger
        Integer.MAX_VALUE as BigInteger | Integer.MAX_VALUE as BigInteger
        Integer.MIN_VALUE as BigInteger | Integer.MIN_VALUE as BigInteger
        Byte.MAX_VALUE as BigInteger    | Byte.MAX_VALUE as BigInteger
        Byte.MIN_VALUE as BigInteger    | Byte.MIN_VALUE as BigInteger
        Short.MAX_VALUE as BigInteger   | Short.MAX_VALUE as BigInteger
        Short.MIN_VALUE as BigInteger   | Short.MIN_VALUE as BigInteger
        Long.MAX_VALUE as BigInteger    | Long.MAX_VALUE as BigInteger
        Long.MIN_VALUE as BigInteger    | Long.MIN_VALUE as BigInteger
    }

    @Unroll
    def 'decode a whole BigInteger from the buffer - little endian'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap intValue
        def decoder = new BigIntegerDecoder()

        when:
        def decoded = decoder.decode buffer, Endianness.LITTLE_ENDIAN

        then:
        decoded == new BigInteger(1, decodedValue)

        where:
        intValue                                                   | decodedValue
        [0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00] as byte[] | [0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00] as byte[]
        [0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01] as byte[] | [0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00] as byte[]
        [0x12, 0x34, 0x56, 0x78, 0x9A, 0xBC, 0xCD, 0xEF] as byte[] | [0xEF, 0xCD, 0xBC, 0x9A, 0x78, 0x56, 0x34, 0x12] as byte[]
        [0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x7F] as byte[] | [0x7F, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00] as byte[]
        [0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x80] as byte[] | [0x80, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00] as byte[]
        [0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x7F, 0xFF] as byte[] | [0xFF, 0x7F, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00] as byte[]
        [0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x80, 0x00] as byte[] | [0x00, 0x80, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00] as byte[]
        [0x00, 0x00, 0x00, 0x00, 0x7F, 0xFF, 0xFF, 0xFF] as byte[] | [0xFF, 0xFF, 0xFF, 0x7F, 0x00, 0x00, 0x00, 0x00] as byte[]
        [0x00, 0x00, 0x00, 0x00, 0x80, 0x00, 0x00, 0x00] as byte[] | [0x00, 0x00, 0x00, 0x80, 0x00, 0x00, 0x00, 0x00] as byte[]
        [0x7F, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF] as byte[] | [0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x7F] as byte[]
        [0x80, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00] as byte[] | [0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x80] as byte[]
    }

    @Unroll
    def 'decode a whole variable length BigInteger from the buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap intValue
        def decoder = new BigIntegerDecoder()

        when:
        def decoded = decoder.decode buffer, bitLength

        then:
        decoded == new BigInteger(decodedValue)

        where:
        intValue                                                                           | decodedValue                                                                       | bitLength
        [0x00] as byte[]                                                                   | [0x00] as byte[]                                                                   | 8
        [0x80] as byte[]                                                                   | [0x80] as byte[]                                                                   | 8
        [0xFF] as byte[]                                                                   | [0xFF] as byte[]                                                                   | 8
        [0x7F] as byte[]                                                                   | [0x7F] as byte[]                                                                   | 8
        [0xFF, 0x01] as byte[]                                                             | [0xFF, 0x01] as byte[]                                                             | 16
        [0xEF, 0x80] as byte[]                                                             | [0xEF, 0x80] as byte[]                                                             | 16
        [0b0001_0010, 0b0010_0010, 0b0011_0011] as byte[]                                  | [0b0000_1001, 0b0001_0001, 0b0001_1001] as byte[]                                  | 23
        [0x00, 0x12, 0x00, 0xF0, 0x0A, 0x00, 0x12, 0x00, 0xEE, 0xAC, 0xDC, 0x07] as byte[] | [0x00, 0x12, 0x00, 0xF0, 0x0A, 0x00, 0x12, 0x00, 0xEE, 0xAC, 0xDC, 0x07] as byte[] | 96

    }

    @Unroll
    def 'decode a 4-bit part of a long from the buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap Longs.toByteArray(intValue)
        def decoder = new BigIntegerDecoder()

        when:
        def decoded = decoder.decode buffer, 4

        then:
        decoded == decodedValue

        where:
        intValue                                                                                  | decodedValue
        0b0001_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long | 1 as long
        0b0010_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long | 2 as long
        0b0100_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long | 4 as long
        0b1000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long | -8 as long
        0b0011_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long | 3 as long
        0b0110_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long | 6 as long
        0b1001_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long | -7 as long
    }

    @Unroll
    def 'decode a 60-bit part of a long from the buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap Longs.toByteArray(intValue)
        def decoder = new BigIntegerDecoder()

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
        def decoder = new BigIntegerDecoder()

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