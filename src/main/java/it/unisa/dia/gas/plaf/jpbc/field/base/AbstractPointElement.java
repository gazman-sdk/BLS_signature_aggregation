package it.unisa.dia.gas.plaf.jpbc.field.base;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.jpbc.Vector;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public abstract class AbstractPointElement<E extends Element, F extends AbstractFieldOver> extends AbstractElement<F> implements Point<E>, Vector<E> {

    protected E x, y;


    protected AbstractPointElement(F field) {
        super(field);
    }


    public E getX() {
        return x;
    }

    public E getY() {
        return y;
    }


}
