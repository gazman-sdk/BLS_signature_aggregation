package it.unisa.dia.gas.plaf.jpbc.field.poly;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractFieldOver;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PolyField<F extends Field> extends AbstractFieldOver<F, PolyElement> {

    public PolyField(F targetField) {
        super(targetField);
    }


    public PolyElement newElement() {
        return new PolyElement(this);
    }

    public BigInteger getOrder() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public PolyElement getNqr() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public int getLengthInBytes() {
        throw new IllegalStateException("Not Implemented yet!");
    }

}
