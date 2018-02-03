package it.unisa.dia.gas.plaf.jpbc.util.io;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class FieldStreamReader {

    private final Field field;
    private final byte[] buffer;

    private int cursor;

    private final ExByteArrayInputStream bais;


    public FieldStreamReader(Field field, byte[] buffer, int offset) {
        this.field = field;
        this.buffer = buffer;

        this.cursor = offset;

        this.bais = new ExByteArrayInputStream(buffer, offset, buffer.length - offset);
    }


    public Element readElement() {
        Element element = field.newElementFromBytes(buffer, cursor);
        jump(field.getLengthInBytes(element));
        return element;
    }


    private void jump(int length) {
        cursor += length;
        bais.skip(length);
    }

}
