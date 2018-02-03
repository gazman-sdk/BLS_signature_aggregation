package it.unisa.dia.gas.plaf.jpbc.field.poly;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PolyElement<E extends Element> extends AbstractPolyElement<E, PolyField> {

    PolyElement(PolyField<Field> field) {
        super(field);
    }


    public PolyField getField() {
        return field;
    }

    public PolyElement<E> duplicate() {
        PolyElement copy = new PolyElement((PolyField<Field>) field);

        for (Element e : coefficients) {
            copy.coefficients.add(e.duplicate());
        }

        return copy;
    }

    public PolyElement<E> set(Element e) {
        PolyElement<E> element = (PolyElement<E>) e;

        ensureSize(element.coefficients.size());

        for (int i = 0; i < coefficients.size(); i++) {
            coefficients.get(i).set(element.coefficients.get(i));
        }

        return this;
    }

    public PolyElement<E> set(int value) {
        ensureSize(1);
        coefficients.get(0).set(value);
        removeLeadingZeroes();

        return this;
    }

    public PolyElement<E> set(BigInteger value) {
        ensureSize(1);
        coefficients.get(0).set(value);
        removeLeadingZeroes();

        return this;
    }

    public PolyElement<E> setToRandom() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public PolyElement<E> setFromHash(byte[] source, int offset, int length) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public PolyElement<E> setToZero() {
        ensureSize(0);

        return this;
    }

    public boolean isZero() {
        return coefficients.size() == 0;
    }

    public PolyElement<E> setToOne() {
        ensureSize(1);
        coefficients.get(0).setToOne();

        return this;
    }

    public boolean isOne() {
        return coefficients.size() == 1 && coefficients.get(0).isOne();

    }

    public PolyElement<E> twice() {
        for (E coefficient : coefficients) {
            coefficient.twice();
        }

        return this;
    }

    public PolyElement<E> invert() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public PolyElement<E> negate() {
        for (E coefficient : coefficients) {
            coefficient.negate();
        }

        return this;
    }

    public PolyElement<E> add(Element e) {
        PolyElement<E> element = (PolyElement<E>) e;


        int i, n, n1;
        PolyElement<E> big;

        n = coefficients.size();
        n1 = element.coefficients.size();
        if (n > n1) {
            big = this;
            n = n1;
            n1 = coefficients.size();
        } else {
            big = element;
        }

        ensureSize(n1);
        for (i = 0; i < n; i++) {
            coefficients.get(i).add(element.coefficients.get(i));
        }

        for (; i < n1; i++) {
            coefficients.get(i).set(big.coefficients.get(i));
        }

        removeLeadingZeroes();

        return this;
    }

    public PolyElement<E> sub(Element e) {
        PolyElement<E> element = (PolyElement<E>) e;

        int i, n, n1;

        PolyElement<E> big;

        n = coefficients.size();
        n1 = element.coefficients.size();

        if (n > n1) {
            big = this;
            n = n1;
            n1 = coefficients.size();
        } else {
            big = element;
        }

        ensureSize(n1);

        for (i = 0; i < n; i++) {
            coefficients.get(i).sub(element.coefficients.get(i));
        }

        for (; i < n1; i++) {
            if (big == this) {
                coefficients.get(i).set(big.coefficients.get(i));
//                coefficients.add((E) big.coefficients.get(i).duplicate());
            } else {
                coefficients.get(i).set(big.coefficients.get(i)).negate();
//                coefficients.add((E) big.coefficients.get(i).duplicate().negate());
            }
        }
        removeLeadingZeroes();

        return this;
    }

    public PolyElement<E> div(Element e) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    @Override
    public Element powZn(Element n) {
        return null;
    }

    public PolyElement<E> mul(Element e) {
        PolyElement<E> element = (PolyElement<E>) e;

        int fcount = coefficients.size();
        int gcount = element.coefficients.size();
        int i, j, n;
        PolyElement prod;
        Element e0;

        if (fcount == 0 || gcount == 0) {
            setToZero();
            return this;
        }

        prod = field.newElement();
        n = fcount + gcount - 1;
        prod.ensureSize(n);

        e0 = field.getTargetField().newElement();
        for (i = 0; i < n; i++) {
            Element x = prod.getCoefficient(i);
            x.setToZero();
            for (j = 0; j <= i; j++) {
                if (j < fcount && i - j < gcount) {
                    e0.set(coefficients.get(j)).mul(element.coefficients.get(i - j));
                    x.add(e0);
                }
            }
        }
        prod.removeLeadingZeroes();
        set(prod);

        return this;
    }

    public PolyElement<E> mul(int z) {
        for (E coefficient : coefficients) {
            coefficient.mul(z);
        }

        return this;
    }

    public PolyElement<E> mul(BigInteger n) {
        for (E coefficient : coefficients) {
            coefficient.mul(n);
        }

        return this;
    }

    public PolyElement<E> sqrt() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public boolean isSqr() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public int sign() {
        int res = 0;
        for (E coefficient : coefficients) {
            res = coefficient.sign();
            if (res != 0)
                break;
        }
        return res;
    }

    public boolean isEqual(Element e) {
        if (e == this)
            return true;

        if (!(e instanceof PolyElement))
            return false;

        PolyElement<E> element = (PolyElement<E>) e;

        int n = this.coefficients.size();
        int n1 = element.coefficients.size();
        if (n != n1)
            return false;

        for (int i = 0; i < n; i++) {
            if (!coefficients.get(i).isEqual(element.coefficients.get(i)))
                return false;
        }

        return true;
    }

    public byte[] toBytes() {
        int count = coefficients.size();
        int targetLB = field.getTargetField().getLengthInBytes();
        byte[] buffer = new byte[2 + (count * targetLB)];

        buffer[0] = (byte) ((count >>> 8) & 0xFF);
        buffer[1] = (byte) ((count) & 0xFF);

        for (int len = 2, i = 0; i < count; i++, len += targetLB) {
            byte[] temp = coefficients.get(i).toBytes();
            System.arraycopy(temp, 0, buffer, len, targetLB);
        }

        return buffer;
    }

    public int setFromBytes(byte[] source) {
        return setFromBytes(source, 0);
    }

    @Override
    public int setFromBytes(byte[] source, int offset) {
        int len = offset;
        int count = ((source[len] << 8) + (source[len + 1]));

        ensureSize(count);
        len += 2;
        for (int i = 0; i < count; i++) {
            len += coefficients.get(i).setFromBytes(source, len);
        }
        return len - offset;
    }

    public BigInteger toBigInteger() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public int getDegree() {
        return coefficients.size() - 1;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder("[");

        for (Element e : coefficients) {
            buffer.append(e).append(" ");
        }
        buffer.append("]");
        return buffer.toString();
    }

    void ensureSize(int size) {
        int k = coefficients.size();
        while (k < size) {
            coefficients.add((E) field.getTargetField().newElement());
            k++;
        }
        while (k > size) {
            k--;
            coefficients.remove(coefficients.size() - 1);
        }
    }


    void removeLeadingZeroes() {
        int n = coefficients.size() - 1;
        while (n >= 0) {
            Element e0 = coefficients.get(n);
            if (!e0.isZero())
                return;

            coefficients.remove(n);
            n--;
        }
    }

    PolyElement<E> setFromPolyMod(PolyModElement polyModElement) {
        int i, n = polyModElement.getField().getN();

        ensureSize(n);
        for (i = 0; i < n; i++) {
            coefficients.get(i).set(polyModElement.getCoefficient(i));
        }
        removeLeadingZeroes();

        return this;
    }

}
