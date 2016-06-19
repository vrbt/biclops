package decoders

import com.github.vrbt.biclops.annotations.StringField
import com.github.vrbt.biclops.decoders.StringDecoder
import org.apache.commons.lang3.StringUtils
import spock.lang.Specification
import spock.lang.Unroll

import java.lang.annotation.Annotation
import java.nio.ByteBuffer

/**
 * Created by Robert on 2016-06-19.
 */
class StringTest extends Specification {
    private class BoxedStringFieldObject {
        @StringField
        private String field;
    }

    def 'detect @StringField annotation on a String field'() {
        given:
        def flagField = BoxedStringFieldObject.class.declaredFields.find { field -> StringUtils.equalsIgnoreCase(field.getName(), 'field') }

        when:
        def contains = flagField.annotations.any { Annotation annotation -> StringUtils.containsIgnoreCase(annotation.annotationType().name, 'StringField') }

        then:
        contains
    }

    @Unroll
    def 'decode a string from the buffer'() {
        given:
        def ByteBuffer buffer = ByteBuffer.wrap stringSource.bytes
        def decoder = new StringDecoder()

        when:
        def decoded = decoder.decode buffer

        then:
        decoded == decodedString

        where:
        stringSource | decodedString
        'a'          | 'a'
        //'ab'            | 'ab'
        //' asbc@#HS^ąćŹ' | ' asbc@#HS^ąćŹ'
    }
}