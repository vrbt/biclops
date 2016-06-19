package decoders

import com.github.vrbt.biclops.annotations.CharField
import com.github.vrbt.biclops.decoders.CharDecoder
import com.github.vrbt.biclops.ordering.BitOrder
import com.github.vrbt.biclops.ordering.Endianness
import com.google.common.primitives.Chars
import org.apache.commons.lang3.StringUtils
import spock.lang.Specification
import spock.lang.Unroll

import java.lang.annotation.Annotation
import java.nio.ByteBuffer

/**
 * Created by Robert on 2016-06-19.
 */
class CharTest extends Specification {
    private class BoxedCharacterObject {
        @CharField
        private Character field;
    }

    def 'detect @CharField annotation on a Character field'() {
        given:
        def flagField = BoxedCharacterObject.class.declaredFields.find { field -> StringUtils.equalsIgnoreCase(field.getName(), 'field') }

        when:
        def contains = flagField.annotations.any { Annotation annotation -> StringUtils.containsIgnoreCase(annotation.annotationType().name, 'CharField') }

        then:
        contains
    }

    private class BoxedCharPrimitive {
        @CharField
        private char field;
    }

    def 'detect @CharField annotation on a char field'() {
        given:
        def flagField = BoxedCharPrimitive.class.declaredFields.find { field -> StringUtils.equalsIgnoreCase(field.getName(), 'field') }

        when:
        def contains = flagField.annotations.any { Annotation annotation -> StringUtils.containsIgnoreCase(annotation.annotationType().name, 'CharField') }

        then:
        contains
    }

    @Unroll
    def 'decode a whole char from the buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap Chars.toByteArray(charValue)
        def decoder = new CharDecoder()

        when:
        def decoded = decoder.decode buffer

        then:
        decoded == decodedValue

        where:
        charValue      | decodedValue
        'a' as char    | 'a'
        'b' as char    | 'b'
        '0' as char    | '0'
        'Ą' as char    | 'Ą'
        '$' as char    | '$'
        0x00BE as char | '¾'
        0x00F0 as char | 'ð'
        0x12c5 as char | 'ዅ'
        0x13e5 as char | 'Ꮵ'
        0x142b as char | 'ᐫ'
        0x179e as char | 'ឞ'
    }

    @Unroll
    def 'decode a whole char from the buffer - little endian'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap Chars.toByteArray(charValue)
        def decoder = new CharDecoder()

        when:
        def decoded = decoder.decode buffer, Endianness.LITTLE_ENDIAN

        then:
        decoded == decodedValue

        where:
        charValue      | decodedValue
        0x6100 as char | 'a'
        0x6200 as char | 'b'
        0x3000 as char | '0'
        0x0401 as char | 'Ą'
        0xDE00 as char | 'Þ'
        0xBE00 as char | '¾'
        0xF000 as char | 'ð'
        0xc512 as char | 'ዅ'
        0xe513 as char | 'Ꮵ'
        0x2b14 as char | 'ᐫ'
        0x9e17 as char | 'ឞ'
    }

    @Unroll
    def 'decode a whole char from the buffer - most significant bit last'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap Chars.toByteArray(charValue)
        def decoder = new CharDecoder()

        when:
        def decoded = decoder.decode buffer, BitOrder.MOST_SIGNIFICANT_BIT_LAST

        then:
        decoded == decodedValue

        where:
        charValue      | decodedValue
        0x0086 as char | 'a'
        0x0046 as char | 'b'
        0x000C as char | '0'
        0x8020 as char | 'Ą'
        0x007B as char | 'Þ'
        0x007D as char | '¾'
        0x000F as char | 'ð'
        0x48A3 as char | 'ዅ'
        0xC8A7 as char | 'Ꮵ'
        0x28D4 as char | 'ᐫ'
        0xE879 as char | 'ឞ'
    }

    @Unroll
    def 'decode a whole char from the buffer - little endian, most significant bit last'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap Chars.toByteArray(charValue)
        def decoder = new CharDecoder()

        when:
        def decoded = decoder.decode buffer, Endianness.LITTLE_ENDIAN, BitOrder.MOST_SIGNIFICANT_BIT_LAST

        then:
        decoded == decodedValue

        where:
        charValue      | decodedValue
        0x8600 as char | 'a'
        0x4600 as char | 'b'
        0x0C00 as char | '0'
        0x2080 as char | 'Ą'
        0x7B00 as char | 'Þ'
        0x7D00 as char | '¾'
        0x0F00 as char | 'ð'
        0xA348 as char | 'ዅ'
        0xA7C8 as char | 'Ꮵ'
        0xD428 as char | 'ᐫ'
        0x79E8 as char | 'ឞ'
    }
}