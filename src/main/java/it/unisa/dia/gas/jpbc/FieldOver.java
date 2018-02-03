package it.unisa.dia.gas.jpbc;

/**
 * This interface represents an algebraic structure defined
 * over another.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @see Field
 * @since 1.0.0
 */
public interface FieldOver<F extends Field, E extends Element> extends Field<E> {

    /**
     * Returns the target field.
     *
     * @return the target field.
     * @since 1.0.0
     */
    F getTargetField();

}
