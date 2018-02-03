package it.unisa.dia.gas.plaf.jpbc.util.io;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

import java.io.DataInputStream;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class PairingStreamReader {

    private final byte[] buffer;

    private int cursor;

    private final DataInputStream dis;
    private final ExByteArrayInputStream bais;


    public PairingStreamReader(byte[] buffer, int offset) {
        this.buffer = buffer;

        this.cursor = offset;

        this.bais = new ExByteArrayInputStream(buffer, offset, buffer.length - offset);
        this.dis = new DataInputStream(bais);
    }


    public Element readFieldElement(Field field) {
        Element element = field.newElementFromBytes(buffer, cursor);
        jump(field.getLengthInBytes(element));
        return element;
    }

    public int readInt() {
        try {
            return dis.readInt();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            cursor = bais.getPos();
        }
    }


    private void jump(int length) {
        cursor += length;
        bais.skip(length);
    }

}
