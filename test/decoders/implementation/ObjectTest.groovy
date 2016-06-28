package decoders.implementation

import com.github.vrbt.biclops.annotations.ByteField
import com.github.vrbt.biclops.annotations.ShortField
import com.github.vrbt.biclops.decoders.implementation.ByteDecoderBuilder
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
    def 'decode object with a single primitive short field'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap byteArray
        def singleDecoder = new ShortDecoderBuilder().build()
        def decoder = new DefaultObjectDecoderBuilder(SingleShortFieldClass.class).addDecoder singleDecoder build()

        when:
        def decodedObject = decoder.decode buffer

        then:
        SingleShortFieldClass.equals(decodedObject.class)
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

    private class SingleBoxedShortFieldClass {
        @ShortField
        public Short field;
    }

    @Unroll
    def 'decode object with a single boxed Short field'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap byteArray
        def singleDecoder = new ShortDecoderBuilder().build()
        def decoder = new DefaultObjectDecoderBuilder(SingleBoxedShortFieldClass.class).addDecoder singleDecoder build()

        when:
        def decodedObject = decoder.decode buffer

        then:
        SingleBoxedShortFieldClass.equals(decodedObject.class)
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

    private class DoubleByteFieldClass {
        @ByteField
        public byte field1;
        @ByteField
        public byte field2;
    }

    @Unroll
    def 'decode object with a two byte fields'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap byteArray
        def builder = new ByteDecoderBuilder()
        def firstDecoder = builder.build()
        def secondDecoder = builder.build()
        def decoder = new DefaultObjectDecoderBuilder(DoubleByteFieldClass.class).addDecoder firstDecoder addDecoder secondDecoder build()

        when:
        def decodedObject = decoder.decode buffer

        then:
        DoubleByteFieldClass.equals(decodedObject.class)
        decodedObject.field1 == field1Value
        decodedObject.field2 == field2Value

        where:
        byteArray              | field1Value  | field2Value
        [0x01, 0x23] as byte[] | 0x01 as byte | 0x23 as byte
        [0x00, 0x12] as byte[] | 0x00 as byte | 0x12 as byte
        [0x76, 0x02] as byte[] | 0x76 as byte | 0x02 as byte
        [0x72, 0xAF] as byte[] | 0x72 as byte | 0xAF as byte
        [0x98, 0xDE] as byte[] | 0x98 as byte | 0xDE as byte
        [0x4F, 0x93] as byte[] | 0x4F as byte | 0x93 as byte
    }

    private class MixedBoxedAndPrimitiveDoubleByteFieldClass {
        @ByteField
        public byte field1;
        @ByteField
        public Byte field2;
    }

    @Unroll
    def 'decode object with a primitive byte and boxed Byte fields'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap byteArray
        def builder = new ByteDecoderBuilder()
        def firstDecoder = builder.build()
        def secondDecoder = builder.build()
        def decoder = new DefaultObjectDecoderBuilder(MixedBoxedAndPrimitiveDoubleByteFieldClass.class).addDecoder firstDecoder addDecoder secondDecoder build()

        when:
        def decodedObject = decoder.decode buffer

        then:
        MixedBoxedAndPrimitiveDoubleByteFieldClass.equals(decodedObject.class)
        decodedObject.field1 == field1Value
        decodedObject.field2 == field2Value

        where:
        byteArray              | field1Value  | field2Value
        [0x01, 0x23] as byte[] | 0x01 as byte | 0x23 as byte
        [0x00, 0x12] as byte[] | 0x00 as byte | 0x12 as byte
        [0x76, 0x02] as byte[] | 0x76 as byte | 0x02 as byte
        [0x72, 0xAF] as byte[] | 0x72 as byte | 0xAF as byte
        [0x98, 0xDE] as byte[] | 0x98 as byte | 0xDE as byte
        [0x4F, 0x93] as byte[] | 0x4F as byte | 0x93 as byte
    }
}