package decoders

import com.github.vrbt.biclops.decoders.BooleanDecoder
import com.github.vrbt.biclops.decoders.IntegerDecoder
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.ByteBuffer


/**
 * Created by Robert on 2016-06-12.
 */
class BitStructureTest extends Specification {
    @Unroll
    def 'decode group of bits from buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap byteValue
        def decoder = new BooleanDecoder()
        def decodedBooleans = []

        when:
        8.times { decodedBooleans << decoder.decode(buffer) }

        then:
        decodedBooleans == decodedValues

        where:
        byteValue           | decodedValues
        0b1010_1100 as byte | [true, false, true, false, true, true, false, false]
        0b1100_0011 as byte | [true, true, false, false, false, false, true, true]
        0b1101_0101 as byte | [true, true, false, true, false, true, false, true]
        0b0111_1111 as byte | [false, true, true, true, true, true, true, true]
        0b0100_1001 as byte | [false, true, false, false, true, false, false, true]
        0b0001_1101 as byte | [false, false, false, true, true, true, false, true]
    }

    @Unroll
    def 'decode a bit, a 6-bit int and a bit from buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap byteValue
        def booleanDecoder = new BooleanDecoder()
        def intDecoder = new IntegerDecoder()

        when:
        def firstDecoded = booleanDecoder.decode buffer
        def intDecoded = intDecoder.decode buffer, 6
        def lastDecoded = booleanDecoder.decode buffer

        then:
        firstDecoded == firstBit
        lastDecoded == lastBit
        intDecoded == integerValue


        where:
        byteValue           | firstBit | integerValue | lastBit
        0b1010_1100 as byte | true     | 22           | false
        0b1100_0011 as byte | true     | 33           | true
        0b1101_0101 as byte | true     | 42           | true
        0b0111_1110 as byte | false    | 63           | false
        0b0100_1001 as byte | false    | 36           | true
        0b0001_1100 as byte | false    | 14           | false
    }

    @Unroll
    def 'decode 2-bit int, 5-bit int and bit from buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap byteValue
        def booleanDecoder = new BooleanDecoder()
        def intDecoder = new IntegerDecoder()

        when:
        def firstIntDecoded = intDecoder.decode buffer, 2
        def secondIntDecoded = intDecoder.decode buffer, 5
        def bitDecoded = booleanDecoder.decode buffer

        then:
        firstIntDecoded == firstIntValue
        secondIntDecoded == secondIntValue
        bitDecoded == bitValue


        where:
        byteValue           | firstIntValue | secondIntValue | bitValue
        0b1010_1100 as byte | 2             | 22             | false
        0b1100_0011 as byte | 3             | 1              | true
        0b1101_0101 as byte | 3             | 10             | true
        0b0111_1110 as byte | 1             | 31             | false
        0b0100_1001 as byte | 1             | 4              | true
        0b0001_1100 as byte | 0             | 14             | false
    }
}