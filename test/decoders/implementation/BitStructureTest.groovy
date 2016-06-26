package decoders.implementation

import com.github.vrbt.biclops.decoders.implementation.BooleanDecoderBuilder
import com.github.vrbt.biclops.decoders.implementation.IntegerDecoderBuilder
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
        def decoder = new BooleanDecoderBuilder().build()
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
        def booleanDecoder = new BooleanDecoderBuilder().build()
        def intDecoder = new IntegerDecoderBuilder().valueLengthInBits 6 build()

        when:
        def firstDecoded = booleanDecoder.decode buffer
        def intDecoded = intDecoder.decode buffer
        def lastDecoded = booleanDecoder.decode buffer

        then:
        firstDecoded == firstBit
        lastDecoded == lastBit
        intDecoded == integerValue


        where:
        byteValue           | firstBit | integerValue | lastBit
        0b1010_1100 as byte | true     | 22           | false
        0b1100_0011 as byte | true     | -31          | true
        0b1101_0101 as byte | true     | -22          | true
        0b0111_1110 as byte | false    | -1           | false
        0b0100_1001 as byte | false    | -28          | true
        0b0001_1100 as byte | false    | 14           | false
    }

    @Unroll
    def 'decode 2-bit int, 5-bit int and bit from buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap byteValue
        def booleanDecoder = new BooleanDecoderBuilder().build()
        def intDecoder1 = new IntegerDecoderBuilder().valueLengthInBits 2 build()
        def intDecoder2 = new IntegerDecoderBuilder().valueLengthInBits 5 build()

        when:
        def firstIntDecoded = intDecoder1.decode buffer
        def secondIntDecoded = intDecoder2.decode buffer
        def bitDecoded = booleanDecoder.decode buffer

        then:
        firstIntDecoded == firstIntValue
        secondIntDecoded == secondIntValue
        bitDecoded == bitValue


        where:
        byteValue           | firstIntValue | secondIntValue | bitValue
        0b1010_1100 as byte | -2            | -10            | false
        0b1100_0011 as byte | -1            | 1              | true
        0b1101_0101 as byte | -1            | 10             | true
        0b0111_1110 as byte | 1             | -1             | false
        0b0100_1001 as byte | 1             | 4              | true
        0b0001_1100 as byte | 0             | 14             | false
    }
}