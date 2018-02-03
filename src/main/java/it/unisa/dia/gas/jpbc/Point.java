package it.unisa.dia.gas.jpbc;

/**
 * This interface represents an element with two coordinates.
 * (A point over an elliptic curve).
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 1.0.0
 */
public interface Point<E extends Element> extends Element, Vector<E> {

    /**
     * Returns the x-coordinate.
     *
     * @return the x-coordinate.
     * @since 1.0.0
     */
    E getX();

    /**
     * Returns the y-coordinate.
     *
     * @return the y-coordinate.
     * @since 1.0.0
     */
    E getY();

}
