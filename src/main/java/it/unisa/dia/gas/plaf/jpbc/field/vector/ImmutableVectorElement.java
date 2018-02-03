package it.unisa.dia.gas.plaf.jpbc.field.vector;

import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ImmutableVectorElement<E extends Element> extends VectorElement<E> {

    ImmutableVectorElement(VectorElement element) {
        super(element.getField());

        this.coeff.clear();
        for (int i = 0; i < field.n; i++)
            coeff.add((E) element.getAt(i).getImmutable());

        this.immutable = true;
    }

    @Override
    public VectorElement<E> getImmutable() {
        return this;
    }

    @Override
    public VectorElement set(Element e) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public VectorElement set(int value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public VectorElement set(BigInteger value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public VectorElement twice() {
        return duplicate().twice().getImmutable();
    }

    @Override
    public VectorElement setToZero() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public VectorElement setToOne() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public VectorElement setToRandom() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public int setFromBytes(byte[] source, int offset) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public VectorElement square() {
        return duplicate().square().getImmutable();
    }

    @Override
    public VectorElement invert() {
        return duplicate().invert().getImmutable();
    }

    @Override
    public VectorElement negate() {
        return duplicate().negate().getImmutable();
    }

    @Override
    public VectorElement add(Element e) {
        return duplicate().add(e).getImmutable();
    }

    @Override
    public VectorElement mul(Element e) {
        return duplicate().mul(e).getImmutable();
    }

    @Override
    public VectorElement mul(BigInteger n) {
        return duplicate().mul(n).getImmutable();
    }

    @Override
    public VectorElement mulZn(Element e) {
        return (VectorElement) duplicate().mulZn(e).getImmutable();
    }

    @Override
    public VectorElement setFromHash(byte[] source, int offset, int length) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public int setFromBytes(byte[] source) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public Element pow(BigInteger n) {
        return duplicate().pow(n).getImmutable();
    }

    @Override
    public Element halve() {
        return duplicate().halve().getImmutable();
    }

    @Override
    public VectorElement sub(Element element) {
        return duplicate().sub(element).getImmutable();
    }

    @Override
    public Element div(Element element) {
        return duplicate().div(element).getImmutable();
    }

    @Override
    public VectorElement mul(int z) {
        return duplicate().mul(z).getImmutable();
    }

    @Override
    public VectorElement sqrt() {
        return duplicate().sqrt().getImmutable();
    }

}
