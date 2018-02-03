package it.unisa.dia.gas.plaf.jpbc.field.vector;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Field;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class VectorElementPowPreProcessing implements ElementPowPreProcessing {
    private final VectorField field;
    private final ElementPowPreProcessing[] processing;

    VectorElementPowPreProcessing(VectorElement vector) {
        this.field = vector.getField();
        this.processing = new ElementPowPreProcessing[vector.getSize()];
        for (int i = 0; i < processing.length; i++) {
            processing[i] = vector.getAt(i).getElementPowPreProcessing();
        }
    }

    public Element pow(BigInteger n) {
        List<Element> coeff = new ArrayList<>(processing.length);
        for (ElementPowPreProcessing processing : processing) {
            coeff.add(processing.pow(n));
        }
        return new VectorElement(field, coeff);
    }

    public Field getField() {
        return field;
    }
}
