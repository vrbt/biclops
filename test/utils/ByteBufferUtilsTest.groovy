package utils

import com.github.vrbt.biclops.utils.ByteBufferUtils
import com.google.common.primitives.Ints
import com.google.common.primitives.Longs
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.ByteBuffer

/**
 * Created by Robert on 2016-06-11.
 */
class ByteBufferUtilsTest extends Specification {

    @Unroll
    def 'cyclic left shift of integers'() {
        given:
        def buffer = ByteBuffer.wrap Ints.toByteArray(originalValue)

        when:
        def shiftedBuffer = ByteBufferUtils.leftShift buffer, shift
        def result = Ints.fromByteArray shiftedBuffer.array()

        then:
        result == shiftedValue

        where:
        originalValue                             | shift | shiftedValue
        //if not for cast, the predefined value would be interpreted as long (longer than int) which would make it positive, when result is negative
        0b0000_0000_0000_0000_0000_0000_0000_0000 | 0     | 0b0000_0000_0000_0000_0000_0000_0000_0000 as int
        0b0000_0000_0000_0000_0000_0000_0000_0000 | 2     | 0b0000_0000_0000_0000_0000_0000_0000_0000 as int
        0b0000_0000_0000_0000_0000_0000_0000_0000 | 4     | 0b0000_0000_0000_0000_0000_0000_0000_0000 as int
        0b0000_0000_0000_0000_0000_0000_0000_0000 | 8     | 0b0000_0000_0000_0000_0000_0000_0000_0000 as int
        0b0000_0000_0000_0000_0000_0000_0000_0000 | 16    | 0b0000_0000_0000_0000_0000_0000_0000_0000 as int
        0b0000_0000_0000_0000_0000_0000_0000_0000 | 32    | 0b0000_0000_0000_0000_0000_0000_0000_0000 as int
        0b0000_0000_0000_0000_0000_0000_0000_0001 | 0     | 0b0000_0000_0000_0000_0000_0000_0000_0001 as int
        0b0000_0000_0000_0000_0000_0000_0000_0001 | 1     | 0b0000_0000_0000_0000_0000_0000_0000_0010 as int
        0b0000_0000_0000_0000_0000_0000_0000_0001 | 4     | 0b0000_0000_0000_0000_0000_0000_0001_0000 as int
        0b0000_0000_0000_0000_0000_0000_0000_0001 | 8     | 0b0000_0000_0000_0000_0000_0001_0000_0000 as int
        0b0000_0000_0000_0000_0000_0000_0000_0001 | 16    | 0b0000_0000_0000_0001_0000_0000_0000_0000 as int
        0b0000_0000_0000_0000_0000_0000_0000_0001 | 31    | 0b1000_0000_0000_0000_0000_0000_0000_0000 as int
        0b0000_0000_0000_0000_0000_0000_0000_0001 | 32    | 0b0000_0000_0000_0000_0000_0000_0000_0000 as int
    }

    @Unroll
    def 'cyclic left shift of longs'() {
        given:
        def buffer = ByteBuffer.wrap Longs.toByteArray(originalValue)

        when:
        def shiftedBuffer = ByteBufferUtils.leftShift(buffer, shift)
        def result = Longs.fromByteArray shiftedBuffer.array()

        then:
        result == (long) shiftedValue

        where:
        originalValue                                                                     | shift | shiftedValue
        //if not for cast, the predefined value would be interpreted as BigInt (longer than long) which would make it positive, when result is negative, dynamic typing strikes again
        0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 | 0     | 0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 | 2     | 0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 | 4     | 0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 | 8     | 0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 | 16    | 0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 | 32    | 0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 | 64    | 0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0001 | 0     | 0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0001 as long
        0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0001 | 1     | 0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0010 as long
        0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0001 | 4     | 0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0001_0000 as long
        0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0001 | 8     | 0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0001_0000_0000 as long
        0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0001 | 16    | 0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0001_0000_0000_0000_0000 as long
        0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0001 | 32    | 0b0000_0000_0000_0000_0000_0000_0000_0001_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0001 | 60    | 0b0001_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0001 | 63    | 0b1000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
        0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0001 | 64    | 0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000 as long
    }

    @Unroll
    def 'reverse bits order of a byte'() {
        given:
        def source = byteValue

        when:
        def result = ByteBufferUtils.reverseBits source

        then:
        result == reversedByte

        where:
        byteValue           | reversedByte
        0b0000_0000 as byte | 0b0000_0000 as byte
        0b0000_0001 as byte | 0b1000_0000 as byte
        0b0000_0010 as byte | 0b0100_0000 as byte
        0b0000_0011 as byte | 0b1100_0000 as byte
        0b0001_0000 as byte | 0b0000_1000 as byte
        0b0100_0001 as byte | 0b1000_0010 as byte
    }

    @Unroll
    def 'reverse bytes order of a byte array'() {
        given:
        def source = bytesValues

        when:
        def result = ByteBufferUtils.reverseBytes source

        then:
        result == reversedBytes

        where:
        bytesValues                                                               | reversedBytes
        [0b0000_0000 as Byte, 0b0000_0000 as Byte, 0b0000_0000 as Byte] as Byte[] | [0b0000_0000 as Byte, 0b0000_0000 as Byte, 0b0000_0000 as Byte]
        [0b0000_0000 as Byte, 0b0001_0000 as Byte, 0b0000_0000 as Byte] as Byte[] | [0b0000_0000 as Byte, 0b0001_0000 as Byte, 0b0000_0000 as Byte]
        [0b0000_0000 as Byte, 0b1100_0000 as Byte, 0b0000_0000 as Byte] as Byte[] | [0b0000_0000 as Byte, 0b1100_0000 as Byte, 0b0000_0000 as Byte]
        [0b0000_0000 as Byte, 0b0000_0000 as Byte, 0b0000_0001 as Byte] as Byte[] | [0b0000_0001 as Byte, 0b0000_0000 as Byte, 0b0000_0000 as Byte]
        [0b1100_0000 as Byte, 0b0000_1001 as Byte, 0b0000_0100 as Byte] as Byte[] | [0b0000_0100 as Byte, 0b0000_1001 as Byte, 0b1100_0000 as Byte]
    }

    @Unroll
    def 'reverse just bits order of bytes in a byte array'() {
        given:
        def source = bytesValues

        when:
        def result = ByteBufferUtils.reverseBits source

        then:
        result == reversedBytes

        where:
        bytesValues                                                               | reversedBytes
        [0b0000_0000 as Byte, 0b0000_0000 as Byte, 0b0000_0000 as Byte] as Byte[] | [0b0000_0000 as Byte, 0b0000_0000 as Byte, 0b0000_0000 as Byte]
        [0b0000_0000 as Byte, 0b0001_0000 as Byte, 0b0000_0000 as Byte] as Byte[] | [0b0000_0000 as Byte, 0b0000_1000 as Byte, 0b0000_0000 as Byte]
        [0b0110_0001 as Byte, 0b1100_0000 as Byte, 0b0000_1010 as Byte] as Byte[] | [0b1000_0110 as Byte, 0b0000_0011 as Byte, 0b0101_0000 as Byte]
        [0b0000_0010 as Byte, 0b0000_0111 as Byte, 0b0000_0001 as Byte] as Byte[] | [0b0100_0000 as Byte, 0b1110_0000 as Byte, 0b1000_0000 as Byte]
        [0b1100_0010 as Byte, 0b0011_1001 as Byte, 0b1000_0100 as Byte] as Byte[] | [0b0100_0011 as Byte, 0b1001_1100 as Byte, 0b0010_0001 as Byte]
    }

    @Unroll
    def 'fullReverse bits order of a byte array'() {
        given:
        def source = bytesValues

        when:
        def result = ByteBufferUtils.fullReverse source

        then:
        result == reversedBytes

        where:
        bytesValues                                                               | reversedBytes
        [0b0000_0000 as Byte, 0b0000_0000 as Byte, 0b0000_0000 as Byte] as Byte[] | [0b0000_0000 as Byte, 0b0000_0000 as Byte, 0b0000_0000 as Byte]
        [0b0000_0000 as Byte, 0b0001_0000 as Byte, 0b0000_0000 as Byte] as Byte[] | [0b0000_0000 as Byte, 0b0000_1000 as Byte, 0b0000_0000 as Byte]
        [0b0000_0000 as Byte, 0b1100_0000 as Byte, 0b0000_0000 as Byte] as Byte[] | [0b0000_0000 as Byte, 0b0000_0011 as Byte, 0b0000_0000 as Byte]
        [0b0000_0000 as Byte, 0b0000_0000 as Byte, 0b0000_0001 as Byte] as Byte[] | [0b1000_0000 as Byte, 0b0000_0000 as Byte, 0b0000_0000 as Byte]
        [0b1100_0000 as Byte, 0b0000_1001 as Byte, 0b0000_0100 as Byte] as Byte[] | [0b0010_0000 as Byte, 0b1001_0000 as Byte, 0b0000_0011 as Byte]
    }

    @Unroll
    def 'reverse byte order of a signed bigInteger'() {
        given:
        def bigInt = new BigInteger(bytesValues)

        when:
        def result = ByteBufferUtils.reverseBytes bigInt, bytesValues.size() * 8

        then:
        result == new BigInteger(reversedBytes)

        where:
        bytesValues                                                               | reversedBytes
        [0b0000_0000 as byte, 0b0000_0000 as byte, 0b0000_0000 as byte] as byte[] | [0b0000_0000 as byte, 0b0000_0000 as byte, 0b0000_0000 as byte] as byte[]
        [0b0000_0000 as byte, 0b0001_0000 as byte, 0b0000_0000 as byte] as byte[] | [0b0000_0000 as byte, 0b0001_0000 as byte, 0b0000_0000 as byte] as byte[]
        [0b0000_0000 as byte, 0b1100_0000 as byte, 0b0000_0000 as byte] as byte[] | [0b0000_0000 as byte, 0b1100_0000 as byte, 0b0000_0000 as byte] as byte[]
        [0b0000_0000 as byte, 0b0000_0000 as byte, 0b0000_0001 as byte] as byte[] | [0b0000_0001 as byte, 0b0000_0000 as byte, 0b0000_0000 as byte] as byte[]
        [0b1100_0000 as byte, 0b0000_1001 as byte, 0b0000_0100 as byte] as byte[] | [0b0000_0100 as byte, 0b0000_1001 as byte, 0b1100_0000 as byte] as byte[]
    }

    @Unroll
    def 'reverse byte order of an unsigned bigInteger'() {
        given:
        def bigInt = new BigInteger(bytesValues)

        when:
        def result = ByteBufferUtils.reverseBytes bigInt, bytesValues.size() * 8, false

        then:
        result == new BigInteger(1, reversedBytes)

        where:
        bytesValues                                                               | reversedBytes
        [0b0000_0000 as byte, 0b0000_0000 as byte, 0b0000_0000 as byte] as byte[] | [0b0000_0000 as byte, 0b0000_0000 as byte, 0b0000_0000 as byte] as byte[]
        [0b0000_0000 as byte, 0b0001_0000 as byte, 0b0000_0000 as byte] as byte[] | [0b0000_0000 as byte, 0b0001_0000 as byte, 0b0000_0000 as byte] as byte[]
        [0b0000_0000 as byte, 0b1100_0000 as byte, 0b0000_0000 as byte] as byte[] | [0b0000_0000 as byte, 0b1100_0000 as byte, 0b0000_0000 as byte] as byte[]
        [0b0000_0000 as byte, 0b0000_0000 as byte, 0b0000_0001 as byte] as byte[] | [0b0000_0001 as byte, 0b0000_0000 as byte, 0b0000_0000 as byte] as byte[]
        [0b1100_0000 as byte, 0b0000_1001 as byte, 0b0000_0100 as byte] as byte[] | [0b0000_0100 as byte, 0b0000_1001 as byte, 0b1100_0000 as byte] as byte[]
    }

    @Unroll
    def 'reverse bits order of bytes in a signed bigInteger'() {
        given:
        def bigInt = new BigInteger(bytesValues)

        when:
        def result = ByteBufferUtils.reverseBits bigInt, bytesValues.size() * 8

        then:
        result == new BigInteger(reversedBytes)

        where:
        bytesValues                                                               | reversedBytes
        [0b0000_0000 as byte, 0b0000_0000 as byte, 0b0000_0000 as byte] as byte[] | [0b0000_0000 as byte, 0b0000_0000 as byte, 0b0000_0000 as byte] as byte[]
        [0b0000_0000 as byte, 0b0001_0000 as byte, 0b0000_0000 as byte] as byte[] | [0b0000_0000 as byte, 0b0000_1000 as byte, 0b0000_0000 as byte] as byte[]
        [0b0000_0000 as byte, 0b1100_0000 as byte, 0b0000_0000 as byte] as byte[] | [0b0000_0000 as byte, 0b0000_0011 as byte, 0b0000_0000 as byte] as byte[]
        [0b0000_0000 as byte, 0b0000_0000 as byte, 0b0000_0001 as byte] as byte[] | [0b0000_0000 as byte, 0b0000_0000 as byte, 0b1000_0000 as byte] as byte[]
        [0b1100_0000 as byte, 0b0000_1001 as byte, 0b0000_0100 as byte] as byte[] | [0b0000_0011 as byte, 0b1001_0000 as byte, 0b0010_0000 as byte] as byte[]
    }

    @Unroll
    def 'reverse bits order of bytes in an unsigned bigInteger'() {
        given:
        def bigInt = new BigInteger(bytesValues)

        when:
        def result = ByteBufferUtils.reverseBits bigInt, bytesValues.size() * 8, false

        then:
        result == new BigInteger(1, reversedBytes)

        where:
        bytesValues                                                               | reversedBytes
        [0b0000_0000 as byte, 0b0000_0000 as byte, 0b0000_0000 as byte] as byte[] | [0b0000_0000 as byte, 0b0000_0000 as byte, 0b0000_0000 as byte] as byte[]
        [0b0000_0000 as byte, 0b0001_0000 as byte, 0b0000_0000 as byte] as byte[] | [0b0000_0000 as byte, 0b0000_1000 as byte, 0b0000_0000 as byte] as byte[]
        [0b0000_0000 as byte, 0b1100_0000 as byte, 0b0000_0000 as byte] as byte[] | [0b0000_0000 as byte, 0b0000_0011 as byte, 0b0000_0000 as byte] as byte[]
        [0b0000_0000 as byte, 0b0000_0000 as byte, 0b0000_0001 as byte] as byte[] | [0b0000_0000 as byte, 0b0000_0000 as byte, 0b1000_0000 as byte] as byte[]
        [0b1100_0000 as byte, 0b0000_1001 as byte, 0b0000_0100 as byte] as byte[] | [0b0000_0011 as byte, 0b1001_0000 as byte, 0b0010_0000 as byte] as byte[]
    }

    @Unroll
    def 'fullReverse bits order of a signed bigInteger'() {
        given:
        def bigInt = new BigInteger(bytesValues)

        when:
        def result = ByteBufferUtils.fullReverse bigInt, bytesValues.size() * 8

        then:
        result == new BigInteger(reversedBytes)

        where:
        bytesValues                                                               | reversedBytes
        [0b0000_0000 as byte, 0b0000_0000 as byte, 0b0000_0000 as byte] as byte[] | [0b0000_0000 as byte, 0b0000_0000 as byte, 0b0000_0000 as byte] as byte[]
        [0b0000_0000 as byte, 0b0001_0000 as byte, 0b0000_0000 as byte] as byte[] | [0b0000_0000 as byte, 0b0000_1000 as byte, 0b0000_0000 as byte] as byte[]
        [0b0000_0000 as byte, 0b1100_0000 as byte, 0b0000_0000 as byte] as byte[] | [0b0000_0000 as byte, 0b0000_0011 as byte, 0b0000_0000 as byte] as byte[]
        [0b0000_0000 as byte, 0b0000_0000 as byte, 0b0000_0001 as byte] as byte[] | [0b1000_0000 as byte, 0b0000_0000 as byte, 0b0000_0000 as byte] as byte[]
        [0b1100_0000 as byte, 0b0000_1001 as byte, 0b0000_0100 as byte] as byte[] | [0b0010_0000 as byte, 0b1001_0000 as byte, 0b0000_0011 as byte] as byte[]
    }

    @Unroll
    def 'fullReverse bits order of an unsigned bigInteger'() {
        given:
        def bigInt = new BigInteger(bytesValues)

        when:
        def result = ByteBufferUtils.fullReverse bigInt, bytesValues.size() * 8, false

        then:
        result == new BigInteger(1, reversedBytes)

        where:
        bytesValues                                                               | reversedBytes
        [0b0000_0000 as byte, 0b0000_0000 as byte, 0b0000_0000 as byte] as byte[] | [0b0000_0000 as byte, 0b0000_0000 as byte, 0b0000_0000 as byte] as byte[]
        [0b0000_0000 as byte, 0b0001_0000 as byte, 0b0000_0000 as byte] as byte[] | [0b0000_0000 as byte, 0b0000_1000 as byte, 0b0000_0000 as byte] as byte[]
        [0b0000_0000 as byte, 0b1100_0000 as byte, 0b0000_0000 as byte] as byte[] | [0b0000_0000 as byte, 0b0000_0011 as byte, 0b0000_0000 as byte] as byte[]
        [0b0000_0000 as byte, 0b0000_0000 as byte, 0b0000_0001 as byte] as byte[] | [0b1000_0000 as byte, 0b0000_0000 as byte, 0b0000_0000 as byte] as byte[]
        [0b1100_0000 as byte, 0b0000_1001 as byte, 0b0000_0100 as byte] as byte[] | [0b0010_0000 as byte, 0b1001_0000 as byte, 0b0000_0011 as byte] as byte[]
    }
}