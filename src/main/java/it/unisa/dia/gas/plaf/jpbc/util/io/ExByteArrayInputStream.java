package it.unisa.dia.gas.plaf.jpbc.util.io;

import java.io.ByteArrayInputStream;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 1.0.0
 */
class ExByteArrayInputStream extends ByteArrayInputStream {

    ExByteArrayInputStream(byte[] buf, int offset, int length) {
        super(buf, offset, length);
    }

    int getPos() {
        return pos;
    }


}
