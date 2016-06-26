package decoders.implementation

import com.github.vrbt.biclops.decoders.implementation.ListDecoderBuilder
import com.github.vrbt.biclops.decoders.implementation.ShortDecoderBuilder
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.ByteBuffer

/**
 * Created by Robert on 2016-06-26.
 */
class ListTest extends Specification {
    @Unroll
    def 'decode list of variable number of shorts from the buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap byteArray
        def singleDecoder = new ShortDecoderBuilder().build()
        def decoder = new ListDecoderBuilder(singleDecoder).size length build()

        when:
        def list = decoder.decode buffer

        then:
        list == decodedValues

        where:
        byteArray                                                                          | length | decodedValues
        [0x01, 0x23, 0x45, 0x67, 0x89, 0xAB, 0xCD, 0xEF, 0xFE, 0xDC, 0xBA, 0x98] as byte[] | 6      | [291, 17767, -30293, -12817, -292, -17768]
        [0x00, 0x12, 0x3A, 0xF0, 0x0A, 0xFE, 0x12, 0x02, 0xEE, 0xAC, 0xDC, 0x07] as byte[] | 6      | [18, 15088, 2814, 4610, -4436, -9209]
        [0x76, 0x02, 0x0C, 0x14, 0xB1, 0xB0, 0x00, 0x7A] as byte[]                         | 4      | [30210, 3092, -20048, 122]
        [0x72, 0xAF, 0xFF, 0xFF, 0xA1, 0x13] as byte[]                                     | 3      | [29359, -1, -24301]
        [0x98, 0xDE, 0xAF, 0x85] as byte[]                                                 | 2      | [-26402, -20603]
        [0x4F, 0x93] as byte[]                                                             | 1      | [20371]
    }
}