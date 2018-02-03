package it.unisa.dia.gas.plaf.jpbc.util;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

import java.util.Arrays;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ElementUtils {

    public static Element[] duplicate(Element[] source) {
        Element[] target = new Element[source.length];
        for (int i = 0; i < target.length; i++)
            target[i] = source[i].duplicate();

        return target;
    }

    public static Element[] cloneImmutable(Element[] source) {
        Element[] target = Arrays.copyOf(source, source.length);

        for (int i = 0; i < target.length; i++) {
            Element uElement = target[i];
            if (uElement != null && !uElement.isImmutable())
                target[i] = target[i].getImmutable();
        }

        return target;
    }


    public static Element[][] multiply(Element[][] a, Element[][] b) {
        int n = a.length;
        Field field = a[0][0].getField();

        Element[][] res = new Element[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                res[i][j] = field.newZeroElement();
                for (int k = 0; k < n; k++)
                    res[i][j].add(a[i][k].duplicate().mul(b[k][j]));
            }
        }

        return res;
    }
}
