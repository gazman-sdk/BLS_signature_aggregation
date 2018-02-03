package it.unisa.dia.gas.plaf.jpbc.pairing.a1;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveField;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteField;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.DegreeTwoExtensionQuadraticField;
import it.unisa.dia.gas.plaf.jpbc.field.z.ZrField;
import it.unisa.dia.gas.plaf.jpbc.pairing.AbstractPairing;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TypeA1Pairing extends AbstractPairing {
    private static final String NAF_MILLER_PROJECTTIVE_METHOD = "naf-miller-projective";
    private static final String MILLER_AFFINE_METHOD = "miller-affine";


    BigInteger r;
    private BigInteger p;
    private long l;

    BigInteger phikOnr;

    Field Fp;
    Field<? extends Point> Fq2;


    public TypeA1Pairing(PairingParameters params) {
        initParams(params);
        initMap(params);
        initFields();
    }

    private void initParams(PairingParameters curveParams) {
        // validate the type
        String type = curveParams.getString("type");
        if (type == null || !"a1".equalsIgnoreCase(type))
            throw new IllegalArgumentException("Type not valid. Found '" + type + "'. Expected 'a1'.");

        // load params
        p = curveParams.getBigInteger("p");
        r = curveParams.getBigInteger("n");
        l = curveParams.getLong("l");
    }


    private void initFields() {
        // Init Zr
        Zr = initFp(r);

        // Init Fp
        Fp = initFp(p);

        //k=2, hence phi_k(q) = q + 1, phikOnr = (q+1)/r
        phikOnr = BigInteger.valueOf(l);

        // Init Eq
        Field<? extends Point> eq = initEq();

        // Init Fq2
        Fq2 = initFi();

        // Init G1, G2, GT
        G1 = eq;
        G2 = G1;
        GT = initGT();
    }


    private Field initFp(BigInteger order) {
        return new ZrField(order);
    }

    private Field<? extends Point> initEq() {
        return new CurveField<>(Fp.newOneElement(), Fp.newZeroElement(), r, phikOnr);
    }

    private Field<? extends Point> initFi() {
        return new DegreeTwoExtensionQuadraticField<Field>(Fp);
    }

    private Field initGT() {
        return new GTFiniteField(r, pairingMap, Fq2);
    }

    private void initMap(PairingParameters curveParams) {
        String method = curveParams.getString("method", NAF_MILLER_PROJECTTIVE_METHOD);

        if (NAF_MILLER_PROJECTTIVE_METHOD.endsWith(method)) {
            pairingMap = new TypeA1TateNafProjectiveMillerPairingMap(this);
        } else if (MILLER_AFFINE_METHOD.equals(method))
            pairingMap = new TypeA1TateAffineMillerPairingMap(this);
        else
            throw new IllegalArgumentException("Pairing method not recognized. Method = " + method);
    }

}