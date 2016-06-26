package decoders.implementation

import com.github.vrbt.biclops.annotations.ShortField
import com.github.vrbt.biclops.decoders.implementation.DefaultObjectDecoderBuilder
import com.github.vrbt.biclops.decoders.implementation.ShortDecoderBuilder
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.ByteBuffer

/**
 * Created by Robert on 2016-06-26.
 */
class ObjectTest extends Specification {

    private class SingleShortFieldClass {
        @ShortField
        public short field;
    }

    @Unroll
    def 'decode object with a single field'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap byteArray
        def singleDecoder = new ShortDecoderBuilder().build()
        def decoder = new DefaultObjectDecoderBuilder(SingleShortFieldClass.class).addDecoder singleDecoder build()

        when:
        SingleShortFieldClass decodedObject = decoder.decode buffer

        then:
        decodedObject.field == fieldValue

        where:
        byteArray              | fieldValue
        [0x01, 0x23] as byte[] | 291
        [0x00, 0x12] as byte[] | 18
        [0x76, 0x02] as byte[] | 30210
        [0x72, 0xAF] as byte[] | 29359
        [0x98, 0xDE] as byte[] | -26402
        [0x4F, 0x93] as byte[] | 20371
    }

}