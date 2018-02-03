package it.unisa.dia.gas.plaf.jpbc.field.quadratic;

import it.unisa.dia.gas.jpbc.Field;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class
DegreeTwoExtensionQuadraticField<F extends Field> extends QuadraticField<F, DegreeTwoExtensionQuadraticElement> {

    public DegreeTwoExtensionQuadraticField(F targetField) {
        super(targetField);
    }


    public DegreeTwoExtensionQuadraticElement newElement() {
        return new DegreeTwoExtensionQuadraticElement(this);
    }

}
