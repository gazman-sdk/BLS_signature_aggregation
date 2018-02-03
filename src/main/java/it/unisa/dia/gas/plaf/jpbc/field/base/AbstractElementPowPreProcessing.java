package it.unisa.dia.gas.plaf.jpbc.field.base;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.util.io.FieldStreamReader;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class AbstractElementPowPreProcessing implements ElementPowPreProcessing {
    private static final int DEFAULT_K = 5;

    private final Field field;

    private final int k;
    private final int bits;
    private int numLookups;
    private Element[][] table;


    AbstractElementPowPreProcessing(Element g) {
        this.field = g.getField();
        this.bits = field.getOrder().bitLength();
        this.k = AbstractElementPowPreProcessing.DEFAULT_K;

        initTable(g);
    }

    AbstractElementPowPreProcessing(Field field, byte[] source, int offset) {
        this.field = field;
        this.bits = field.getOrder().bitLength();
        this.k = AbstractElementPowPreProcessing.DEFAULT_K;

        initTableFromBytes(source, offset);
    }

    public Field getField() {
        return field;
    }

    public Element pow(BigInteger n) {
        return powBaseTable(n);
    }


    private void initTableFromBytes(byte[] source, int offset) {
        int lookupSize = 1 << k;
        numLookups = bits / k + 1;
        table = new Element[numLookups][lookupSize];

        FieldStreamReader in = new FieldStreamReader(field, source, offset);

        for (int i = 0; i < numLookups; i++)
            for (int j = 0; j < lookupSize; j++)
                table[i][j] = in.readElement();
    }

    /**
     * build k-bit base table for n-bit exponentiation w/ base a
     *
     * @param g an element
     */
    private void initTable(Element g) {
        int lookupSize = 1 << k;

        numLookups = bits / k + 1;
        table = new Element[numLookups][lookupSize];

        Element multiplier = g.duplicate();

        for (int i = 0; i < numLookups; i++) {
            table[i][0] = field.newOneElement();

            for (int j = 1; j < lookupSize; j++) {
                table[i][j] = multiplier.duplicate().mul(table[i][j - 1]);
            }
            multiplier.mul(table[i][lookupSize - 1]);
        }
    }

    private Element powBaseTable(BigInteger n) {
        /* early abort if raising to power 0 */
        if (n.signum() == 0) {
            return field.newOneElement();
        }

        if (n.compareTo(field.getOrder()) > 0)
            n = n.mod(field.getOrder());

        Element result = field.newOneElement();
        int numLookups = n.bitLength() / k + 1;

        for (int row = 0; row < numLookups; row++) {
            int word = 0;
            for (int s = 0; s < k; s++) {
                word |= (n.testBit(k * row + s) ? 1 : 0) << s;
            }

            if (word > 0) {
                result.mul(table[row][word]);
            }
        }

        return result;
    }

}
