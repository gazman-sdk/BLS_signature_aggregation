package it.unisa.dia.gas.plaf.jpbc.field.z;

import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractField;
import it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class SymmetricZrField extends AbstractField<SymmetricZrElement> {
    private final BigInteger order;
    final BigInteger halfOrder;

    private SymmetricZrElement nqr;
    private final int fixedLengthInBytes;
    final BigInteger twoInverse;


    private SymmetricZrField(BigInteger order, BigInteger nqr) {
        this.order = order;
        this.orderIsOdd = BigIntegerUtils.isOdd(order);

        this.fixedLengthInBytes = (order.bitLength() + 7) / 8;

        this.twoInverse = BigIntegerUtils.TWO.modInverse(order);

        this.halfOrder = order.divide(BigInteger.valueOf(2));

        if (nqr != null)
            this.nqr = newElement().set(nqr);
    }


    public SymmetricZrElement newElement() {
        return new SymmetricZrElement(this);
    }

    public BigInteger getOrder() {
        return order;
    }

    public SymmetricZrElement getNqr() {
        if (nqr == null) {
            nqr = newElement();
            do {
                nqr.setToRandom();
            } while (nqr.isSqr());
        }

        return nqr.duplicate();
    }

    public int getLengthInBytes() {
        return fixedLengthInBytes;
    }

}
