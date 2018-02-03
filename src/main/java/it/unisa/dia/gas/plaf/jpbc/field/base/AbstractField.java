package it.unisa.dia.gas.plaf.jpbc.field.base;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Field;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public abstract class AbstractField<E extends Element> implements Field<E> {

    protected boolean orderIsOdd = false;


    public E newElement(int value) {
        E e = newElement();
        e.set(value);

        return e;
    }

    public E newElementFromHash(byte[] source, int offset, int length) {
        E e = newElement();
        e.setFromHash(source, offset, length);

        return e;
    }

    public E newElementFromBytes(byte[] source) {
        E e = newElement();
        e.setFromBytes(source);

        return e;
    }

    public E newElementFromBytes(byte[] source, int offset) {
        E e = newElement();
        e.setFromBytes(source, offset);

        return e;
    }

    public E newZeroElement() {
        E e = newElement();
        e.setToZero();

        return e;
    }

    public E newOneElement() {
        E e = newElement();
        e.setToOne();

        return e;
    }

    public E newRandomElement() {
        E e = newElement();
        e.setToRandom();

        return e;
    }

    public boolean isOrderOdd() {
        return orderIsOdd;
    }

    public int getLengthInBytes(Element e) {
        return getLengthInBytes();
    }

    public Element[] twice(Element[] elements) {
        for (Element element : elements) {
            element.twice();
        }

        return elements;
    }

    public Element[] add(Element[] a, Element[] b) {
        for (int i = 0; i < a.length; i++) {
            a[i].add(b[i]);
        }

        return a;
    }

    public ElementPowPreProcessing getElementPowPreProcessingFromBytes(byte[] source, int offset) {
        return new AbstractElementPowPreProcessing(this, source, offset);
    }
}
